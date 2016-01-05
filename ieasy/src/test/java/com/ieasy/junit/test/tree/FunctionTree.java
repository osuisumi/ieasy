package com.ieasy.junit.test.tree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能菜单树类
*/
public class FunctionTree {
	
	public static void main(String[] args) {
		// 读取层次数据结果集列表
		List<Object> dataList = VirtualDataGenerator.getVirtualResult();

		// 构造加权多叉树
		Node root = buildWeightedMultiTree(dataList);
		
		// 构造功能叶子列表
		//List functionLeafList = buildFunctionLeafList(root);
		
		// 对多叉树重新进行横向排序
		root.sortChildren();
		// 输出首次登录后的树形菜单
		System.out.println("首次登录时的树形菜单：\n" + root.toString());	
		
		/*// 进行菜单节点搜索（即功能路径筛选）
		searchTreeNode(root, "321");
		
		// 输出搜索结果
		System.out.println("搜索后的树形菜单：\n" + root.toString());
		
		// 增加功能路径权值
		increaseRouteWeight(root, functionLeafList, "122");
		increaseRouteWeight(root, functionLeafList, "323");
		
		// 对多叉树重新进行横向排序
		root.sortChildren();
		// 输出权值变化后的树形菜单		
		System.out.println("路径权值变化后再次登录时的树形菜单：\n" + root.toString());
		
		// 获取热点功能叶子
		List hotFunctionLeaf = getHotFunctionLeaf(functionLeafList);
		
		// 输出热点功能叶子
		printHotFunctionLeaf(hotFunctionLeaf);*/

		// 程序输出结果如下：
		// 首次登录时的树形菜单：
		// {id : '0', text : '根', children : [{id : '1', text : '1', children : [{id : '11', text : '11', leaf : true},{id : '12', text : '12', children : [{id : '121', text : '121', leaf : true},{id : '122', text : '122', leaf : true},{id : '123', text : '123', leaf : true}]}]},{id : '2', text : '2', children : [{id : '21', text : '21', leaf : true},{id : '22', text : '22', leaf : true}]},{id : '3', text : '3', children : [{id : '31', text : '31', leaf : true},{id : '32', text : '32', children : [{id : '321', text : '321', leaf : true},{id : '322', text : '322', leaf : true},{id : '323', text : '323', leaf : true}]}]},{id : '4', text : '4', children : [{id : '41', text : '41', leaf : true},{id : '42', text : '42', leaf : true},{id : '43', text : '43', leaf : true}]}]}
		// 搜索后的树形菜单：
		// {id : '0', text : '根', children : [{id : '3', text : '3', children : [{id : '32', text : '32', children : [{id : '321', text : '321', leaf : true}]}]}]}
		// 路径权值变化后再次登录时的树形菜单：
		// {id : '0', text : '根', children : [{id : '1', text : '1', children : [{id : '12', text : '12', children : [{id : '122', text : '122', leaf : true},{id : '121', text : '121', leaf : true},{id : '123', text : '123', leaf : true}]},{id : '11', text : '11', leaf : true}]},{id : '3', text : '3', children : [{id : '32', text : '32', children : [{id : '323', text : '323', leaf : true},{id : '321', text : '321', leaf : true},{id : '322', text : '322', leaf : true}]},{id : '31', text : '31', leaf : true}]},{id : '2', text : '2', children : [{id : '21', text : '21', leaf : true},{id : '22', text : '22', leaf : true}]},{id : '4', text : '4', children : [{id : '41', text : '41', leaf : true},{id : '42', text : '42', leaf : true},{id : '43', text : '43', leaf : true}]}]}
		// 热点功能叶子：
		// [{id : '323', text : '323', leaf : true}, {id : '122', text : '122', leaf : true}]
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
			node.parentId = (String) dataRecord.get("parentId");
			node.weight = Integer.parseInt((String) dataRecord.get("weight"));
			nodeList.put(node.id, node);
		}
		// 构造无序的多叉树
		Set entrySet = nodeList.entrySet();
		for (Iterator<Object> it = entrySet.iterator(); it.hasNext();) {
			Node node = (Node) ((Map.Entry) it.next()).getValue();
			if (node.parentId == null || node.parentId.equals("")) {
				root = node;
			} else {
				((Node) nodeList.get(node.parentId)).addChild(node);
				// 在节点中增加一个父节点的引用
				node.parentNode = (Node) nodeList.get(node.parentId);
			}
		}

