package com.ieasy.module.system.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ieasy.module.system.util.tree.MenuTreeUtil;
import com.ieasy.module.system.util.tree.Node;

public class AuthForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Object> authTree;

	private List<ACLForm> authOpers;
	
	private List<String> authUrl ;

	public String getAuthTree() {
		if(null != authTree && authTree.size() > 0) {
			// 生成菜单树
			Node node = MenuTreeUtil.buildWeightedMultiTree(authTree);
			// 菜单树排序，从小到达
			node.sortChildren();
			return node.toString();
		}else {
			return "" ;
		}
	}

	public void setAuthTree(List<Object> authTree) {
		this.authTree = authTree;
	}

	public Set<ACLForm> getAuthOpers() {
		if(null != authOpers && authOpers.size() > 0) {
			Set<ACLForm> af = new HashSet<ACLForm>();  
			af.addAll(authOpers);
			return af;
		} else {
			return null ;
		}
	}

	public void setAuthOpers(List<ACLForm> authOpers) {
		this.authOpers = authOpers;
	}

	public List<String> getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(List<String> authUrl) {
		if(null != authUrl && authUrl.size() > 0) {
			Set<String> au = new HashSet<String>();  
			au.addAll(authUrl);
			List<String> authUrlList = new ArrayList<String> ();  
			authUrlList.addAll(au) ;
			authUrl = authUrlList ;
		}
		this.authUrl = authUrl;
	}

}
