package com.champion.jianyu.service.impl;

import com.champion.jianyu.mapper.ListUrlConditionMapper;
import com.champion.jianyu.pojo.ListUrlCondition;
import com.champion.jianyu.pojo.ListUrlConditionExample;
import com.champion.jianyu.service.ListUrlConditionService;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/8/14.
 */
@Service
public class ListUrlConditionServiceImpl implements ListUrlConditionService {

    private static Logger logger = Logger.getLogger(ListUrlConditionServiceImpl.class);

    @Autowired
    private ListUrlConditionMapper listUrlConditionMapper;

    @Override
    public int insert(ListUrlCondition listUrlCondition) {
        return listUrlConditionMapper.insert(listUrlCondition);
    }

    @Override
    public List<ListUrlCondition> queryUrlCondition() {
        ListUrlConditionExample example=new ListUrlConditionExample();
        PageHelper.startPage(1, 10000);
        ListUrlConditionExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);
        return listUrlConditionMapper.selectByExample(example);
    }

    @Override
    public int update(ListUrlCondition listUrlCondition) {
        ListUrlConditionExample example=new ListUrlConditionExample();
        ListUrlConditionExample.Criteria criteria = example.createCriteria();
        criteria.andAreaEqualTo(listUrlCondition.getArea())
                .andPublishtimeEqualTo(listUrlCondition.getPublishtime())
                .andSubtypeEqualTo(listUrlCondition.getSubtype())
                .andKeywordEqualTo(listUrlCondition.getKeyword());
        return listUrlConditionMapper.updateByExample(listUrlCondition,example);
    }
}