		return root;
	} 
	
	/**
	 * 构造功能叶子列表
	 * @param root
	 * @return
	 */
	public static List<Object> buildFunctionLeafList(Node root) {
		List<Object> functionLeafList = new ArrayList<Object>();
		root.initializeLeafList(functionLeafList);
		return functionLeafList;
	}
	
	/**
	 * 进行菜单节点搜索（即功能路径筛选）
	 * @param root
	 * @param keyWord
	 */
	public static void searchTreeNode(Node root, String keyWord) {
		// 首先设置整棵树的功能路径为不可见
		root.setTreeNotVisible();
		// 在整棵功能树中搜索包含关键字的节点，并进行路径筛选
		root.searchTreeNode(keyWord);					
	}
	
	/**
	 * 增加功能路径权值
	 * @param root
	 */
	public static void increaseRouteWeight(Node root, List<Object> functionLeafList, String nodeId) {
		// 首先设置整棵树的功能路径为可见
		root.setTreeVisible();		
		// 对包含功能叶子节点的路径权值加1
		for (Iterator<Object> it = functionLeafList.iterator(); it.hasNext();) {
			Node leafNode = (Node) it.next();
			if (leafNode.id.equals(nodeId)) {
				leafNode.increaseRouteWeight();
			}
		}		
	}
	
	/**
	 * 获取热点功能叶子
	 * @param functionLeafList
	 * @return
	 */
	public static List<Object> getHotFunctionLeaf(List<Object> functionLeafList) {
		int count = 0;
		int totalWeight = 0;
		BigDecimal avgWeight;
		for (Iterator<Object> it = functionLeafList.iterator(); it.hasNext();) {
			Node node = (Node) it.next();
			totalWeight += node.weight;
			count++;
		}
		avgWeight = (new BigDecimal(totalWeight)).divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
		List<Object> retList = new ArrayList<Object>();
		for (Iterator<Object> it = functionLeafList.iterator(); it.hasNext();) {
			Node node = (Node) it.next();
			if (node.weight > avgWeight.doubleValue()) {
				retList.add(node);
			}
		}
		return retList;
	}
	
	/**
	 * 输出热点功能叶子
	 * @param hotFunctionLeaf
	 */
	public static void printHotFunctionLeaf(List<Object> hotFunctionLeaf) {
		System.out.println("热点功能叶子：\n" + hotFunctionLeaf);
	}
	
	
}




/**
 * 构造虚拟的层次数据
 */
