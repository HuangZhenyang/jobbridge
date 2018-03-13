package com.galigaigai.jobbridge.util;


import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * Created by Administrator on 2017/7/25 0025.
 */
public class MailUtil {
    // Spring的邮件工具类，实现了MailSender和JavaMailSender接口
    private JavaMailSenderImpl mailSender;
    private Properties p;
    private int timeout = 25000;
    private String url = "http://localhost:8080/student/confirm";
    private String subject = "JobBridge学生账号验证";
    private StringBuilder content;
    public MailUtil(){
        mailSender = new JavaMailSenderImpl();
        p = new Properties();
        content = new StringBuilder();
        initMail(mailSender,p);
    }

    private void initMail(JavaMailSenderImpl mailSender, Properties p){
        // 设置参数
        mailSender.setHost("smtp.163.com");
        mailSender.setUsername("jobbridge@163.com");
        mailSender.setPassword("churenceo123");
        //设置property
        p.setProperty("mail.smtp.timeout",timeout+"");
        p.setProperty("mail.smtp.auth","true");
        mailSender.setJavaMailProperties(p);
        //设置内容格式
        content.append("<body>");
        content.append("<span style=\"font-size:30px\">Verification Email</span>");
        content.append("<hr>");
        content.append("<p><span style=\"font-size:20px\">Dear student:<br>");
        content.append("Thanks for using our platform, please click the website to verify your account</span></p>\n");
        content.append("<a href=\"");
        content.append(url);
        content.append("\"><span style=\"font-size:25px\">Verification Website</span></a><br>");
        content.append("<span style=\"font-size:20px\">Thanks</span></body>");
    }

    /**
     *
     * @param recipient 收件人
     *
     *
     */

    public void Send(String recipient) throws MessagingException {
        // 构建简单邮件对象，见名知意
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg,true);
        // 设定邮件参数
        helper.setFrom(mailSender.getUsername());
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content.toString(),true);
        // 发送邮件
        mailSender.send(msg);
    }

}
