package org.apache.ibatis.custom.handler;

import org.apache.ibatis.custom.domain.Person;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author ck163
 * @date 2022-04-15 14:04:17
 */
public class CustomHandler implements TypeHandler<Person> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Person parameter, JdbcType jdbcType) throws SQLException {
        parameter.setAge(34);
        parameter.setCreateTime(new Date());
        ps.setObject(i, parameter);
    }

    @Override
    public Person getResult(ResultSet rs, String columnName) throws SQLException {
        return (Person) rs.getObject(columnName);
    }

    @Override
    public Person getResult(ResultSet rs, int columnIndex) throws SQLException {
        return (Person) rs.getObject(columnIndex);
    }

    @Override
    public Person getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (Person) cs.getObject(columnIndex);
    }
}