class VirtualDataGenerator {
	// 构造无序的结果集列表，实际应用中，该数据应该从数据库中查询获得；
	public static List<Object> getVirtualResult() {				
		List<Object> dataList = new ArrayList<Object>();
		
		HashMap<String, Object> dataRecord1 = new HashMap<String, Object>();
		dataRecord1.put("id", "0");
		dataRecord1.put("text", "根");
		dataRecord1.put("parentId", "");
		dataRecord1.put("weight", "1");
		
		HashMap<String, Object> dataRecord2 = new HashMap<String, Object>();
		dataRecord2.put("id", "1");
		dataRecord2.put("text", "1");
		dataRecord2.put("parentId", "0");
		dataRecord2.put("weight", "1");
		
		HashMap<String, Object> dataRecord3 = new HashMap<String, Object>();
		dataRecord3.put("id", "2");
		dataRecord3.put("text", "2");
		dataRecord3.put("parentId", "0");
		dataRecord3.put("weight", "1");
						
		HashMap<String, Object> dataRecord4 = new HashMap<String, Object>();
		dataRecord4.put("id", "3");
		dataRecord4.put("text", "3");
		dataRecord4.put("parentId", "0");
		dataRecord4.put("weight", "1");
						
		HashMap<String, Object> dataRecord5 = new HashMap<String, Object>();
		dataRecord5.put("id", "4");
		dataRecord5.put("text", "4");
		dataRecord5.put("parentId", "0");
		dataRecord5.put("weight", "1");
		
		HashMap<String, Object> dataRecord6 = new HashMap<String, Object>();
		dataRecord6.put("id", "11");
		dataRecord6.put("text", "11");
		dataRecord6.put("parentId", "1");
		dataRecord6.put("weight", "1");
		
		HashMap<String, Object> dataRecord7 = new HashMap<String, Object>();
		dataRecord7.put("id", "12");
		dataRecord7.put("text", "12");
		dataRecord7.put("parentId", "1");
		dataRecord7.put("weight", "1");
		
		HashMap<String, Object> dataRecord8 = new HashMap<String, Object>();
		dataRecord8.put("id", "21");
		dataRecord8.put("text", "21");
		dataRecord8.put("parentId", "2");
		dataRecord8.put("weight", "1");
		
		HashMap<String, Object> dataRecord9 = new HashMap<String, Object>();
		dataRecord9.put("id", "22");
		dataRecord9.put("text", "22");
		dataRecord9.put("parentId", "2");
		dataRecord9.put("weight", "1");
		
		HashMap<String, Object> dataRecord10 = new HashMap<String, Object>();
		dataRecord10.put("id", "31");
		dataRecord10.put("text", "31");
		dataRecord10.put("parentId", "3");
		dataRecord10.put("weight", "1");
		
		HashMap<String, Object> dataRecord11 = new HashMap<String, Object>();
		dataRecord11.put("id", "32");
		dataRecord11.put("text", "32");
		dataRecord11.put("parentId", "3");
		dataRecord11.put("weight", "1");
		
		HashMap<String, Object> dataRecord12 = new HashMap<String, Object>();
		dataRecord12.put("id", "41");
		dataRecord12.put("text", "41");
		dataRecord12.put("parentId", "4");
		dataRecord12.put("weight", "1");
		
		HashMap<String, Object> dataRecord13 = new HashMap<String, Object>();
		dataRecord13.put("id", "42");
		dataRecord13.put("text", "42");
		dataRecord13.put("parentId", "4");
		dataRecord13.put("weight", "1");
		
		HashMap<String, Object> dataRecord14 = new HashMap<String, Object>();
		dataRecord14.put("id", "43");
		dataRecord14.put("text", "43");
		dataRecord14.put("parentId", "4");
		dataRecord14.put("weight", "1");
				
		HashMap<String, Object> dataRecord15 = new HashMap<String, Object>();
		dataRecord15.put("id", "121");
		dataRecord15.put("text", "121");
		dataRecord15.put("parentId", "12");
		dataRecord15.put("weight", "1");
		
		HashMap<String, Object> dataRecord16 = new HashMap<String, Object>();
		dataRecord16.put("id", "122");
		dataRecord16.put("text", "122");
		dataRecord16.put("parentId", "12");
		dataRecord16.put("weight", "1");
		
		HashMap<String, Object> dataRecord17 = new HashMap<String, Object>();
		dataRecord17.put("id", "123");
		dataRecord17.put("text", "123");
		dataRecord17.put("parentId", "12");
		dataRecord17.put("weight", "1");
				
		HashMap<String, Object> dataRecord18 = new HashMap<String, Object>();
		dataRecord18.put("id", "321");
		dataRecord18.put("text", "321");
		dataRecord18.put("parentId", "32");
		dataRecord18.put("weight", "1");
		
		HashMap<String, Object> dataRecord19 = new HashMap<String, Object>();
		dataRecord19.put("id", "322");
		dataRecord19.put("text", "322");
		dataRecord19.put("parentId", "32");
		dataRecord19.put("weight", "1");
		
		HashMap<String, Object> dataRecord20 = new HashMap<String, Object>();
		dataRecord20.put("id", "323");
		dataRecord20.put("text", "323");
		dataRecord20.put("parentId", "32");
		dataRecord20.put("weight", "1");
				
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