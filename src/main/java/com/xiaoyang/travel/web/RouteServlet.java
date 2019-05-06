package com.xiaoyang.travel.web;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.RouteService;
import com.xiaoyang.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 线路servlet
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/21/0021 21:05
 * @description TODO
 **/
@WebServlet(urlPatterns = "/routeServlet")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();

    /**
     * 处理页面分页
     * @param request
     * @param response
     * @throws IOException
     */
   private void routePageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException{
       //获取页面数据 strPageNum:当前页 strPageSize:当前页显示的数据个数 cid 后期引入的条件
       String strPageNum = request.getParameter("pageNum");
       String strPageSize = request.getParameter("PageSize");
       String cid = request.getParameter("cid");
       //获取用于模糊查询url的数据
       String rname = request.getParameter("rname");

       //处理数据
       String jsonRoutePage = routeService.pageQuery(strPageNum,strPageSize,cid,rname);
       //响应数据
       response.getWriter().println(jsonRoutePage);
   }

    /**
     * 展示详细线路数据
     * @param request
     * @param response
     * @throws IOException
     */
   private void queryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String rid = request.getParameter("rid");
       //处理数据
       String detailJson = routeService.queryDetail(rid);
       //响应数据
       response.getWriter().println(detailJson);
   }

    /**
     * 根据关键字查询 插件查询
     * @param request
     * @param response
     * @throws IOException
     */
   private void queryByKey(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String rname = request.getParameter("term");

       String keyJson = routeService.queryByKey(rname);
       response.getWriter().println(keyJson);
   }
}