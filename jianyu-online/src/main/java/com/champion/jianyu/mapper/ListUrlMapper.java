package com.champion.jianyu.mapper;

import com.champion.jianyu.pojo.ListUrl;
import com.champion.jianyu.pojo.ListUrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ListUrlMapper {
    int countByExample(ListUrlExample example);

    int deleteByExample(ListUrlExample example);

    int deleteByPrimaryKey(String id);

    int insert(ListUrl record);

    int insertSelective(ListUrl record);

    List<ListUrl> selectByExample(ListUrlExample example);

    ListUrl selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ListUrl record, @Param("example") ListUrlExample example);

    int updateByExample(@Param("record") ListUrl record, @Param("example") ListUrlExample example);

    int updateByPrimaryKeySelective(ListUrl record);

    int updateByPrimaryKey(ListUrl record);
}