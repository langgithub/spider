package com.champion.jianyu.service;

import com.champion.jianyu.pojo.ListUrl;

import java.util.List;

/**
 * Created by root on 2017/8/14.
 */
public interface ListUrlService {
    int  insert(ListUrl listUrl);
    List<ListUrl> queryListUrl();
    int update(ListUrl listUrl);
}
