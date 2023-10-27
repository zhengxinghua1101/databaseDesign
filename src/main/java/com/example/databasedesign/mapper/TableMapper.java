package com.example.databasedesign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.databasedesign.vo.Table;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/27 7:56
 * @version: 1.0.0
 */
@Mapper
public interface TableMapper extends BaseMapper<Table> {


    @Select("select a.table_schema,a.engine ,a.table_name ,a.create_time,a.table_comment  ,b.column_name ,b.column_comment ,b.column_type from information_schema.tables a  left join information_schema.columns b on a.table_name = b.table_name  where a.table_schema = #{tableSchema} order by a.table_name")
    List<Table> selectTableByDataBaseName(String tableSchema);

}
