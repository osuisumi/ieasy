package com.ieasy.junit.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class IpScanner {
	
	private int corePoolSize=30;
    private int maximumPoolSize=50;
    private long keepAliveTime=5000;
    private BlockingDeque<Runnable> workQueue=new LinkedBlockingDeque<Runnable>();
    private ExecutorService threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
            TimeUnit.MILLISECONDS,workQueue);
    public void scan(String start,String end){
        if(!validate(start)||!validate(end)){
            System.err.println("Ip format Exception.");
            return;
        }
        String pre = start.substring(0, start.lastIndexOf(".")+1);
        int first = Integer.valueOf(start.split("\\.")[3]);
        int last = Integer.valueOf(end.split("\\.")[3]);
        if(first>last){
            System.err.println("Ip format Exception.");
            return;
        }
        for(int i=first;i<=last;i++){
            String ipStr=pre+i;
            while(workQueue.size()>maximumPoolSize){
                try {
//                    System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadPoolExecutor.execute(new ScanWorker(ipStr));
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
 
    private boolean validate(String ip){
        if(ip==null||ip.length()==0) return false;
        String[] array=ip.split("\\.");
        if(array.length!=4) return false;
        for(String str:array){
            try {
                Integer.valueOf(str);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
 
    private class ScanWorker implements Runnable{
        private String ipStr;
        public ScanWorker(String ipStr){
            this.ipStr=ipStr;
        }
        @Override
        public void run() {
            try {
                InetAddress inetAddress=InetAddress.getByName(ipStr);
                if(inetAddress.isReachable(5000)) { // wait 5 seconds
                    System.out.println("HostName:" + inetAddress.getHostName() + " Ip:" + inetAddress.getHostAddress());
                }
            } catch (UnknownHostException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
 
    public static void main(String[] args) {
        IpScanner ipScanner=new IpScanner();
        //ipScanner.scan("192.168.1.1","192.168.1.255");
        ipScanner.scan("192.168.1.1","192.168.1.255");
    }

}
