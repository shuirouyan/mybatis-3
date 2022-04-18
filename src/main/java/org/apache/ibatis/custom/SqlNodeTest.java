package org.apache.ibatis.custom;

import org.apache.ibatis.custom.domain.Book;
import org.apache.ibatis.custom.mappers.BookMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * @author ck163
 * @date 2022-04-15 09:54:45
 */
public class SqlNodeTest {
    public static void main(String[] args) {
        Reader resourceAsReader = null;
        try {
            resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsReader);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            Book book = new Book();
            book.setBookTotal(653);
            book.setIsbn("56321438");
//            List<Book> listByIdAndIsbn = mapper.findListByIdAndIsbn(book.getBookTotal(), book.getIsbn(), "mvc");
//            System.out.printf("%s\n", listByIdAndIsbn);
            String dataBaseTime = mapper.getDataBaseTime();
            System.out.println("dataBaseTime = " + dataBaseTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
