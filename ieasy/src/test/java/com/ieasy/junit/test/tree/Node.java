package com.ieasy.junit.test.tree;

import java.util.List;

public class Node {

	/**
	 * 节点编号
	 */
	public String id;
	/**
	 * 节点内容
	 */
	public String text;
	/**
	 * 节点内容
	 */
	public String href;
	/**
	 * 父节点编号
	 */
	public String parentId;
	/**
	 * 节点权值
	 */
	public int weight;
	/**
	 * 是否可见，默认为true
	 */
	public boolean visible = true;
	/**
	 * 父节点引用
	 */
	public Node parentNode;
	/**
	 * 孩子节点列表
	 */
	private Children children = new Children();
	
	// 添加孩子节点
	public void addChild(Node node) {
		this.children.addChild(node);
	}
	
	// 先序遍历，拼接JSON字符串
	public String toString() {
		if (visible) {
			String result = "{"
				+ "id : '" + id + "'"
				+ ", text : '" + text + "'"
				+ ", href : '" + href + "'";
			
			if (children != null && children.getSize() != 0) {
				result += ", children : " + children.toString();
			} else {
				result += ", leaf : true";
			}
					
			return result + "}";
		} else {
			return "";
		}
	}
	
	// 兄弟节点横向排序
	public void sortChildren() {
		if (children != null && children.getSize() != 0) {
			children.sortChildren();
		}
	}
	
	// 先序遍历，构造功能叶子列表
	public void initializeLeafList(List<Object> leafList) {
		if (children == null || children.getSize() == 0) {
			leafList.add(this);
		} else {
			children.initializeLeafList(leafList);
		}
	}
	
	// 先序遍历，设置该节点下的所有功能路径为不可见
	public void setTreeNotVisible() {
		visible = false;
		if (children != null && children.getSize() != 0) {
			children.setTreeNotVisible();
		}
	}
	
	// 先序遍历，设置该节点下的所有功能路径为可见
	public void setTreeVisible() {
		visible = true;
		if (children != null && children.getSize() != 0) {
			children.setTreeVisible();
		}
	}
	
	// 设置包含该叶子节点的功能路径可见
	public void setRouteVisible() {
		visible = true;
		for (Node parentNode = this.parentNode; parentNode != null; parentNode = parentNode.parentNode) {
			parentNode.visible = true;
		}
	}
	
	// 对包含该叶子节点的功能路径权值加1
	public void increaseRouteWeight() {
		weight++;
		updateNodeWeightToDB(this);
		for (Node parentNode = this.parentNode; parentNode != null; parentNode = parentNode.parentNode) {
			parentNode.weight++;
			updateNodeWeightToDB(parentNode);
		}
	}
	
	// 更新节点权值到数据库
	public void updateNodeWeightToDB(Node node) {
		// 暂时不实现，实际应用中需要实现该方法
		// 或者用户退出系统时，遍历整棵树，统一更新所有节点的权值到数据库中，应该这样做比较好，一次性统一处理
	}
	
	// 先序遍历，搜索菜单节点，同时进行功能路径过滤
	public void searchTreeNode(String keyWord) {
		if (this.text.indexOf(keyWord) > -1) {
			this.setTreeVisible();
			this.setRouteVisible();
		} else {
			if (children != null && children.getSize() != 0) {
				children.searchTreeNode(keyWord);
			}
		}
	}
	
}
