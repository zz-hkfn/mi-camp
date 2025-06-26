package com.xiaomi.cug.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
        @Override protected Object determineCurrentLookupKey() {
                return DynamicDataSourceContextHolder.get();
        }
}
