package com.ieasy.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath*:spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicJunitTest {

	/**
	 * 该单元测试类型必须写个方法,不然Maven test无法通过
	 */
	@Test
	public void test() {}
	
}
