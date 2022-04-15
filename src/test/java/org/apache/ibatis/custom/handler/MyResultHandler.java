package org.apache.ibatis.custom.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author ck163
 * @date 2022-04-15 14:29:01
 */
public class MyResultHandler implements ResultHandler {
    @Override
    public void handleResult(ResultContext resultContext) {
        Object resultObject = resultContext.getResultObject();
        System.out.println("resultObject = " + resultObject);
    }

    /*@Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        List<E> list = new ArrayList<>();
        ResultSet resultSet = stmt.getResultSet();
        while (resultSet.next()) {

            org.apache.ibatis.custom.domain.Person person = org.apache.ibatis.custom.domain.Person.newInstanceMethod();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setNickName(resultSet.getString("nickName"));
            try {
                person.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultSet.getString("createTime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            person.setAge(resultSet.getInt("age"));
            list.add((E) person);
        }
        return list;
    }

    @Override
    public void handleOutputParameters(CallableStatement cs) throws SQLException {

    }*/
}
