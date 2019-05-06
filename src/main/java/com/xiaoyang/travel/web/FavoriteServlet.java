package com.xiaoyang.travel.web;

import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.FavoriteService;
import com.xiaoyang.travel.service.impl.FavoriteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoyang
 * @version v1.0
 * @date 2019/3/24/0024 10:32
 * @description 处理收藏模块业务
 **/
@WebServlet(urlPatterns = "/favoriteServlet")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 通过rid和uid查询展示是否可收藏信息
     * @param request
     * @param response
     * @throws IOException
     */
    private void queryIsCollect(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //获取数据
        String rid = request.getParameter("rid");
        //处理数据
        //从session中回去用户信息 取得cid属性
        User user = (User) request.getSession().getAttribute("user");
        String collectFlag = favoriteService.queryIsCollect(rid,user);
        //响应数据
        response.getWriter().println(collectFlag);
    }

    /**
     * 添加收藏业务
     * @param request
     * @param response
     * @throws IOException
     */
    private void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1、接收请求数据
        String rid = request.getParameter("rid");
        User loginUser = (User) request.getSession().getAttribute("user");

        //2、处理数据：调用service处理添加收藏业务
        String addFavoriteJson = favoriteService.addFavorite(rid, loginUser);

        //3、响应数据
        response.getWriter().println(addFavoriteJson);
    }

    /**
     * 我的收藏路线
     * @param request
     * @param response
     * @throws IOException
     */
    private void pageQueryByMyFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //获取数据
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        //处理数据
        //获取用户
        User user = (User) request.getSession().getAttribute("user");
        String favoriteJson = favoriteService.pageQueryByMyFavorite(user,pageNum,pageSize);
        //返回数据
        response.getWriter().println(favoriteJson);
    }

    /**
     * 收藏排行榜列表
     * @param request
     * @param response
     * @throws IOException
     */
    private void pageQueryByRank(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //获取数据
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String startPrice = request.getParameter("startPrice");
        String endPrice = request.getParameter("endPrice");

        //处理数据
        //获取用户
        User user = (User) request.getSession().getAttribute("user");
        //获取我的收藏排行榜
//        String favoriteJson = favoriteService.pageQueryByRank(user,pageNum,pageSize,rname,startPrice,endPrice);
        //获取所有数据收藏排行榜
        String favoriteJson = favoriteService.pageQueryByRank(pageNum,pageSize,rname,startPrice,endPrice);

        //返回数据
        response.getWriter().println(favoriteJson);
    }
}