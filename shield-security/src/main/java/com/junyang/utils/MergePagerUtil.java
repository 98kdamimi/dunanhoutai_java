package com.junyang.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lizhen
 * 多条分页合并工具
 */
public class MergePagerUtil {
    /**
     * 同一个方法中多次执行startpage后，将数据合并返回
     *
     * @param data       多条查询合并后的数据
     * @param pageNumber 第几页
     * @param pages      多条查询分页时的page
     * @param <T>
     * @return
     */
    public static <T> PageInfo<T> pageInfo(List<T> data, Integer pageNumber,Integer oldPageSize, List<Page<T>> pages) {
        PageInfo<T> info = new PageInfo<>(data);
        info.setPageNum(pageNumber);
        info.setSize(data.size());
        info.setEndRow(data.size() - 1);
        int pageSize =0;
        int total = 0;
        int pagesNum =0;//所有pages中取最大的那个
        for (Page<T> page : pages) {
            pageSize+= page.getPageSize();
            total += page.getTotal();
            if (page.getPages()>pagesNum){
                pagesNum=page.getPages();
            }
            //log.info("每页大小"+page.getPageSize()+"total:"+page.getTotal()+"size:"+page.getPages());
        }
        if (pageSize<1){
            pageSize=oldPageSize;
        }
        info.setPageSize(pageSize);//pagesize是根据具体执行分页多少次
        info.setTotal(total);
        info.setPrePage(info.getPageNum()-1);//前一页
        info.setPages(pagesNum);//总页数
        info.setIsFirstPage(info.getPageNum() == 1);//是否是第一页
        info.setIsLastPage(info.getPages() == pageNumber);//是否是最后一页
        info.setHasPreviousPage(info.getPageNum() > 1);//是否有前一夜
        info.setHasNextPage(info.getPageNum() < info.getPages());//是否有后一页
        if (info.isHasNextPage()) {
            info.setNextPage(info.getPageNum() + 1);
        }
        info.setNavigateLastPage(info.getPages());
        return info;
    }
}
