package com.ieasy.junit.test.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Children {
	private List<Object> list = new ArrayList<Object>();

	public int getSize() {
		return list.size();
	}

	public void addChild(Node node) {
		list.add(node);
	}

	// 拼接孩子节点的JSON字符串
	public String toString() {
		String result = "[";
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			Node node = (Node) it.next();
			if (node.visible) {
				result += node.toString();
				result += ",";
			}
		}
		result = result.substring(0, result.length() - 1);
		result += "]";
		return result;
	}

	// 在孩子节点中寻找功能叶子节点
	public void initializeLeafList(List<Object> leafList) {
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			((Node) it.next()).initializeLeafList(leafList);
		}
	}

	// 孩子节点排序
	public void sortChildren() {
		// 对本层节点进行排序
		// 可根据不同的排序属性，传入不同的比较器，这里传入优先级比较器
		Collections.sort(list, new NodePriorityComparator());
		// 对每个节点的下一层节点进行排序
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			((Node) it.next()).sortChildren();
		}
	}

	// 设置孩子节点为不可见
	public void setTreeNotVisible() {
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			((Node) it.next()).setTreeNotVisible();
		}
	}

	// 设置孩子节点为可见
	public void setTreeVisible() {
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			((Node) it.next()).setTreeVisible();
		}
	}

	// 搜索菜单节点，同时进行功能路径过滤
	public void searchTreeNode(String keyWord) {
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			((Node) it.next()).searchTreeNode(keyWord);
		}
	}
}
