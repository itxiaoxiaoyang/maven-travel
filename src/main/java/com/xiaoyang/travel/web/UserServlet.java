package com.xiaoyang.travel.web;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.model.ResultInfo;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.UserService;
import com.xiaoyang.travel.service.impl.UserServiceImpl;
import com.xiaoyang.travel.utils.MailUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/19/0019 22:22
 * @description TODO
 **/
@WebServlet(urlPatterns = "/userServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    /**
     * 注册模块
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        Map<String, Object> checkMap = new HashMap<>();
        try {
            BeanUtils.populate(user, map);
            //registFlag:   true--成功  false--失败
            boolean checkFlag = userService.register(user);

            //注册成功-给用户发送激活邮件
            if (checkFlag) {
                //给用户发送激活邮件
                MailUtil.sendEmail(user.getEmail(),
                        "<h1>恭喜您，注册成功！</h1><a href='http://localhost:8080/userServlet?methodName=active&code=" + user.getCode() + "'>请点击此链接，进行激活</a>");
            }


            checkMap.put("checkFlag", checkFlag);
        } catch (Exception e) {
            //出异常返回false数据
            e.printStackTrace();
            checkMap.put("checkFlag", false);
            checkMap.put("msg", "注册出现异常，请联系管理员！");
        }

        //3. 响应数据 json   {name:value} -- map<key,value>
        response.getWriter().println(JSON.toJSONString(checkMap));
    }

    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        boolean flag = userService.checkEmail(email);

        response.getWriter().println(flag);
    }

    /**
     * 邮箱激活
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //处理邮件激活业务
        //1.获取请求数据
        //激活码
        String code = request.getParameter("code");
        //2.处理数据
        boolean activeFlag = userService.emailActive(code);
        //3.响应数据
        if (activeFlag) {
            //激活成功  重定向到登录页面
            response.getWriter().println("邮件激活成功，即将跳转到登录页面");
            response.setHeader("refresh", "3;/login.html");
        } else {
            //激活失败，响应失败信息
            response.getWriter().println("邮件激活失败，3秒中之后将跳转到注册页面，请重新注册");
            response.setHeader("refresh", "3;/register.html");
        }
    }

    /**
     * 用于校验验证码是否正确
     * 鼠标离焦事件
     */
    private void checkCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String check = request.getParameter("code");
        String code = (String) request.getSession().getAttribute("code");

        Map<String, Object> result = new HashMap<>();
        result.put("codeFlag", true);

        if (!code.equalsIgnoreCase(check)) {
            //不相等 返回json数据 覆盖原数据
            result.put("codeFlag", false);
        }
        response.getWriter().println(JSON.toJSONString(result));
    }

    /**
     * 判断登录数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //处理数据
        User user = new User();
        ResultInfo info = new ResultInfo();
        try {
            BeanUtils.populate(user, map);
            boolean loginFlag = userService.checkEmailAndPwd(user);
            if (loginFlag) {
                //数据匹配成功
                info.setFlag(true);
                //将数据库信息全部存到session中
                user = userService.addUser(user.getEmail());
                //登录成功 将用户数据存储到session中
                request.getSession().setAttribute("user", user);
            } else {
                //数据不匹配
                info.setFlag(false);
                info.setErrorMsg("用户与密码不匹配!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //数据不匹配
            info.setFlag(false);
            info.setErrorMsg("账户出问题,请和管理员联系!!!");
        }
        //将数据返回浏览器
        response.getWriter().println(JSON.toJSONString(info));
    }

    /**
     * 校验邮箱是否激活
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void activeEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取数据
        String email = request.getParameter("email");
        //处理数据
        boolean checkFlag = userService.isActiveEmail(email);
        ResultInfo info = new ResultInfo();
        if (checkFlag) {
            //邮箱已激活
            info.setFlag(true);
        } else {
            //邮箱未激活
            info.setErrorMsg("邮箱未激活!");
            info.setFlag(false);
        }
        //响应数据
        response.getWriter().println(JSON.toJSONString(info));
    }

    /**
     * 判断用户是否登录 将数据显示在页面上
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void getSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        //处理数据
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();
        if (null != user) {
            //有数据
            info.setFlag(true);
            info.setData(user.getName());
        } else {
            info.setFlag(false);
        }
        response.getWriter().println(JSON.toJSONString(info));
    }

    /**
     * 退出注销操作
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            request.getSession().invalidate();
        }

        //响应数据：跳转到登录页面 --  转发，重定向
        response.sendRedirect("/login.html");

    }

}