package com.xiaomi.cug.interceptor;

import com.xiaomi.cug.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@Intercepts({
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                                RowBounds.class, ResultHandler.class})
})
public class RoutingInterceptor implements Interceptor {

        private static final Pattern TABLE_PATTERN = Pattern.compile("(?i)\\b(battery_signal)\\b");
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

        @Override public Object intercept(Invocation invocation) throws Throwable {
                Object[] args = invocation.getArgs();

                MappedStatement ms = (MappedStatement) args[0];
                Object parameterObject = args[1];

                Long chassisNumber = null;
                LocalDateTime createdTime = null;

                if (parameterObject instanceof Map) {
                        Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                        if (paramMap.containsKey("chassisNumber") && paramMap.containsKey("reportTime")) {
                                chassisNumber = (Long) paramMap.get("chassisNumber");
                                createdTime = (LocalDateTime) paramMap.get("reportTime");
                        }
                } else {
                        try {
                                if (null != parameterObject) {
                                        Class<?> clazz = parameterObject.getClass();

                                        Field vidField = getField(clazz, "chassisNumber");
                                        if (vidField != null) {
                                                vidField.setAccessible(true);
                                                Object vidVal = vidField.get(parameterObject);
                                                if (vidVal instanceof Long) {
                                                        chassisNumber = (Long) vidVal;
                                                }
                                        }

                                        Field timeField = getField(clazz, "reportTime");
                                        if (timeField != null) {
                                                timeField.setAccessible(true);
                                                Object timeVal = timeField.get(parameterObject);
                                                if (timeVal instanceof LocalDateTime) {
                                                        createdTime = (LocalDateTime) timeVal;
                                                }
                                        }
                                }
                        }
                        catch (IllegalAccessException e) {
                                log.error("Failed to access fields from parameterObject", e);
                        }
                }


                boolean routed = false;
                try {
                        if (chassisNumber != null) {
                                String dbKey = determineDB(chassisNumber);
                                DynamicDataSourceContextHolder.set(dbKey);
                                routed = true;

                                // 替换表名
                                BoundSql boundSql = ms.getBoundSql(parameterObject);
                                String sql = boundSql.getSql();
                                if (sql != null && createdTime != null) {
                                        String newTableName = getTableName("battery_signal", createdTime);
                                        String newSql = replaceTableName(sql, newTableName);

                                        // 构造新的 BoundSql
                                        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSql.getParameterMappings(), parameterObject);

                                        // 将 additionalParameters 拷贝（否则 foreach、where 标签可能报错）
                                        for (String key : boundSql.getAdditionalParameters().keySet()) {
                                                newBoundSql.setAdditionalParameter(key, boundSql.getAdditionalParameter(key));
                                        }

                                        // 创建新的 MappedStatement（否则 SQL 替换了也不生效）
                                        MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));

                                        // 替换参数为新的 MappedStatement
                                        args[0] = newMs;
                                }
                        }

                        return invocation.proceed();
                }
                catch (ReflectiveOperationException e) {
                        log.error("RoutingInterceptor error ", e);
                }
                finally {
                        if (routed) {
                                DynamicDataSourceContextHolder.clear();
                        }
                }
                return null;
        }

        private Field getField(Class<?> clazz, String fieldName) {
                while (clazz != null && clazz != Object.class) {
                        try {
                                return clazz.getDeclaredField(fieldName);
                        } catch (NoSuchFieldException e) {
                                clazz = clazz.getSuperclass(); // 递归向上找
                        }
                }
                return null;
        }

        private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
                MappedStatement.Builder builder = new MappedStatement.Builder(
                                ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

                builder.resource(ms.getResource());
                builder.fetchSize(ms.getFetchSize());
                builder.statementType(ms.getStatementType());
                builder.keyGenerator(ms.getKeyGenerator());
                builder.keyProperty(ms.getKeyProperties() == null ? null : String.join(",", ms.getKeyProperties()));
                builder.timeout(ms.getTimeout());
                builder.parameterMap(ms.getParameterMap());
                builder.resultMaps(ms.getResultMaps());
                builder.resultSetType(ms.getResultSetType());
                builder.cache(ms.getCache());
                builder.flushCacheRequired(ms.isFlushCacheRequired());
                builder.useCache(ms.isUseCache());

                return builder.build();
        }

        static class BoundSqlSqlSource implements SqlSource {
                private final BoundSql boundSql;

                public BoundSqlSqlSource(BoundSql boundSql) {
                        this.boundSql = boundSql;
                }

                @Override
                public BoundSql getBoundSql(Object parameterObject) {
                        return boundSql;
                }
        }

        // 自动路由到哪个数据库，跟前面设置数据库路由中的key保持一致
        private static String determineDB(Long chassisNumber) {
                long idx = chassisNumber%2;
                return "bmsDb" + idx;
        }

        // 按照时间分表，每个月一个表
        private static String getTableName(String base, LocalDateTime createdTime) {
                String format = DATE_TIME_FORMATTER.format(createdTime);
                return base +"_" + format;
        }

        private String replaceTableName(String originalSql, String newTableName) {
                Matcher matcher = TABLE_PATTERN.matcher(originalSql);
                return matcher.replaceAll(newTableName);
        }

        @Override public Object plugin(Object target) {
                return Plugin.wrap(target, this);
        }
}
