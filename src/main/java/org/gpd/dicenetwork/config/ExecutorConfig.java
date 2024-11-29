package org.gpd.dicenetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ExecutorConfig {
    //定义核心线程数
    public static final  int CORE_POOL_SIZE = 10;
    // 最大线程数
    public static final  int MAX_POOL_SIZE = 20;
    // 任务队列容量大小
    public static final  int QUEUE_MAX_COUNT = 100;

    @Bean("asyncScheduledPool")
    public Executor asyncScheduledPool(){
        //自定义线程池
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPoolExecutor.setCorePoolSize(CORE_POOL_SIZE);
        //设置最大线程数 ： 长工 +  临时工
        threadPoolExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        //设置任务队列容量大小
        threadPoolExecutor.setQueueCapacity(QUEUE_MAX_COUNT);
        //设置线程的名称前缀
        threadPoolExecutor.setThreadNamePrefix("myTask-");
        //设置拒绝策略
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return  threadPoolExecutor;
    }
}
