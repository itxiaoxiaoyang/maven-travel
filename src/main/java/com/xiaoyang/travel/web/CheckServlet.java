package com.xiaoyang.travel.web;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/19/0019 20:48
 * @description 判断验证码是否正确
 **/
@WebServlet(urlPatterns = "/checkServlet")
public class CheckServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String check = request.getParameter("check");
        String code = (String) request.getSession().getAttribute("code");

        Map<String, Object> result = new HashMap<>();
        result.put("codeFlag", true);

        if (!code.equalsIgnoreCase(check)) {
            //不相等 返回json数据 覆盖原数据
            result.put("codeFlag", false);
        }
        response.getWriter().println(JSON.toJSONString(result));
    }
}