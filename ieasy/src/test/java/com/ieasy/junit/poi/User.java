package com.ieasy.junit.poi;

import com.ieasy.basic.util.poi.excel.ExcelResources;

public class User {
	
	private int id ;
	
	private String name ;
	
	private String sex ;
	
	private int age ;
	
	private String birth ;

	@ExcelResources(title="唯一标示", order=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ExcelResources(title="用户名", order=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelResources(title="性别", order=2)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ExcelResources(title="年龄", order=3)
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@ExcelResources(title="出生日期", order=5)
	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", sex=" + sex + ", age="
				+ age + ", birth=" + birth + "]";
	}

	public User(int id, String name, String sex, int age, String birth) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.birth = birth;
	}

	public User() {
		super();
	}
}
