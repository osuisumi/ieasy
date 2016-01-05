package com.ieasy.basic.util.springmvc;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ieasy.basic.util.ConfigUtil;

@Component
public class ServletContextRealPathResolver implements RealPathResolver, ServletContextAware {

	public String get(String path) {
		String realpath = context.getRealPath(path);
		// tomcat8.0获取不到真实路径，通过/获取路径
		if (realpath == null) {
			realpath = context.getRealPath("/") + path;
		}
		return realpath;
	}

	public String getContextPath() {
		return context.getContextPath();
	}
	
	@Override
	public String getLocalIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();//获得本机IP
			return ip ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getParentDir() {
		return new File(context.getRealPath("/").toString()).getParent();
	}

	public String getWebRoot() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	}

	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}

	private ServletContext context;

	@Override
	public String getServerRoot() {
		String port = ConfigUtil.get("post") ;
		return "http://"+getLocalIp()+(null != port && !"".equals(port)?":"+port:"");
	}

}
