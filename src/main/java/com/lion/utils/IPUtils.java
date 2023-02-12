package com.lion.utils;

import com.lion.formatter.Str2NumFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP地址处理
 */
public class IPUtils {

    private static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

    private IPUtils() {}

    /**
     * 获取IP地址
     */
    public static String fetchIp() {
        String ip;
        try {
            List<String> ipList = getHostAddress(null);
            // default the first
            ip = (!ipList.isEmpty()) ? ipList.get(0) : "";
        } catch (Exception ex) {
            ip = "";
            logger.warn("Utils get IP warn", ex);
        }
        return ip;
    }

    /**
     * 获取指定网卡的IP地址
     */
    public static String fetchIp(String interfaceName) {
        String ip;
        interfaceName = interfaceName.trim();
        try {
            List<String> ipList = getHostAddress(interfaceName);
            ip = (!ipList.isEmpty()) ? ipList.get(0) : "";
        } catch (Exception ex) {
            ip = "";
            logger.warn("Utils get IP warn", ex);
        }
        return ip;
    }

    /**
     * 校验是否IP地址
     */
    public static boolean isIP(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        String str = "(2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2}(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 校验是否公网IP
     */
    public static boolean isPubicNetwork(String s) {
        if (!isIP(s)) {
            return false;
        }
        if ("0.0.0.0".equals(s) || "1.1.1.1".equals(s)) {
            return false;
        }
        String[] ipArray = s.split(".");
        int a1 = Str2NumFormatter.toInt(ipArray[0]);
        // 10.0.0.0 ~ 10.255.255.255
        if (a1 == 10) {
            return false;
        }
        int a2 = Str2NumFormatter.toInt(ipArray[1]);
        // 172.16.0.0 ~ 172.31.255.255
        if (a1 == 172) {
            if (a2 >= 16 && a2 <= 31) {
                return false;
            }
        }
        // 192.168.0.0 ~ 192.168.255.255
        if (a1 == 192) {
            if (a2 == 168) {
                return false;
            }
        }
        return true;
    }

    /**
     * IP 转为 long 类型
     */
    public static Long convertIpToLong(String ip) {
        String[] ipArray = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String str: ipArray) {
            sb.append((str.length()<3) ? 0 + str : str);
        }
        return Long.parseLong(sb.toString());
    }

    /**
     * 从请求头中获取 IP 地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }

    /**
     * @param interfaceName 可指定网卡名称,null则获取全部
     * @return List<String>
     */
    private static List<String> getHostAddress(String interfaceName) throws SocketException {
        List<String> ipList = new ArrayList<String>(5);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> allAddress = ni.getInetAddresses();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                if (address.isLoopbackAddress()) {
                    // skip the loopback addr
                    continue;
                }
                if (address instanceof Inet6Address) {
                    // skip the IPv6 addr
                    continue;
                }
                String hostAddress = address.getHostAddress();
                if (null == interfaceName) {
                    ipList.add(hostAddress);
                } else if (interfaceName.equals(ni.getDisplayName())) {
                    ipList.add(hostAddress);
                }
            }
        }
        return ipList;
    }

}
