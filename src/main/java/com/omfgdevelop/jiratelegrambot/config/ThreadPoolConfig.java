package com.omfgdevelop.jiratelegrambot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${app.pool.size}")
    public Integer poolSize;

    @Value("${app.pool.alive.time}")
    public Long keepAliveTime;

    @Value("${app.pool.max.threads.count}")
    public Integer maxThreadsCount;

    @Bean
    public ExecutorService getPoolExecutor() {
        return new ThreadPoolExecutor(poolSize, maxThreadsCount,
                keepAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }
}
