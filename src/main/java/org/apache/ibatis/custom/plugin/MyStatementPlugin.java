package org.apache.ibatis.custom.plugin;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
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
        String sql = "";
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

                sql = boundSql.getSql();
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
            Object[] args = invocation.getArgs();
            if (args.length >= 1) {
                Statement statement = (Statement) args[0];
            }
        } else if ("update".equalsIgnoreCase(method.getName())) {
            System.out.printf("update....update..\n");
            Object target = invocation.getTarget();
            Object[] args = invocation.getArgs();
            System.out.println("target = " + target);
            System.out.printf("args %s\n", Arrays.toString(args));
        } else if ("prepare".equalsIgnoreCase(method.getName())) {
            System.out.printf("prepare......\n");
            Object target = invocation.getTarget();
            System.out.println("target = " + target);
            Object[] args = invocation.getArgs();
            if (target instanceof StatementHandler) {
                StatementHandler statementHandler = (StatementHandler) target;
                sql = statementHandler.getBoundSql().getSql();
            }
        }
        // after process
        Object proceed = null;
        long startTimeMillis = 0;
        try {
            startTimeMillis = System.currentTimeMillis();
            proceed = invocation.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTimeMillis = System.currentTimeMillis();
            Long queryTime = Long.valueOf(properties.getProperty("queryTime", "3"));
            Long pauseTime = Math.max(queryTime, 0);
            long time = endTimeMillis - startTimeMillis;
            if (time > pauseTime * 1000) {
                System.out.printf("慢sql记录：%s\n耗时[%s]毫秒\n", sql, time);
            }
        }
        return proceed;
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
