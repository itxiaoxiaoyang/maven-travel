package com.xiaoyang.travel.web;

import com.sun.org.apache.regexp.internal.RE;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/19/0019 22:22
 * @description TODO
 **/
@WebServlet(urlPatterns = "/baseServlet")
public class BaseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 请求分发思想
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取请求数据
        String methodName = request.getParameter("methodName");
        //2 处理数据
        try {
            //暴力获取
            //根据方法名调用方法：1、根据方法名动态获取方法；2、动态调用方法；  --- 反射
            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //2、动态调用方法；
            //取消权限检查
            method.setAccessible(true);
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //3 响应数据


    }

}