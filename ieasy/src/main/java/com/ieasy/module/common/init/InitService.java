package com.ieasy.module.common.init;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class InitService implements IInitService, BeanFactoryAware {
	
	private String path;

	private BeanFactory factory = null;
	
	@Inject
	private IInitAdminService initAdmin ;

	@SuppressWarnings("unchecked")
	@Override
	public void initEntityByXml() {
		try {
			// 解析init.xml文档
			Document doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
			// 得到根元素
			Element root = doc.getRootElement();
			// 得到包名
			String pkg = root.valueOf("@package");

			// 得到根元素下的entity集合
			List<Element> entities = root.selectNodes("entity");

			for (Iterator<Element> iter = entities.iterator(); iter.hasNext();) {
				Element e = iter.next();
				
				//exist属性标示为1则不进行初始化
				if("0".equals(e.attributeValue("exist"))) {
					addEntity(e, pkg, null, null, e.attributeValue("class"), e.attributeValue("parent"));
				}
			}
			
			//初始化超级管理员账号和角色授权
			this.initAdmin.addInitAdmin() ;
			System.out.println("系统数据初始化完成...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param e	标签元素
	 * @param pkg 包名
	 * @param parent 父对象
	 * @param methodString 调用的方法
	 * @param clazzName	类名称
	 * @param parentField 对应实体对象的父对象属性
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addEntity(Element e, String pkg, Object parent, String methodString, String clazzName, String parentField) {
		try {
			// 处理当前Element
			// 1. 要创建一个什么样类型的对象
			// 要创建类的全包名
			String className = pkg + "." + clazzName;
			// 根据类名创建实体对象
			Object entity = Class.forName(className).newInstance();
			// 给实体对象当中的属性赋值
			Iterator iter = e.attributeIterator();
			while (iter.hasNext()) {
				Attribute attr = (Attribute) iter.next();
				String propName = attr.getName();
				// 判断除了class和call属性的其它属性赋值
				if (!"class".equals(propName) && !"method".equals(propName)) {
					String propValue = attr.getValue();
					BeanUtils.copyProperty(entity, propName, propValue);
				}
			}
			if (null != parentField) {
				// 给entity父实体属性赋值
				BeanUtils.copyProperty(entity, parentField, parent);
			}

			// 2. 存储对象(调用哪一个Service的哪一个方法?)
			String method = e.attributeValue("method");
			if (method != null) {
				methodString = method;
			}

			if (methodString == null) {
				throw new RuntimeException("无法创建实体对象,调用方法未知!");
			}

			// 3. 调用相应的方法存储实体
			String[] mesg = methodString.split("\\.");
			String serviceName = mesg[0];
			String methodName = mesg[1];
			// 得到Service对象
			Object serviceObject = factory.getBean(serviceName);
			// 得到要调用的Servce对象上的方法的反射类
			for (Method m : serviceObject.getClass().getMethods()) {
				if (methodName.equals(m.getName())) {
					// 调用这个方法
					m.invoke(serviceObject, entity);
				}
			}

			// 4. 考虑当前Element下有没有子元素
			List<Element> subEntities = e.elements("entity");
			for (Iterator<Element> itr = subEntities.iterator(); itr.hasNext();) {
				Element subElement = itr.next();
				addEntity(subElement, pkg, entity, methodString, clazzName, parentField);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public void setBeanFactory(BeanFactory factory) throws BeansException {
		this.factory = factory;
	}
}
