package com.ieasy.junit.init;

import javax.inject.Inject;

import org.junit.Test;

import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.common.init.IInitAdminService;
import com.ieasy.module.common.init.IInitService;

public class InitSystem extends BasicJunitTest {
	
	@Inject
	private IInitService initService ;
	
	@Inject
	private IInitAdminService initAdminPermitService ;
	
	@Test
	public void testInitEntityByXml(){
		initService.initEntityByXml() ;
	}
	/*
	@Test
	public void testInitAdmin(){
		initAdminPermitService.addInitAdmin() ;
	}
	*/
}
