package org.apache.ibatis.custom.plugin;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * @author ck163
 * @date 2022-04-15 15:20:00
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class}),
        @Signature(type = StatementHandler.class,
                method = "update",
                args = {Statement.class}),
        @Signature(type = StatementHandler.class,
                method = "batch",
                args = {Statement.class}),
        @Signature(type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class})
})
public class MyStatementPlugin implements Interceptor {
    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /*System.out.printf("org.apache.ibatis.custom.plugin.MyStatementPlugin plugin load....");
        // pre process
        if (invocation.getTarget() instanceof StatementHandler) {
            Method method = invocation.getMethod();
            Object[] args = invocation.getArgs();
            Object target = invocation.getTarget();
            Parameter[] parameters = method.getParameters();
            System.out.println("method.getName() = " + method.getName());
            System.out.printf("args %s\n", Arrays.toString(args));
            System.out.println("target = " + target);
            System.out.println("parameters = " + parameters);
            System.out.printf("parameters %s\n", Arrays.toString(parameters));
        }*/
        Method method = invocation.getMethod();
        if ("query".equalsIgnoreCase(method.getName())) {
            System.out.printf("query......\n");
            Object target = invocation.getTarget();
            Object[] args = invocation.getArgs();
            System.out.printf("args.length:%d\n", args.length);
            Object arg0 = args[0];
            Object arg1 = args[1];
            if (target instanceof RoutingStatementHandler) {
                RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) target;
                BoundSql boundSql = routingStatementHandler.getBoundSql();
                String sql = boundSql.getSql();
                Object parameterObject = boundSql.getParameterObject();
//                System.out.printf("sql:%s\nparameterObject:%s\n", sql, parameterObject);
                System.out.printf("%-10s====> %s\n", "sql statment", sql);
                if (parameterObject instanceof Map) {
                    Map<String, Object> map = (Map) parameterObject;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.printf("%-10s====> %s\n", entry.getKey(), entry.getValue());
                    }
                } else {
                    System.out.printf("%-10s====> %s\n", "sql param", parameterObject);
                }
            }
            if (arg0 instanceof Statement) {
                Statement statement = (Statement) arg0;
                System.out.println("statement = " + statement);
            }
            System.out.println("arg1 = " + arg1);
            if (arg1 instanceof ResultHandler) {
                ResultHandler resultHandler = (ResultHandler) arg1;
                System.out.println("resultHandler = " + resultHandler);
            }
//            System.out.println("target = " + target);
//            System.out.printf("args %s\n", Arrays.toString(args));
        } else if ("batch".equalsIgnoreCase(method.getName())) {
            System.out.printf("batch......\n");
        } else if ("update".equalsIgnoreCase(method.getName())) {
            System.out.printf("update......\n");
        } else if ("prepare".equalsIgnoreCase(method.getName())) {
            System.out.printf("update......\n");
            Object target = invocation.getTarget();
            System.out.println("target = " + target);
            Object[] args = invocation.getArgs();
            Object arg = args[0];
            if (arg instanceof Connection) {
                Connection connection = (Connection) arg;
//                connection.prepareStatement();
            }
        }
        // after process
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
