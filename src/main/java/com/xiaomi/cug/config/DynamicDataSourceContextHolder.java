package com.xiaomi.cug.config;

public class DynamicDataSourceContextHolder {
        private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
        public static void set(String dbKey) {
                contextHolder.set(dbKey);
        }

        public static String get() {
                return contextHolder.get();
        }

        public static void clear() {
                contextHolder.remove();
        }
}
