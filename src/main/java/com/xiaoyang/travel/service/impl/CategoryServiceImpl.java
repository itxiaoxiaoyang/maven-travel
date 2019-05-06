package com.xiaoyang.travel.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.dao.CategoryDao;
import com.xiaoyang.travel.dao.impl.CategoryDaoImpl;
import com.xiaoyang.travel.model.Category;
import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.service.CategoryService;
import com.xiaoyang.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/20/0020 23:58
 * @description TODO
 **/
public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();


    /**
     * 1. header.html页面加载完成后，发送异步请求查询类别列表；
     * 2. CategoryServlet类处理请求和响应，具体业务交由CategoryService处理；
     * 3. CategoryService中，接收请求后：从redis中获取类别数据：
     * 1. 如果redis中有数据：直接将数据返回；
     * 2. 如果redis中没有数据：
     * 1. 从数据库中查询类别列表数据，并将其转化成json字符串；
     * 2. 将json字符串存储到redis中，然后返回给web层；
     * 4. header.html页面获取到类别列表后，将其展示到导航栏位置；
     *
     * @return
     */
    @Override
    public String queryAll() {
        //到jedis中查找数据
        Jedis jedis = JedisUtils.getJedis();
        String categoryListString = jedis.get("categojryListString");
        if (categoryListString == null) {
            //说明缓存中没有数据 从数据库中查找
            List<Category> categoryList = categoryDao.queryAll();
            //json数据
            categoryListString = JSON.toJSONString(categoryList);
            //在缓存中存储数据
            jedis.set("categoryListString", categoryListString);
        }

        return categoryListString;

    }

    /**
     * 处理查询出的主题旅游线路模块
     * @return
     */
    @Override
    public String routeCareChoose() {
        //人气旅游
        List<Route> popularityList = categoryDao.popularity();
        //最新旅游
        List<Route> newestList = categoryDao.newest();
        //主题旅游
        List<Route> themeList = categoryDao.theme();

        //将数据封装到map集合中
        Map<String, List<Route>> categoryMap = new HashMap<>();
        categoryMap.put("popularityList",popularityList);
        categoryMap.put("newestList",newestList);
        categoryMap.put("themeList",themeList);

        //转换成json数据格式返回
        return JSON.toJSONString(categoryMap);
    }
}
