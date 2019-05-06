package com.xiaoyang.travel.web;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.UserService;
import com.xiaoyang.travel.service.impl.UserServiceImpl;
import com.xiaoyang.travel.utils.UuidUtil;
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
 * @date 2019/3/19/0019 21:23
 * @description 处理表单注册页面
 **/
@WebServlet(urlPatterns = "/registServlet")
public class RegistServlet extends HttpServlet {
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
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        Map<String, Object> checkMap = new HashMap<>();
        try {
            BeanUtils.populate(user, map);
            UserService userService = new UserServiceImpl();
            boolean checkFlag = userService.register(user);
            //registFlag:   true--成功  false--失败
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
}