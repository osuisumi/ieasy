package com.ieasy.junit.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PortScanner {

	private int corePoolSize = 50;
	private int maximumPoolSize = 70;
	private long keepAliveTime = 5000;
	private BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();
	private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);

	public void scan(String ip, int start, int end) {
		if (!validate(ip)) {
			System.err.println("Ip format is illegal.");
			return;
		}
		if (start < 0 || end >= (1 << 16)) {
			System.err.println("The port number is illegal.");
			return;
		}
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(ip);
			if (!inetAddress.isReachable(5000)) {
				System.err.println(inetAddress.getHostAddress() + " is not reachable.");
				return;
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
		for (int i = start; i <= end; i++) {
			while (workQueue.size() > maximumPoolSize) {
				try {
					// System.out.println("Thread queue is too long,sleep 500 milliseconds.");
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			threadPoolExecutor.execute(new ScanWorker(inetAddress, i));
		}
		threadPoolExecutor.shutdown();
		while (!threadPoolExecutor.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done.");
	}

	private boolean validate(String ip) {
		if (ip == null || ip.length() == 0)
			return false;
		String[] array = ip.split("\\.");
		if (array.length != 4)
			return false;
		for (String str : array) {
			try {
				Integer.valueOf(str);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	private class ScanWorker implements Runnable {
		InetAddress inetAddress;
		int port;

		public ScanWorker(InetAddress inetAddress, int port) {
			this.inetAddress = inetAddress;
			this.port = port;
		}

		@Override
		public void run() {
			Socket socket = null;
			try {

				socket = new Socket(inetAddress, port);
				// socket.setSoTimeout(1000);
				String serviceName = getServiceName(socket.getPort());
				System.out.println("Port:" + socket.getPort() + " ServiceName:" + serviceName + " Ip:" + inetAddress.getHostAddress());
			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				if (socket != null && !socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private String getServiceName(int port) {
			String name;
			switch (port) {
			case 21:
				name = "FTP";
				break;
			case 23:
				name = "TELNET";
				break;
			case 25:
				name = "SMTP";
				break;
			case 80:
				name = "HTTP";
				break;
			case 110:
				name = "POP";
				break;
			case 135:
				name = "RPC";
				break;
			case 139:
				name = "netBIOS";
				break;
			case 443:
				name = "HTTPS";
				break;
			case 1433:
				name = "SQL server";
				break;
			case 3389:
				name = "Terminal Service";
				break;
			case 1521:
				name = "Oracle";
				break;
			case 8080:
				name = "Tomcat";
				break;
			default:
				name = "Unknown";
			}
			return name;
		}
	}

	public static void main(String[] args) {
		PortScanner portScanner = new PortScanner();
		portScanner.scan("192.168.4.3", 1, 1000);
		// portScanner.scan("74.125.143.115",1,10000);
	}

}
