package com.xiaomi.cug.config;

import com.xiaomi.cug.interceptor.RoutingInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class DataSourceConfig {
        @Bean("bmsDb0")
        @ConfigurationProperties(prefix = "spring.datasource.db0")
        public DataSource db0() {
                DataSource ds = DataSourceBuilder.create().build();
                return ds;
        }

        @Bean("bmsDb1")
        @ConfigurationProperties(prefix = "spring.datasource.db1")
        public DataSource db1() {
                DataSource ds = DataSourceBuilder.create().build();
                return ds;
        }

        @Bean("routingDataSource")
        public DataSource routingDataSource(@Qualifier("bmsDb0") DataSource db0,
                        @Qualifier("bmsDb1") DataSource db1) {
                Map<Object, Object> map = new HashMap<>();
                RoutingDataSource routingDataSource = new RoutingDataSource();
                map.put("bmsDb0", db0);
                map.put("bmsDb1", db1);
                routingDataSource.setDefaultTargetDataSource(db0);
                routingDataSource.setTargetDataSources(map);
                return routingDataSource;
        }

        @Bean
        public SqlSessionFactory sqlSessionFactory(@Qualifier("routingDataSource") DataSource ds, @Qualifier("routingInterceptor") RoutingInterceptor routingInterceptor) throws Exception {
                org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
                config.setMapUnderscoreToCamelCase(true);
                SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
                factoryBean.setDataSource(ds);
                factoryBean.setConfiguration(config);
                factoryBean.setPlugins(routingInterceptor);
                return factoryBean.getObject();
        }

        @Bean
        public DataSourceTransactionManager transactionManager(@Qualifier("routingDataSource") DataSource ds) {
                return new DataSourceTransactionManager(ds);
        }
}
