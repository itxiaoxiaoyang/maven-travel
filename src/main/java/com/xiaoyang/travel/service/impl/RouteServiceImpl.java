package com.xiaoyang.travel.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.dao.RouteDao;
import com.xiaoyang.travel.dao.impl.RouteDaoImpl;
import com.xiaoyang.travel.model.Favorite;
import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.model.RouteImg;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.RouteService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/21/0021 21:12
 * @description TODO
 **/
public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();


    /**
     * 处理分页展示数据业务
     *
     * @param strPageNum
     * @param strPageSize
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public String pageQuery(String strPageNum, String strPageSize, String cid, String rname) {
        //设置默认的当前页面和页面展示数据个数
        //当前页面
        int pageNum = 1;
        //每页展示数据个数
        int pageSize = 8;
        //查询的起始位置
        int pageStart = 0;
        //定义数据总页数
        int totalPage = 1;
        //定义数据总条数
        int totalCount = 0;

        //工具类:判断数据是否为空或null
        if (StringUtils.isNotBlank(strPageNum)) {
            pageNum = Integer.parseInt(strPageNum);
        }
        if (StringUtils.isNotBlank(strPageSize)) {
            pageSize = Integer.parseInt(strPageSize);
        }

        //计算出页面起始位置
        pageStart = (pageNum - 1) * pageSize;

        List<Route> routeList = routeDao.routeQuery(pageStart, pageSize,cid,rname);

        //数据总条数
        totalCount = routeDao.totalCount(cid,rname);
        //数据总页数 向上取整Math.ceil()
        totalPage = (int) Math.ceil((totalCount * 1.0) / pageSize);

        //计算上一页和下一页
        int prePage = pageNum <= 1 ? 1 : (pageNum - 1);
        int nextPage = pageNum >= totalPage ? totalPage : (pageNum + 1);

        //创建map集合存储键值对数据
        Map<String, Object> map = new HashMap<>();
        map.put("routeList",routeList);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("pageStart", pageStart);
        map.put("totalPage", totalPage);
        map.put("totalCount", totalCount);
        map.put("prePage", prePage);
        map.put("nextPage", nextPage);

        return JSON.toJSONString(map);
    }


    /**
     * 处理路线详细信息的业务
     * @param rid
     * @return
     */
    @Override
    public String queryDetail(String rid) {
        //查询多表获取的线路详细信息
        Map<String,Object> map = routeDao.queryDetail(rid);
        //根据rid查询出详细路线中的大小图片
        List<RouteImg> routeImgList = routeDao.queryDetailImg(rid);
        map.put("routeImgList", routeImgList);
        return JSON.toJSONString(map);
    }

    /**
     * 插件模糊查询搜索框
     * @param rname
     * @return
     */
    @Override
    public String queryByKey(String rname) {
        List<String> list = routeDao.queryByKey(rname);

        return JSON.toJSONString(list);
    }
}
