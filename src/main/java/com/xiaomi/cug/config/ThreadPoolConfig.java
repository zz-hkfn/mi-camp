package com.xiaomi.cug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {
        @Bean
        public ExecutorService delayDeleteExecutor() {
                return new ThreadPoolExecutor(
                                4,
                                8,
                                60,
                                TimeUnit.SECONDS,
                                new LinkedBlockingQueue<>(100),
                                new ThreadFactory() {
                                        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
                                        private int count = 0;
                                        @Override
                                        public Thread newThread(Runnable r) {
                                                Thread thread = defaultFactory.newThread(r);
                                                thread.setName("delay-delete-thread-" + count++);
                                                return thread;
                                        }
                                },
                                new ThreadPoolExecutor.AbortPolicy()
                );
        }
}
