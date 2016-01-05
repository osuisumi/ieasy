package com.ieasy.module.system.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能菜单树类
*/
public class MenuTreeUtil {
	
	public static void main(String[] args) {
		// 读取层次数据结果集列表
		List<Object> dataList = VirtualDataGenerator.getVirtualResult();

		// 构造加权多叉树
		Node root = buildWeightedMultiTree(dataList);
		// 对多叉树重新进行横向排序
		root.sortChildren();
		// 输出首次登录后的树形菜单
		System.out.println("首次登录时的树形菜单：\n" + root.toString());	
	}			
	
	/**
	 * 构造加权多叉树
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Node buildWeightedMultiTree(List<Object> dataList) {
		
		// 节点列表（散列表，用于临时存储节点对象）
		HashMap<String, Object> nodeList = new HashMap<String, Object>();
		// 根节点
		Node root = null;
		// 根据结果集构造节点列表（存入散列表）
		for (Iterator<Object> it = dataList.iterator(); it.hasNext();) {
			Map<String, Object> dataRecord = (Map<String, Object>) it.next();
			Node node = new Node();
			node.id = (String) dataRecord.get("id");
			node.text = (String) dataRecord.get("text");
			node.href = (String) dataRecord.get("href");
			node.iconCls = (String) dataRecord.get("iconCls");
			node.state = (String) dataRecord.get("state");
			node.weight = (Integer)dataRecord.get("weight");
			node.pid = (String) dataRecord.get("pid");
			nodeList.put(node.id, node);
		}
		// 构造无序的多叉树
		Set entrySet = nodeList.entrySet();
		for (Iterator<Object> it = entrySet.iterator(); it.hasNext();) {
			Node node = (Node) ((Map.Entry) it.next()).getValue();
			if (node.pid == null || node.pid.equals("")) {
				root = node;
			} else {
				((Node) nodeList.get(node.pid)).addChild(node);
				// 在节点中增加一个父节点的引用
				node.parentNode = (Node) nodeList.get(node.pid);
			}
		}

		return root;
	} 
	
	
}




/**
 * 构造虚拟的层次数据
 */
class VirtualDataGenerator {
	// 构造无序的结果集列表，实际应用中，该数据应该从数据库中查询获得；
	public static List<Object> getVirtualResult() {				
		List<Object> dataList = new ArrayList<Object>();
		
		HashMap<String, Object> dataRecord11g = new HashMap<String, Object>();
		dataRecord11g.put("id", "111111");
		dataRecord11g.put("text", "根11111");
		dataRecord11g.put("pid", "");
		dataRecord11g.put("weight", 1);
		
		HashMap<String, Object> dataRecord1 = new HashMap<String, Object>();
		dataRecord1.put("id", "0");
		dataRecord1.put("text", "根");
		dataRecord1.put("pid", "");
		dataRecord1.put("weight", 1);
		
		HashMap<String, Object> dataRecord2 = new HashMap<String, Object>();
		dataRecord2.put("id", "1");
		dataRecord2.put("text", "1");
		dataRecord2.put("pid", "0");
		dataRecord2.put("weight", 1);
		
		HashMap<String, Object> dataRecord3 = new HashMap<String, Object>();
		dataRecord3.put("id", "2");
		dataRecord3.put("text", "2");
		dataRecord3.put("pid", "0");
		dataRecord3.put("weight", 1);
						
		HashMap<String, Object> dataRecord4 = new HashMap<String, Object>();
		dataRecord4.put("id", "3");
		dataRecord4.put("text", "3");
		dataRecord4.put("pid", "0");
		dataRecord4.put("weight", 1);
						
		HashMap<String, Object> dataRecord5 = new HashMap<String, Object>();
		dataRecord5.put("id", "4");
		dataRecord5.put("text", "4");
		dataRecord5.put("pid", "0");
		dataRecord5.put("weight", 1);
		
		HashMap<String, Object> dataRecord6 = new HashMap<String, Object>();
		dataRecord6.put("id", "11");
		dataRecord6.put("text", "11");
		dataRecord6.put("pid", "1");
		dataRecord6.put("weight", 1);
		
		HashMap<String, Object> dataRecord7 = new HashMap<String, Object>();
		dataRecord7.put("id", "12");
		dataRecord7.put("text", "12");
		dataRecord7.put("pid", "1");
		dataRecord7.put("weight", 1);
		
		HashMap<String, Object> dataRecord8 = new HashMap<String, Object>();
		dataRecord8.put("id", "21");
		dataRecord8.put("text", "21");
		dataRecord8.put("pid", "2");
		dataRecord8.put("weight", 1);
		
		HashMap<String, Object> dataRecord9 = new HashMap<String, Object>();
		dataRecord9.put("id", "22");
		dataRecord9.put("text", "22");
		dataRecord9.put("pid", "2");
		dataRecord9.put("weight", 1);
		
		HashMap<String, Object> dataRecord10 = new HashMap<String, Object>();
		dataRecord10.put("id", "31");
		dataRecord10.put("text", "31");
		dataRecord10.put("pid", "3");
		dataRecord10.put("weight", 1);
		
		HashMap<String, Object> dataRecord11 = new HashMap<String, Object>();
		dataRecord11.put("id", "32");
		dataRecord11.put("text", "32");
		dataRecord11.put("pid", "3");
		dataRecord11.put("weight", 1);
		
		HashMap<String, Object> dataRecord12 = new HashMap<String, Object>();
		dataRecord12.put("id", "41");
		dataRecord12.put("text", "41");
		dataRecord12.put("pid", "4");
		dataRecord12.put("weight", 1);
		
		HashMap<String, Object> dataRecord13 = new HashMap<String, Object>();
		dataRecord13.put("id", "42");
		dataRecord13.put("text", "42");
		dataRecord13.put("pid", "4");
		dataRecord13.put("weight", 1);
		
		HashMap<String, Object> dataRecord14 = new HashMap<String, Object>();
		dataRecord14.put("id", "43");
		dataRecord14.put("text", "43");
		dataRecord14.put("pid", "4");
		dataRecord14.put("weight", 1);
				
		HashMap<String, Object> dataRecord15 = new HashMap<String, Object>();
		dataRecord15.put("id", "121");
		dataRecord15.put("text", "121");
		dataRecord15.put("pid", "12");
		dataRecord15.put("weight", 1);
		
		HashMap<String, Object> dataRecord16 = new HashMap<String, Object>();
		dataRecord16.put("id", "122");
		dataRecord16.put("text", "122");
		dataRecord16.put("pid", "12");
		dataRecord16.put("weight", 1);
		
		HashMap<String, Object> dataRecord17 = new HashMap<String, Object>();
		dataRecord17.put("id", "123");
		dataRecord17.put("text", "1111");
		dataRecord17.put("pid", "12");
		dataRecord17.put("weight", 1);
				
		HashMap<String, Object> dataRecord18 = new HashMap<String, Object>();
		dataRecord18.put("id", "321");
		dataRecord18.put("text", "2222");
		dataRecord18.put("pid", "32");
		dataRecord18.put("weight", 2);
		
		HashMap<String, Object> dataRecord19 = new HashMap<String, Object>();
		dataRecord19.put("id", "322");
		dataRecord19.put("text", "3333");
		dataRecord19.put("pid", "32");
		dataRecord19.put("weight", 3);
		
		HashMap<String, Object> dataRecord20 = new HashMap<String, Object>();
		dataRecord20.put("id", "323");
		dataRecord20.put("text", "4444");
		dataRecord20.put("pid", "32");
		dataRecord20.put("weight", 4);
				
		dataList.add(dataRecord11g);
		dataList.add(dataRecord1);
		dataList.add(dataRecord2);
		dataList.add(dataRecord3);
		dataList.add(dataRecord4);
		dataList.add(dataRecord5);
		dataList.add(dataRecord6);
		dataList.add(dataRecord7);		
		dataList.add(dataRecord8);
		dataList.add(dataRecord9);
		dataList.add(dataRecord10);
		dataList.add(dataRecord11);
		dataList.add(dataRecord12);
		dataList.add(dataRecord13);
		dataList.add(dataRecord14);		
		dataList.add(dataRecord15);
		dataList.add(dataRecord16);
		dataList.add(dataRecord17);
		dataList.add(dataRecord18);
		dataList.add(dataRecord19);
		dataList.add(dataRecord20);
		
		return dataList;
	}	
}