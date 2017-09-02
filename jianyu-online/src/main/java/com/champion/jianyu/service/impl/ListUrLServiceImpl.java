package com.champion.jianyu.service.impl;

import com.champion.jianyu.mapper.ListUrlMapper;
import com.champion.jianyu.pojo.ListUrl;
import com.champion.jianyu.pojo.ListUrlExample;
import com.champion.jianyu.service.ListUrlService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/14.
 */
@Service
public class ListUrLServiceImpl implements ListUrlService {

    @Autowired
    private ListUrlMapper listUrlMapper;

    @Override
    public int insert(ListUrl listUrl) {
        return listUrlMapper.insert(listUrl);
    }

    @Override
    public List<ListUrl> queryListUrl() {
        ListUrlExample example =new ListUrlExample();
        PageHelper.startPage(1, 10000);
        ListUrlExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);
        return listUrlMapper.selectByExample(example);
    }

    @Override
    public int update(ListUrl listUrl) {
        ListUrlExample example=new ListUrlExample();
        ListUrlExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(listUrl.getId());
        return listUrlMapper.updateByExample(listUrl,example);
    }
}
