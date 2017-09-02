package com.champion.jianyu.service;

import com.champion.jianyu.pojo.ListUrlCondition;

import java.util.List;

/**
 * Created by root on 2017/8/14.
 */
public interface ListUrlConditionService {
    int insert(ListUrlCondition listUrlCondition);
    List<ListUrlCondition> queryUrlCondition();
    int update(ListUrlCondition listUrlCondition);
}
