package com.champion.jianyu.service.impl;

import com.champion.jianyu.mapper.ContentMapper;
import com.champion.jianyu.pojo.Content;
import com.champion.jianyu.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 2017/8/16.
 */
@Service
public class ContentServiceImpl implements ContentService{

    @Autowired
    private ContentMapper contentMapper;
    @Override
    public int insert(Content content) {
        return contentMapper.insert(content);
    }
}
