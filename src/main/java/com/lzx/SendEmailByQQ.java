package com.lzx;

import com.alibaba.fastjson.JSON;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author 1Zx.
 * @data 2019/11/21 10:48
 */
public class SendEmailByQQ implements Runnable {

    private String content;

    private String authorizationCode;

    private String protocol;

    private String host;

    private String auth;

    private Integer port;

    private String sslEnable;

    private String debug;

    private String fromEmail;

    private String receiveEmail;

    @Override
    public void run() {
        Properties properties = new
                Properties();
        properties.put("mail.transport.protocol",protocol);
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port",port);
        properties.put("mail.smtp.auth",auth);
        properties.put("mail.smtp.ssl.enable",sslEnable);
        properties.put("mail.debug",debug);
        Session session = Session.getInstance(properties);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmail));
            message.setSubject("jxf");
            message.setText(content);
            Transport transport = session.getTransport();
            transport.connect(receiveEmail,authorizationCode);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            System.out.println("errCode" + -1);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSslEnable() {
        return sslEnable;
    }

    public void setSslEnable(String sslEnable) {
        this.sslEnable = sslEnable;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
