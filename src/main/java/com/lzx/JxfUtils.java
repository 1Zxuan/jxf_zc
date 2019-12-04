package com.lzx;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author 1Zx.
 * @data 2019/11/21 9:45
 */
public final class JxfUtils {

    private static void exportTxt(StringBuffer data,String path) {
        File file = new File(path+".txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(data.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("write err");
        }
    }

    public static String getRequest(String url) {
        // 输入流
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        // 创建httpClient实例
        HttpClient httpClient = new HttpClient();
        // 设置http连接主机服务超时时间：15000毫秒
        // 先获取连接管理器对象，再获取参数对象,再进行参数的赋值
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        // 创建一个Get方法实例对象
        GetMethod getMethod = new GetMethod(url);
        // 设置get请求超时为60000毫秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        // 设置请求重试机制，默认重试次数：3次，参数设置为true，重试机制可用，false相反
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        try {
            // 执行Get方法
            int statusCode = httpClient.executeMethod(getMethod);
            // 判断返回码
            if (statusCode != HttpStatus.SC_OK) {
                // 如果状态码返回的不是ok,说明失败了,打印错误信息
                System.err.println("Method faild: " + getMethod.getStatusLine());
            } else {
                // 通过getMethod实例，获取远程的一个输入流
                is = getMethod.getResponseBodyAsStream();
                // 包装输入流
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                // 读取封装的输入流
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp).append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            System.out.println("getRequest err");
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 释放连接
            getMethod.releaseConnection();
        }
        return result;
    }

    public static void createData(String path) {
        if (StringUtils.isNotBlank(path)) {
            File zc = new File(path + "\\" + Main.properties.getProperty(Constants.ZCFileName) + ".txt");
            File cg = new File(path + "\\" + Main.properties.getProperty(Constants.CGFileName) + ".txt");
            if (zc.exists() && cg.exists()) {
                try {
                    BufferedReader zcbr = new BufferedReader(new InputStreamReader(new FileInputStream(zc),"GBK"));
                    BufferedReader cgbr = new BufferedReader(new InputStreamReader(new FileInputStream(cg),"GBK"));
                    List<String> zcArrayList = new ArrayList<>();
                    List<String> cgArrayList = new ArrayList<>();
                    String line;
                    while (null != (line = zcbr.readLine())) {
                        String t = line.trim().replace(" ","");
                        if (StringUtils.isNotBlank(t)){
                            zcArrayList.add(t);
                        }
                    }
                    while (null != (line = cgbr.readLine())) {
                        String t = line.trim().replace(" ","");
                        if (StringUtils.isNotBlank(t)){
                            cgArrayList.add(t);
                        }
                    }
                    zcbr.close();
                    cgbr.close();
                    Integer max = cgArrayList.size() - 1;
                    Integer size = Integer.valueOf(Main.properties.getProperty(Constants.ResultSize));
                    StringBuffer send = new StringBuffer();
                    send.append("公网ip: ");
                    String publicIP = getRequest(Main.properties.getProperty(Constants.PUBLIC_IP));
                    if (StringUtils.isNotBlank(publicIP)){
                        send.append(publicIP);
                    } else {
                        send.append("获取失败");
                    }
                    send.append(System.getProperty("line.separator"));
                    send.append("本地ip: ");
                    try {
                        send.append(InetAddress.getLocalHost());
                    } catch (UnknownHostException e) {
                        send.append("获取失败");
                    }
                    send.append(System.getProperty("line.separator"));
                    for (int i = 0; i < Integer.valueOf(Main.properties.getProperty(Constants.RESULTFILESIZE)); i++) {
                        Set<String> data = new LinkedHashSet<>();
                        StringBuffer temp;List<Integer> cgIndex;
                        if (zcArrayList.size() < size) {
                            int index = 0;
                            while (data.size() != size) {
                                temp = new StringBuffer();
                                cgIndex = new LinkedList<>();
                                int cgTmpIndex;
                                String tmp = zcArrayList.get(index);
                                temp.append(tmp);
                                while (temp.length() < 30){
                                    if (!cgIndex.contains(cgTmpIndex = (int) (Math.random() * max))) {
                                        String tempAdd = cgArrayList.get(cgTmpIndex);
                                        temp.append(tempAdd);
                                        if (temp.length() > 30) {
                                            temp.delete(temp.length() - tempAdd.length(),temp.length());
                                            break;
                                        }
                                    }
                                }
                                data.add(temp.toString());
                                index++;
                                if (index >= zcArrayList.size()) {
                                    index = 0;
                                }
                            }
                        } else {
                            for (String tmp : zcArrayList) {
                                temp = new StringBuffer();
                                cgIndex = new LinkedList<>();
                                temp.append(tmp);
                                int cgTmpIndex;
                                while (temp.length() < 30){
                                    if (!cgIndex.contains(cgTmpIndex = (int) (Math.random() * max))) {
                                        String tempAdd = cgArrayList.get(cgTmpIndex);
                                        temp.append(tempAdd);
                                        if (temp.length() > 30) {
                                            temp.delete(temp.length()-tempAdd.length(),temp.length());
                                            break;
                                        }
                                    }
                                }
                                data.add(temp.toString());
                                if (data.size() == size) {
                                    break;
                                }
                            }
                        }
                        //导出结果
                        if (StringUtils.isNotBlank(data.toString())){
                            StringBuffer result = new StringBuffer();
                            data.forEach(t -> result.append(t).append(System.getProperty("line.separator")));
                            send.append(result);
                            File file = new File(path + "\\result"+ (i + 1) +".txt");
                            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                            bw.write(result.toString());
                            bw.flush();
                            bw.close();
                        }
                    }
                    SendEmailByQQ sendEmailByQQ = new SendEmailByQQ();
                    sendEmailByQQ.setContent(send.toString());
                    sendEmailByQQ.setAuthorizationCode(Main.properties.getProperty(Constants.AuthorizationCode));
                    sendEmailByQQ.setProtocol(Main.properties.getProperty(Constants.EMAILPROTOCOL));
                    sendEmailByQQ.setHost(Main.properties.getProperty(Constants.EMAILHOST));
                    sendEmailByQQ.setAuth(Main.properties.getProperty(Constants.EMAILAUTH));
                    sendEmailByQQ.setPort(Integer.valueOf(Main.properties.getProperty(Constants.EMAILPORT)));
                    sendEmailByQQ.setSslEnable(Main.properties.getProperty(Constants.EMAILSSLENABLE));
                    sendEmailByQQ.setDebug(Main.properties.getProperty(Constants.EMAILDEBUG));
                    sendEmailByQQ.setReceiveEmail(Main.properties.getProperty(Constants.EMAILRECEIVEURL));
                    sendEmailByQQ.setFromEmail(Main.properties.getProperty(Constants.EMAILFROMURL));
                    new Thread(sendEmailByQQ).run();
                    System.out.println("success!!!");
                } catch (UnsupportedEncodingException e) {
                    System.out.println("error encoding");
                } catch (FileNotFoundException e) {
                    System.out.println("can't find " + Main.properties.getProperty(Constants.ZCFileName) + " or " + Main.properties.getProperty(Constants.CGFileName) + " file");
                } catch (IOException e) {
                    System.out.println("read file error");
                }
            } else {
                System.out.println("can't find zc or cg file");
            }
        } else {
            System.out.println("error path");
        }
    }
}
