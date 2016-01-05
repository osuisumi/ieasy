package com.ieasy.junit.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.system.service.IDictService;
import com.ieasy.module.system.web.form.DictJson;

public class DictTest extends BasicJunitTest {
	
	@Inject
	private IDictService dictService ;
	
	@Test
	public void testgenJsonAttrMaps() {
		String keys = "HYZK,ZCJB,ZC" ;
		Map<String, List<DictJson>> attrs = new HashMap<String, List<DictJson>>() ;
		Map<String, List<DictJson>> dicts = this.dictService.generateAttrMaps() ;
		if(null != keys && !"".equals(keys.trim())) {
			String[] spilt = keys.split(",") ;
			for (String key : spilt) {
				attrs.put(key, dicts.get(key)) ;
			}
		}
		System.out.println(attrs);
		System.out.println(JSON.toJSONStringWithDateFormat(attrs, "yyyy-MM-dd HH:mm:ss"));
	}

}
