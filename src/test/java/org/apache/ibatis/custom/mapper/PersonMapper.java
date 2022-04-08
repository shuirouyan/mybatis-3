package org.apache.ibatis.custom.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.custom.domain.Person;

import java.util.Date;
import java.util.List;

/**
 * @author ck163
 * @date 2022-04-06 17:31:10
 */
public interface PersonMapper {
    /**
     * 注释信息
     *
     * @return entity
     */
    Person findById(Integer id);

    /**
     * save
     *
     * @param id         id
     * @param name       name
     * @param nickName   nickName
     * @param createTime createTime
     * @param age        age
     * @param phone      phone
     * @return 受影响的行数
     */
    int save(@Param("id") Integer id, @Param("name") String name, @Param("nickName") String nickName,
             @Param("createTime") Date createTime, @Param("age") Integer age, @Param("phone") String phone);

    /**
     * person list by id
     *
     * @param id id
     * @return list
     */
    List<Person> getPersonListById(@Param("id") Integer id);

    /**
     * person list
     *
     * @param id id
     * @return list
     */
    List<Person> getPersonListById2(@Param("id") Integer id);
}
