//package org.gpd.dicenetwork.nettyServer;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.gpd.dicenetwork.wallet.service.WalletService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//public class NettyServerConfig {
//
//    @Autowired
//    private NettyServerBootstrap nettyServerBootstrap;
//
//    @PostConstruct
//    public void startNettyServer() throws InterruptedException {
//        nettyServerBootstrap.start();
//    }
//
////    @PreDestroy
////    public void stopNettyServer() {
////        nettyServerBootstrap.stop();
////    }
//
////    @Autowired
////    private WalletService walletService;
//
////    @Bean
////    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
////        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////        executor.setCorePoolSize(10);
////        executor.setMaxPoolSize(20);
////        executor.setQueueCapacity(100);
////        executor.initialize();
////        return executor;
////    }
////
////    @Bean
////    public ServerBootstrap serverBootstrap() {
////        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
////        EventLoopGroup workerGroup = new NioEventLoopGroup();
////
////        ServerBootstrap bootstrap = new ServerBootstrap()
////                .group(bossGroup, workerGroup)
////                .channel(NioServerSocketChannel.class)
////                .childHandler(new ServerInitializer(walletService));
////
////        return bootstrap;
////    }
//}
