package org.apache.ibatis.custom;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.custom.domain.Book;
import org.apache.ibatis.custom.domain.Person;
import org.apache.ibatis.custom.mapper.BookMapper;
import org.apache.ibatis.custom.mapper.PersonMapper;
import org.apache.ibatis.domain.blog.Author;
import org.apache.ibatis.domain.blog.Blog;
import org.apache.ibatis.domain.blog.Post;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.submitted.results_id.AnotherMapper;
import org.apache.ibatis.submitted.results_id.User;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    @Test
    public void method07() {
        Reader resourceAsReader;
        try {
            resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory build = sqlSessionFactoryBuilder.build(resourceAsReader);
            SqlSession sqlSession = build.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method08() {
        Reader resourceAsReader;
        try {
            resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/custom/resources/mybatis-config.xml");
            XMLConfigBuilder parser = new XMLConfigBuilder(resourceAsReader, null, null);
            Configuration configuration = parser.getConfiguration();
            System.out.printf("configuration = %s\n", configuration);
            Configuration parse = parser.parse();
            Class<? extends Log> logImpl = parse.getLogImpl();
            System.out.println("logImpl = " + logImpl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method09() {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            XPathParser xPathParser = new XPathParser(resourceAsStream);
            XNode xNode = xPathParser.evalNode("/configuration");
            System.out.printf("xNode = %s\n", xNode);
            XNode xNode1 = xNode.evalNode("settings");
            System.out.printf("xNode1 = %s\n", xNode1);
            String setting = xNode1.evalString("setting");
            System.out.println("setting = " + setting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method10() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config2.xml");
            Document document = documentBuilder.parse(inputStream);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList evaluate = (NodeList) xPath.evaluate("/users/*", document, XPathConstants.NODESET);
            List<Person> peopleList = new ArrayList<>();
            List<Person> peopleSyncList = Collections.synchronizedList(peopleList);
            BlockingQueue<Person> peopleBlockingQueue = new ArrayBlockingQueue<Person>(2);
            BlockingQueue<Person> linkedBlockingQueue = new LinkedBlockingQueue<>();
            for (int i = 1; i < evaluate.getLength() + 1; i++) {
                String path = "/users/user[" + i + "]";
                String id = (String) xPath.evaluate(path + "/@id", document, XPathConstants.STRING);
                System.out.println("id = " + id);
                String name = (String) xPath.evaluate(path + "/name", document, XPathConstants.STRING);
                String age = (String) xPath.evaluate(path + "/age", document, XPathConstants.STRING);
                String phone = (String) xPath.evaluate(path + "/phone", document, XPathConstants.STRING);
                String createTime = (String) xPath.evaluate(path + "/createTime", document, XPathConstants.STRING);
                Date createTimeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
                String nickName = (String) xPath.evaluate(path + "/nickName", document, XPathConstants.STRING);
                Person person = new Person(Integer.valueOf(id), name, nickName, createTimeNow, Integer.valueOf(age), phone, null);
                System.out.println("person = " + person);
                // add put
                // peopleBlockingQueue.offer(person,2, TimeUnit.SECONDS);
                peopleSyncList.add(person);
            }
            // take remove
            /*Person poll1 = peopleBlockingQueue.poll(2, TimeUnit.SECONDS);
            System.out.println("poll1 = " + poll1);
            Person poll2 = peopleBlockingQueue.poll(2, TimeUnit.SECONDS);
            System.out.println("poll2 = " + poll2);*/
            System.out.println("peopleSyncList = " + peopleSyncList);

        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method11() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config2.xml");
            LinkedBlockingQueue<Person> peopleLinkedBlockingQueue = new LinkedBlockingQueue<>();
            XPathParser xPathParser = new XPathParser(inputStream);
            List<XNode> xNodeList = xPathParser.evalNodes("/users/*");
            for (XNode xNode : xNodeList) {
                Person person = new Person();
                person.setId(xNode.getIntAttribute("id"));
                List<XNode> children = xNode.getChildren();
                for (XNode child : children) {
                    if ("age".equals(child.getName())) {
                        person.setAge(Integer.valueOf(child.getStringBody()));
                    }
                    if ("phone".equals(child.getName())) {
                        person.setPhone(child.getStringBody("phone"));
                    }
                    if ("name".equals(child.getName())) {
                        person.setName(child.getStringBody("name"));
                    }
                    if ("nickName".equals(child.getName())) {
                        person.setNickName(child.getStringBody("nickName"));
                    }
                    if ("createTime".equals(child.getName())) {
                        person.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(child.getStringBody("createTime")));
                    }
                }
                peopleLinkedBlockingQueue.offer(person);
            }
            System.out.println("peopleLinkedBlockingQueue = " + peopleLinkedBlockingQueue);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void method12() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            /*XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream);
            Configuration parse = xmlConfigBuilder.parse();
            Environment environment = parse.getEnvironment();
            System.out.printf("configuration:%s\n", JSON.toJSON(parse.getEnvironment().getId()));
            DataSource dataSource = environment.getDataSource();
            Connection connection = dataSource.getConnection();*/
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = build.openSession();
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
//            Person byId = mapper.findById();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method13() {
        try {
            InputStream inputStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = build.openSession();
            SqlSession sqlSession1 = build.openSession(true);
            SqlSession sqlSession2 = build.openSession(TransactionIsolationLevel.REPEATABLE_READ);
            SqlSession sqlSession3 = build.openSession(ExecutorType.REUSE);
            SqlSession sqlSession4 = build.openSession(ExecutorType.REUSE, true);
//            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
//            Person byId = mapper.findById(2);
//            System.out.println("byId = " + byId);
//            Person person = sqlSession.selectOne("org.apache.ibatis.custom.mapper.PersonMapper.findById",2);
//            List<Person> person = sqlSession.selectList("org.apache.ibatis.custom.mapper.PersonMapper.findById", 2);
//            System.out.println("person = " + person);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", 4);
            map.put("name", "sqlSession insert");
            map.put("nickName", "sqlSession nickName");
            map.put("createTime", new Date());
            map.put("age", 16);
            map.put("phone", "17799998888");
            int insertNum = sqlSession1.insert("org.apache.ibatis.custom.mapper.PersonMapper.save", map);
            Integer id = (Integer) map.get("id");
            System.out.println("id = " + id);
            System.out.println("insertNum = " + insertNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method14() {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            Person person = mapper.findById(3);
            System.out.println("person = " + person);
            List<Book> isbnBooks = person.getIsbnBook();
            String isbnBook = isbnBooks.get(0).getIsbn();
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            int bookNum = bookMapper.save(null, isbnBook, "springmvc攻略", 653, "描述信息......", new Date(), null);
            System.out.println("bookNum = " + bookNum);
            sqlSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method15() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            List<Object> list = sqlSession.selectList("org.apache.ibatis.custom.mapper.BookMapper.findAllByIsbn", "17162694");
            System.out.println("list = " + list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void method16() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            Map<String, Object> map = new HashMap<>();
            map.put("id", 3);
            List<Person> list = sqlSession.selectList("org.apache.ibatis.custom.mapper.PersonMapper.getPersonListById2");
            list.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method17() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            Map<String, Object> map = new HashMap<>();
            map.put("id", 3);
            List<Person> list = sqlSession.selectList("org.apache.ibatis.custom.mapper.PersonMapper.getPersonListById");
            list.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void method18() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Resources.getResourceAsStream("org/apache/ibatis/custom/resources/mybatis-config.xml");
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(resourceAsStream);
            Configuration parse = xmlConfigBuilder.parse();
            SqlSessionFactory sessionFactory = new DefaultSqlSessionFactory(parse);
            SqlSession sqlSession = sessionFactory.openSession(true);
            HashMap<String, Object> map = new HashMap<>(1);
            map.put("isbn","56321438");
            List<Object> list = sqlSession.selectList("org.apache.ibatis.custom.mapper.BookMapper.findAllByIsbn", map);
            System.out.printf("%s\n", list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void method19() {
        Reader resourceAsReader = null;
        try {
            resourceAsReader = Resources.getResourceAsReader("xx.xx.xx");
            new SqlSessionFactoryBuilder().build(resourceAsReader);
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(resourceAsReader);
            Configuration configuration = new Configuration();
            configuration.setVariables(Resources.getResourceAsProperties(""));
            // configuration.setEnvironment();
            configuration.setDefaultExecutorType(ExecutorType.BATCH);
            // configuration.setObjectFactory();

            DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
            SqlSession defaultSqlSession = defaultSqlSessionFactory.openSession();
            ScriptRunner scriptRunner = new ScriptRunner(defaultSqlSession.getConnection());
            // scriptRunner.runScript();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
