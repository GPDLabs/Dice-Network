//package org.gpd.dicenetwork.task;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.gpd.dicenetwork.config.ServerStatus;
//import org.gpd.dicenetwork.config.ServerStatusManager;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.ScheduledFuture;
//
///**
// *  Lottery Scheduled Tasks
// */
//@Component
//@Slf4j
//public class LotteryJob implements Job {
//
//    @Autowired
//    private ServerStatusManager serverStatus;
//
//
//
////    @Autowired
////    private TaskScheduler taskScheduler;
//
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println("Lottery Job Executed");
//    }
//}
