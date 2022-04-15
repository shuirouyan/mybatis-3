package org.apache.ibatis.custom;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author ck163
 * @date 2022-04-15 15:20:00
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class})})
public class MyStatementPlugin implements Interceptor {
    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.printf("org.apache.ibatis.custom.MyStatementPlugin plugin load....");
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
        }
        Object proceed = invocation.proceed();
        // after process
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
