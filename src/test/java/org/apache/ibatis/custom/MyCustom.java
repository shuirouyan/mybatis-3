package org.apache.ibatis.custom;

import org.apache.ibatis.domain.blog.Author;
import org.apache.ibatis.domain.blog.Blog;
import org.apache.ibatis.domain.blog.Post;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.submitted.results_id.AnotherMapper;
import org.apache.ibatis.submitted.results_id.User;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * @author ck163
 * @date 2022-03-31 16:14:15
 */
public class MyCustom {


    @Test
    public void method01() {
        List<Blog> blogs = new ArrayList<Blog>() {
            {
                add(new Blog(1, "tile-1", new Author(1), new ArrayList<Post>() {{
                    Post post = new Post();
                    post.setId(3);
                    post.setCreatedOn(new Date());
//                     Date from = Date.from(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).toInstant(ZoneOffset.of("+8")));
//                    Date from = Date.from(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).toInstant(ZoneOffset.UTC));
//                    Date from = Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant());
//                    System.out.println("from = " + from);
                    post.setSubject("subject-1");
                    add(post);
                }}));
                add(new Blog(2, "tile-2", new Author(2), new ArrayList<Post>() {{
                }}));
            }
        };
        blogs.forEach(System.out::println);

    }

    @Test
    public void method02() {
        Post post = new Post();
        post.setId(1);
        post.setSubject("subject");
        post.setBody("body information.");
        System.out.printf("%s\n", post);
        MetaObject metaObject = SystemMetaObject.forObject(post);
        String[] getterNames = metaObject.getGetterNames();
        System.out.printf("%s\n", Arrays.toString(getterNames));
        Object value = metaObject.getValue("subject");
        System.out.printf("%s\n", value);
    }

    @Test
    public void method03() {
        DefaultObjectFactory objectFactory = new DefaultObjectFactory();
        List list = objectFactory.create(List.class);
        Map map = objectFactory.create(Map.class);
        list.addAll(Arrays.asList(1, 2, 3, 4));
        map.put("test", "test");
        System.out.printf("%s\n", list);
        System.out.printf("%s\n", map);
    }

    @Test
    public void method04() {
        try {
            Properties properties = Resources.getResourceAsProperties("org/apache/ibatis/custom/resourceprop/blog-derby.properties");
            Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
            /*ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(Resources.getResourceAsReader("org/apache/ibatis/custom/resourceprop/blog-derby-schema.sql"));
            scriptRunner.runScript(Resources.getResourceAsReader("org/apache/ibatis/custom/resourceprop/blog-derby-dataload.sql"));
            System.out.printf("%s\n", connection.toString());*/
            SqlRunner sqlRunner = new SqlRunner(connection);
            List<Map<String, Object>> list = sqlRunner.selectAll("select * from node");
            list.forEach(System.out::println);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method05() {
        try {
            Reader resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/submitted/results_id/CreateDB.sql");
            InputStream resource = Resources.getResourceAsStream("org/apache/ibatis/submitted/results_id/mybatis-config.xml");
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
            Configuration configuration = build.getConfiguration();

            Environment environment = configuration.getEnvironment();
            Connection connection = environment.getDataSource().getConnection();
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(resourceAsReader);
            System.out.println("------");
            SqlSession sqlSession = build.openSession();
            AnotherMapper mapper = sqlSession.getMapper(AnotherMapper.class);
            User user = mapper.getUser(2);
            System.out.println("users = " + user.getName() + "\tid:" + user.getId());
            sqlSession.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method06() {
        try {
            Reader resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/submitted/results_id/CreateDB.sql");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
