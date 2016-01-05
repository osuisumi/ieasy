package com.ieasy.junit.org;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.system.service.IOrgService;
import com.ieasy.module.system.web.form.OrgForm;

public class OrgTest extends BasicJunitTest {
	
	@Inject
	private IOrgService orgService ;
	
	@Test
	public void testOrgInit() {
		//this.orgService.add(null) ;
	}
	
	@Test
	public void testOrgAdd() {
		/*OrgForm f = new OrgForm() ;
		f.setName("Hello") ;
		f.setSn("hello") ;
		this.orgService.add(f) ;*/
		
	}
	
	@Test
	public void testOrgTree() {
		/*List<OrgForm> treeAll = this.orgService.tree("40288186473e57cd01473e57d00d0003") ;
		for (OrgForm f : treeAll) {
			System.out.println(f);
		}*/
		List<OrgForm> tree = this.orgService.tree(null) ;
		for (OrgForm o : tree) {
			System.out.println(o.getId() + "==" + o.getName());
			List<OrgForm> c = o.getChildren() ;
			System.out.println(c.size());
		}
	}

}
