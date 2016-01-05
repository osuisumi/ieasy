package com.ieasy.module.system.util.tree;

import java.util.Comparator;

public class NodePriorityComparator implements Comparator<Object> {

	// 按照 （节点权值+节点编号） 比较
	public int compare(Object o1, Object o2) {
		// 按权值由大到小排序
		int w1 = ((Node) o1).weight;
		int w2 = ((Node) o2).weight;
		if (w1 > w2) {
			return 1;
		} else if (w1 < w2) {
			return -1;
		} else { // 权值相等时，按照节点编号由小到大排序
			int i1 = ((Node) o1).weight;
			int i2 = ((Node) o1).weight;
			return i1 > i2 ? -1 : (i1 == i2 ? 0 : 1);
		}
	}
}
