package org.apache.ibatis.custom.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.custom.domain.Book;

import java.util.Date;
import java.util.List;

/**
 * @author ck163
 * @date 2022-04-07 13:53:35
 */
public interface BookMapper {
    /**
     * find book
     *
     * @return book
     */
    Book findOne(Integer id);

    int save(@Param("id") Integer id, @Param("isbn") String isbn, @Param("bookName") String bookName, @Param("bookTotal") Integer bookTotal,
             @Param("bookDescription") String bookDescription, @Param("createTime") Date createTime, @Param("updateTime") Date updateTime);

    List<Book> findAllByIsbn(String isbn);
}
