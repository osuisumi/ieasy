package com.ieasy.basic.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
/**
 * 生成序号
 * @author feizi
 * @time 2014-11-5下午5:27:23
 */
public class CreateSerialNo {
     
    private static  Map<String,String> map=new HashMap<String, String>(); 
    private static String STATNUM="000001";
     
    /**
     * 获取年月日
     * @return
     */
    public String getTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
         
        return df.format(cal.getTime());
    }
     
    /**
     * 判断序号是否到了最后一个
     * @param s
     * @return
     */
    public String getLastSixNum(String s){
         String rs=s;
         int i=Integer.parseInt(rs);
         i+=1;
         rs=""+i;
         for (int j = rs.length(); j <6; j++) {
            rs="0"+rs;
        }        
        return rs;  
    }
     
    /**
     * 产生不重复的号码  加锁 
     * @return
     */
    public synchronized  String getNum(){
        String yearAMon = getTime();
        String last6Num=map.get(yearAMon);
        if(last6Num==null){
            map.put(yearAMon,STATNUM);
        }else{
            map.put(yearAMon,getLastSixNum(last6Num));
        }
         
        return yearAMon+map.get(yearAMon);
    }
     
    /**
     * main测试
     * @param args
     */
    public static void main(String[] args) {
        CreateSerialNo t= new CreateSerialNo();
        for (int i = 0; i < 200; i++) {
            System.out.println(t.getNum());
        }
    }
}