package com.xiaoyang.travel.web;

import com.xiaoyang.travel.service.UserService;
import com.xiaoyang.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/17/0017 23:34
 * @description TODO
 **/
@WebServlet(urlPatterns = "/checkEmailServlet")
public class CheckEmailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 此处代码无用 已被userServlet代码复用
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        UserService userService = new UserServiceImpl();
        boolean flag = userService.checkEmail(email);

        response.getWriter().println(flag);
    }
}