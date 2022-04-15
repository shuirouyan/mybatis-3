package org.apache.ibatis.custom.test;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.custom.domain.Person;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ck163
 * @date 2022-04-15 09:55:45
 */
public class SqlNodeTest {
    @Test
    public void method01() {
        Reader resourceAsReader = null;
        try {
            resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsReader);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            Configuration configuration = sqlSession.getConfiguration();
            StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode("select * from person where 1=1");
            SqlNode sqlNode1 = new IfSqlNode(new StaticTextSqlNode("and id=#{id}"), "id != null");
            SqlNode sqlNode2 = new IfSqlNode(new StaticTextSqlNode("and name=#{name}"), "name != null");
            List<SqlNode> sqlNodes = Arrays.asList(staticTextSqlNode, sqlNode1, sqlNode2);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(sqlNodes);
            TextSqlNode textSqlNode = new TextSqlNode("");

            Map<String, Object> map = new HashMap<>(2);
            map.put("id", 2);
            map.put("name", "赵敏");
            DynamicContext context = new DynamicContext(configuration, map);
            // RawSqlSource rawSqlSource = new RawSqlSource(configuration, mixedSqlNode, Person.class);
            mixedSqlNode.apply(context);
            String sql = context.getSql();
            System.out.printf("%s\n", sql);
            Map<String, Object> bindings = context.getBindings();
            System.out.printf("%s\n", bindings);

            SqlSource parse = new SqlSourceBuilder(configuration).parse(context.getSql(), Person.class, bindings);
            BoundSql boundSql = parse.getBoundSql(Person.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                parse.getBoundSql(entry.getKey()).setAdditionalParameter(entry.getKey(), entry.getValue());
            }
            String resultSql = boundSql.getSql();
            System.out.printf("%s\n", resultSql);
            System.out.printf("%s\n", map);
            List<Object> objects = sqlSession.selectList(boundSql.getSql(), map);
            //System.out.println("objects = " + objects);
            PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement(boundSql.getSql());
            preparedStatement.setInt(1, (Integer) map.get("id"));
            preparedStatement.setString(2, (String) map.get("name"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    String string = resultSet.getString(i);
                    System.out.printf("%s\t", string);
                }
                System.out.println();
            }
            //ParameterHandler parameterHandler = new DefaultParameterHandler(configuration.getMappedStatement(""), map, boundSql);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
