package com.champion.jianyu.mapper;

import com.champion.jianyu.pojo.ListUrlCondition;
import com.champion.jianyu.pojo.ListUrlConditionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ListUrlConditionMapper {
    int countByExample(ListUrlConditionExample example);

    int deleteByExample(ListUrlConditionExample example);

    int insert(ListUrlCondition record);

    int insertSelective(ListUrlCondition record);

    List<ListUrlCondition> selectByExample(ListUrlConditionExample example);

    int updateByExampleSelective(@Param("record") ListUrlCondition record, @Param("example") ListUrlConditionExample example);

    int updateByExample(@Param("record") ListUrlCondition record, @Param("example") ListUrlConditionExample example);
}