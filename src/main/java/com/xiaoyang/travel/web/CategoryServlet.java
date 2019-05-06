package com.xiaoyang.travel.web;

import com.xiaoyang.travel.service.CategoryService;
import com.xiaoyang.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/20/0020 23:38
 * @description TODO
 **/
@WebServlet(urlPatterns = "/categoryServlet")
public class CategoryServlet extends BaseServlet {

    private CategoryService categoryService = new CategoryServiceImpl();

    /**
     * 查询导航栏信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void queryAllCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //json数据
        String categoryList = categoryService.queryAll();
        response.getWriter().println(categoryList);
    }

    /**
     * 黑马精选主题旅游
     * 查询出符合要求的旅游线路 4条
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void routeCareChoose(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取数据
        //处理数据
        //查询出了所有符合的线路 返回值 json数据
        String routeCareChoose = categoryService.routeCareChoose();
        //响应数据
        response.getWriter().println(routeCareChoose);
    }
}