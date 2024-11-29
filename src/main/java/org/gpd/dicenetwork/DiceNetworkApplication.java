package org.gpd.dicenetwork;

import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.gpd.dicenetwork.lottery.service.impl.LotteryListServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = "org.gpd.dicenetwork.*.mapper")
public class DiceNetworkApplication {

    @Autowired
    private LotteryListService lotteryListService;

    public static void main(String[] args) {
        SpringApplication.run(DiceNetworkApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        return args -> {
//            try {
//                JobDetail jobDetail = JobBuilder.newJob(LotteryJob.class)
//                        .withIdentity("transactionVerifierJob", "group1")
//                        .storeDurably()
//                        .build();
//
//                Trigger trigger = TriggerBuilder.newTrigger()
//                        .forJob(jobDetail)
//                        .withIdentity("transactionVerifierTrigger", "group1")
//                        .withSchedule(CronScheduleBuilder.cronSchedule("0,10,20,30,40,50 * * * * ?"))
//                        .build();
//
//                scheduler.scheduleJob(jobDetail, trigger);
//                System.out.println("定时任务已启动");
//            } catch (SchedulerException e) {
//                e.printStackTrace();
//            }
//        };
//    }


}
