package org.gpd.dicenetwork.nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DatagramPacketEncoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.gpd.dicenetwork.config.ServerStatusManager;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.gpd.dicenetwork.wallet.entity.WalletEntity;
import org.gpd.dicenetwork.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NettyServerBootstrap{

//    @Autowired
//    private NettyServerHandler nettyServerHandler;

    @Autowired
    private ApplicationContext context;

    private ChannelFuture channelFuture;
    private static final String DELIMITER = "#C";


    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start() {
        new Thread(() -> {
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(8080)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
//                                p.addLast(new LineBasedFrameDecoder(2048));
                                ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER.getBytes());
                                p.addLast(new DelimiterBasedFrameDecoder(1024 * 17, delimiter));
                                p.addLast(new StringDecoder());
                                p.addLast(new StringEncoder());
                                p.addLast(new NettyServerHandler(
                                    context.getBean(ClientManagerService.class),
                                    context.getBean(WalletService.class),
                                    context.getBean(LotteryListService.class),
                                    context.getBean(ServerStatusManager.class)
                                ));
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                channelFuture = b.bind().sync();
//                channelFuture.channel().closeFuture().sync();
                // 注册关闭监听器
                channelFuture.channel().closeFuture().addListener(future -> {
                    System.out.println("Server is shutting down...");
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                });

                // 保持线程存活，防止线程结束导致服务器关闭
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }

    @PreDestroy
    public void stop() {
        if (channelFuture != null && channelFuture.channel().isOpen()) {
            channelFuture.channel().closeFuture().syncUninterruptibly();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}

