package com.ieasy.basic.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class HtmlUtil {

	/**
	 * 生成html的select元素
	 * @param datas	<option的value, option的text>
	 * @return	构建好的select元素html代码
	 */
	public static String generateSelect(Map<String, String> datas) {
		return generateSelect(datas, false);
	}
	
	/**
	 * 生成html的select元素
	 * @param 	datas	<option的value, option的text>
	 * @param	withSelectTip 是否在第一个选择中添加“请选择”
	 * @return	构建好的select元素html代码
	 */
	public static String generateSelect(Map<String, String> datas, boolean withSelectTip) {
		return generateSelect(datas, withSelectTip, null, true);
	}
	
	/**
	 * 生成html的select元素
	 * @param 	datas	<option的value, option的text>
	 * @param	withSelectTip 是否在第一个选择中添加“请选择”
	 * @param	defaultValue	默认选择的值
	 * @return	构建好的select元素html代码
	 */
	public static String generateSelect(Map<String, String> datas, boolean withSelectTip, String defaultValue) {
		return generateSelect(datas, withSelectTip, defaultValue, true);
	}
	
	/**
	 * 生成html的select元素
	 * @param 	datas	<option的value, option的text>
	 * @param	withSelectTip 是否在第一个选择中添加“请选择”
	 * @param	defaultValue	默认选择的值
	 * @param	setValue		是否设置option的value属性
	 * @return	构建好的select元素html代码
	 */
	public static String generateSelect(Map<String, String> datas, boolean withSelectTip, String defaultValue, boolean setValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("<select>");
		
		if (withSelectTip) {
			sb.append("<option value=''>请选择</option>");
		}
		
		Set<Entry<String, String>> entrySet = datas.entrySet();
		String defaultHtml = "";
		for (Entry<String, String> entry : entrySet) {
			defaultHtml = "";
			if (StringUtils.isNotBlank(defaultValue) && entry.getKey().equals(defaultValue)) {
				defaultHtml = " selected";
			}
			sb.append("<option");
			if (setValue) {
				sb.append(" value='" + entry.getKey() + "'");
			}
			sb.append(defaultHtml + ">" + entry.getValue() + "</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * 不设置option的value属性
	 * @param datas	<option的value, option的text>
	 * @return	构建好的select元素html代码
	 */
	public static String generateSelectNoValue(Map<String, String> datas) {
		return generateSelect(datas, false, null, false);
	}
	
	/**
	 * 过滤所以空格
	 * @param str
	 * @return
	 */
	public static String filterAllWhite(String str) {
		if(str.indexOf(" ") !=-1) {
			str = str.replaceAll(" ","") ;
		}
		return str ;
	}
	
	/**
	 * 基本功能：替换标记以正常显示
	 * @param input
	 * @return
	 */
	public static String replaceTag(String input) {   
        if (!hasSpecialChars(input)) {   
            return input;   
        }   
        StringBuffer filtered = new StringBuffer(input.length());   
        char c;   
        for (int i = 0; i <= input.length() - 1; i++) {   
            c = input.charAt(i);   
            switch (c) {   
            case '<':   
                filtered.append("&lt;");   
                break;   
            case '>':   
                filtered.append("&gt;");   
                break;   
            case '"':   
                filtered.append("&quot;");   
                break;   
            case '&':   
                filtered.append("&amp;");   
                break;   
            default:   
                filtered.append(c);   
            }   
  
        }   
        return (filtered.toString());   
    }   
	
	/**
	 * 基本功能：判断标记是否存在  
	 * @param input
	 * @return
	 */
    public static boolean hasSpecialChars(String input) {   
        boolean flag = false;   
        if ((input != null) && (input.length() > 0)) {   
            char c;   
            for (int i = 0; i <= input.length() - 1; i++) {   
                c = input.charAt(i);   
                switch (c) {   
                case '>':   
                    flag = true;   
                    break;   
                case '<':   
                    flag = true;   
                    break;   
                case '"':   
                    flag = true;   
                    break;   
                case '&':   
                    flag = true;   
                    break;   
                }   
            }   
        }   
        return flag;   
    }   


	
	/**
     * 去除html代码
     * @param inputString
     * @return
     */
    public static String HtmltoText(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;          
        java.util.regex.Pattern p_ba;
        java.util.regex.Matcher m_ba;
        
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
            String patternStr = "\\s+";
            
            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签
         
            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            
            p_ba = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            htmlStr = m_ba.replaceAll(""); //过滤空格
         
         textStr = htmlStr;
         
        }catch(Exception e) {
                    System.err.println("Html2Text: " + e.getMessage());
        }          
        return textStr;//返回文本字符串
     }
	
	public static void main(String[] args) {
		System.out.println(HtmltoText("<h1>AAA</h1><img src='cid:d:Q2.gif'/>"));
	}
	
}
