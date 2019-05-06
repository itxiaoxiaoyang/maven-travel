package com.xiaoyang.travel.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.ResourceBundle;

/**
 * @author buguniao
 * @version v1.0
 * @date 2019/3/19 16:17
 * @description 邮件发送的工具类
 **/
public class MailUtil {

    private static String host;
    private static String charset;
    private static String username;
    private static String password;


    /**
     * 静态代码块加载配置信息
     */
    static {

        ResourceBundle bundle = ResourceBundle.getBundle("mail");
        host = bundle.getString("mail.host");
        charset = bundle.getString("mail.charset");
        username = bundle.getString("mail.username");
        password = bundle.getString("mail.password");
    }


    /**
     * 发送邮件
     * @param emailTo
     * @param msg
     */
    public static void sendEmail(String emailTo,String msg) throws EmailException {

        //1、创建一个HtmlEmail对象
        HtmlEmail htmlEmail = new HtmlEmail();
        //2、设置邮箱服务器的参数
        htmlEmail.setHostName(host);
        //设置编码-
        htmlEmail.setCharset(charset);

        //3、设置邮箱验证（客户端授权）
        //username：邮箱账号
        //password：客户端授权码
        htmlEmail.setAuthentication(username,password);

        //4、设置收件人 和 发件人
        htmlEmail.setFrom(username, "【小杨旅游社】");

        htmlEmail.addTo(emailTo, "【注册用户】");

        //5、设置主题 和  正文
        htmlEmail.setSubject("【小杨旅游社注册激活邮件】");
        //网易
        //htmlEmail.setTextMsg(msg);

        //qq
        htmlEmail.setHtmlMsg(msg);

        //6、发送邮件
        htmlEmail.send();
    }














}
