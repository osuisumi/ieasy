package com.ieasy.module.common.web.servlet;

import java.util.Date;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ieasy.basic.util.cons.Const;

@WebListener
public class OnlineSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

	// session创建的时候调用
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		//历史访客总数++，访问该站点就进行++
		AppConst.TOTAL_HISTORY_COUNT++;
	}

	// session销毁的时候调用
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// 即将被销毁的session
		HttpSession session = sessionEvent.getSession();
		
		//从Map中如果找到该Session的Id则删除
		if(null != AppConst.SESSION_MAP.get(session.getId())) {
			//从map中将session的索引删除
			AppConst.SESSION_MAP.remove(session.getId());
			AppConst.CURRENT_LOGIN_COUNT--;// 当前用户总数--
		}
	}

	// 添加属性的时候被调用
	public void attributeAdded(HttpSessionBindingEvent event) {
		// 如果是person
		if (event.getName().equals(Const.USER_SESSION)) {
			//保存登陆用户的session
			AppConst.SESSION_MAP.put(event.getSession().getId(), event.getSession());
			
			//当前在线人数++
			AppConst.CURRENT_LOGIN_COUNT++;
			
			//如果登陆人数超过历史同时最大在线人数 【更新人数 更新时间】
			if (AppConst.SESSION_MAP.size() > AppConst.MAX_ONLINE_COUNT) {
				// 更新最大在线人数
				AppConst.MAX_ONLINE_COUNT = AppConst.SESSION_MAP.size();
				// 更新日期
				AppConst.MAX_ONLINE_COUNT_DATE = new Date();
			}
			
			// 得到session
			HttpSession session = event.getSession();
			// 查询该账户有没有在别的机器上登录
			for (HttpSession sess : AppConst.SESSION_MAP.values()) {
				if (event.getValue().equals(sess.getAttribute(Const.USER_SESSION)) && sess.getId() != session.getId()) {
					sess.invalidate();// 销毁session
				}
			}
		}
	}

	// 移除属性时被调用
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if (event.getName().equals(Const.USER_SESSION)) {
			// 从map中将sesion的索引删除
			AppConst.SESSION_MAP.remove(event.getSession().getId());
		}
	}

	// 修改属性时被调用
	public void attributeReplaced(HttpSessionBindingEvent event) {
		if (event.getName().equals(Const.USER_SESSION)) {
			HttpSession session = event.getSession();
			// 重新登录session
			for (HttpSession sess : AppConst.SESSION_MAP.values()) {
				// 如果新账号在其他机器上登录过，则一切的登录失效
				if (event.getValue().equals(sess.getAttribute(Const.USER_SESSION)) && sess.getId() != session.getId()) {
					sess.invalidate();
				}
			}
		}
	}

}
