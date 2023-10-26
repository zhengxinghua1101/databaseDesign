package com.example.databasedesign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasedesign.vo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/26 15:44
 * @version: 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
