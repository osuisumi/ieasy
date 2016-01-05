package com.ieasy.basic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取登录用户IP地址
 * 
 * @param request
 * @return
 */
public class IpUtil {

	/**
	 * 获取登录用户的IP地址
	 * 对手机无效
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	public static String getIpByMobile(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip != null && (ip.equals("127.0.0.1") || ip.startsWith("192")))
			ip = getIpAddr(request);

		return ip;
	}

	public static JSONObject getIp(String ip) {
		String result = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);

		JSONObject obj = (JSONObject) JSON.parse(result);

		return obj;

	}

	public static String getIpAddr() {

		String ip = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("province") + " ";
		info += obj.getString("city") + " ";
		info += obj.getString("isp");

		return info;
	}

	public static String getIpCity() {
		String ip = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=");
		String info = "";
		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("city");

		return info;
	}

	public static String getWeather(String location) {

		String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location + "&output=json&ak=640f3985a6437dad8135dae98d775a09";
		String ip = new Http().get(url);

		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);

		if ("success".equals(obj.getString("status"))) {
			JSONObject data = obj.getJSONArray("results").getJSONObject(0);

			data = data.getJSONArray("weather_data").getJSONObject(0);
			info += info += location + "天气  ";
			info += data.getString("weather") + " ";
			info += data.getString("wind") + " ";
			info += data.getString("temperature") + " ";
		}

		return info;
	}

	public static long strToLong(String strip) {
		long[] ip = new long[4];
		int position1 = strip.indexOf(".");
		int position2 = strip.indexOf(".", position1 + 1);
		int position3 = strip.indexOf(".", position2 + 1);
		ip[0] = Long.parseLong(strip.substring(0, position1));
		ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strip.substring(position3 + 1));
		return ((ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]);

	}

	/**
	 * 通过IP获取地址(需要联网，调用淘宝的IP库)
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIpInfo(String ip) {
		if (ip.equals("本地")) {
			ip = "127.0.0.1";
		}
		String info = "";
		try {
			URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
			htpcon.setRequestMethod("GET");
			htpcon.setDoOutput(true);
			htpcon.setDoInput(true);
			htpcon.setUseCaches(false);

			InputStream in = htpcon.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			JSONObject obj = (JSONObject) JSON.parse(temp.toString());
			if (obj.getIntValue("code") == 0) {
				JSONObject data = obj.getJSONObject("data");
				info += data.getString("country") + " ";
				info += data.getString("region") + " ";
				info += data.getString("city") + " ";
				info += data.getString("isp");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static void main(String[] args) {
		System.out.println(getIp("210.21.28.34"));
		System.out.println(getIpAddr());
		System.out.println(getWeather("广州"));
		System.out.println(getIpCity());
	}

}
