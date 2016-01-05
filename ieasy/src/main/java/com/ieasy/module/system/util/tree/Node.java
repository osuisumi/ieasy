package com.ieasy.module.system.util.tree;


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
	 * 节点链接
	 */
	public String href;
	/**
	 * 节点图标
	 */
	public String iconCls;
	/**
	 * 节点状态
	 */
	public String state;
	/**
	 * 父节点编号
	 */
	public String pid;
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
				+ ", href : '" + href + "'"
				+ ", iconCls : '" + iconCls + "'"
				+ ", state : '" + state + "'"
				+ ", pid : '" + pid + "'";
			
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
	
}
