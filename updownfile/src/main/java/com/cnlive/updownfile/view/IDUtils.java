package com.cnlive.updownfile.view;

public class IDUtils {
    private static byte[] lock = new byte[0];  
  
    // 位数，默认是8位  
    private final static long w = 100000000;  
  
    public static String createID() {  
        long r = 0;  
        synchronized (lock) {  
            r = (long) ((Math.random() + 1) * w);  
        }  
  
        return System.currentTimeMillis() + String.valueOf(r).substring(1);  
    }  
}  