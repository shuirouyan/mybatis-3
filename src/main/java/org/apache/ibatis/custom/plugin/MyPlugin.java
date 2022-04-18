package org.apache.ibatis.custom.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Properties;

/**
 * @author ck163
 * @date 2022-04-15 15:00:38
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MyPlugin implements Interceptor {

    private Properties properties;

    /**
     * @param target executor的实例
     * @return object
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // implement pre-processing if needed
        String name = invocation.getMethod().getName();
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor) {
            Object arg = args[0];
            Object param = args[1];
            Object limitParameter = args[2];
            if (arg instanceof MappedStatement) {
                MappedStatement mappedStatement = (MappedStatement) arg;
                //mappedStatement.getBoundSql()
                SqlSource sqlSource = mappedStatement.getSqlSource();
                BoundSql boundSql = sqlSource.getBoundSql(param);
                if (boundSql != null) {
                    String sql = boundSql.getSql();
                    System.out.printf("source sql ====> %s\n", sql);
                    if (args.length > 1 && param instanceof Map) {
                        Map arg1 = (Map) param;
                        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                            String property = parameterMapping.getProperty();
                            System.out.printf("%-11s ===> %s\n", property, arg1.get(property));
                        }
                        System.out.println();
                    }
                }
            }
        }
        /*Method method = invocation.getMethod();
        System.out.printf("method.getClass() %s\n", method.getClass());
        Object[] args = invocation.getArgs();
        Object target = invocation.getTarget();
        Parameter[] parameters = method.getParameters();*/
        /*System.out.println("method.getName() = " + method.getName());
        System.out.printf("args %s\n", Arrays.toString(args));
        System.out.println("target = " + target);
        System.out.println("parameters = " + parameters);
        System.out.printf("parameters %s\n", Arrays.toString(parameters));

        System.out.printf("pre-processing %s plugin.......\n", name);
        System.out.printf("%s\n", invocation.getMethod().getDeclaringClass());*/
        long startExecutor = System.currentTimeMillis();
        Object returnObject = invocation.proceed();
        long endExecutor = System.currentTimeMillis();
        // implement post-processing if needed
        System.out.printf("post-processing %s plugin....[sql executor spend time %s ms]...\n", name, (endExecutor - startExecutor));
        return returnObject;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        System.out.println("properties = " + properties);
    }

}
