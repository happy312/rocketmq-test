package com.zxy.bank2.mapper;

import com.zxy.bank2.model.AccountInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountInfoMapper {
    @Update("update account_info set balance = balance + #{money} where id = #{id}")
    void updateAccountInfo(@Param("id") String id, @Param("money") String money);

    @Select("select count(*) from de_duplication where tax_no = #{taxNo}")
    Integer getByTaxNo(@Param("taxNo") String taxNo);

    @Insert("insert into de_duplication (tax_no, create_time) values (#{taxNo}, now())")
    void saveLog(@Param("taxNo") String taxNo);

    @Insert("insert into account_info (id, name, balance) values (#{id}, #{name}, #{balance})")
    Integer saveAccountInfo(@Param("id") String id, @Param("name") String name, @Param("balance") String balance);

    @Select("select * from account_info where id = #{id} ")
    AccountInfo getById(@Param("id") String id);
}
