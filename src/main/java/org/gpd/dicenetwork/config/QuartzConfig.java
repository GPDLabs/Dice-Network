//package org.gpd.dicenetwork.config;
//
//import jakarta.annotation.PostConstruct;
//import org.gpd.dicenetwork.task.LotteryJob;
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class QuartzConfig {
//
//    @Bean
//    public JobDetail transactionVerifierJobDetail() {
//        return JobBuilder.newJob(LotteryJob.class)
//                .withIdentity("transactionVerifierJob", "group1")
//                .storeDurably()
//                .build();
//    }
//
//    @Bean
//    public Trigger transactionVerifierTrigger() {
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0,10,20,30,40,50 * * * * ?");
//
//        return TriggerBuilder.newTrigger()
//                .forJob(transactionVerifierJobDetail())
//                .withIdentity("transactionVerifierTrigger", "group1")
//                .withSchedule(scheduleBuilder)
//                .build();
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setOverwriteExistingJobs(true);
//        factory.setDataSource(dataSource);
//        factory.setQuartzProperties(quartzProperties());
//        factory.setTriggers(transactionVerifierTrigger());
//        return factory;
//    }
//
//    @Bean
//    public org.quartz.Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
//        return schedulerFactoryBean.getScheduler();
//    }
//
//    @Bean
//    public org.springframework.core.io.Resource quartzProperties() {
//        return new org.springframework.core.io.ClassPathResource("/quartz.properties");
//    }
//
//    @PostConstruct
//    public void init() throws SchedulerException {
//        Scheduler scheduler = scheduler(schedulerFactoryBean(null));
//        if (!scheduler.isStarted()) {
//            scheduler.start();
//            System.out.println("Quartz Scheduler started.");
//        }
//    }
//}
