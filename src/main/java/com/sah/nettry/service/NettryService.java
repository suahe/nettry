package com.sah.nettry.service;

import com.sah.nettry.handler.DecryptHandler;
import com.sah.nettry.handler.DiscardServerHandler;
import com.sah.nettry.handler.OtherHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author suahe
 * @date 2022/3/1
 * @ApiNote
 */
@Component
public class NettryService {

    @Value("${netty.port}")
    private int nettyPort;
    @Value("${netty.readerIdleTimeSeconds}")
    private int readerIdleTimeSeconds;

    public void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try{
            serverBootstrap.group(bossLoopGroup,workerLoopGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childOption(ChannelOption.SO_SNDBUF, 65535);
            serverBootstrap.childOption(ChannelOption.SO_RCVBUF, 65535);
            serverBootstrap.localAddress(nettyPort);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    ch.pipeline().addLast("idleStateHandler",new IdleStateHandler(readerIdleTimeSeconds, 0, 0));
                    ch.pipeline().addLast("decryptHandler", new DecryptHandler());
                    ch.pipeline().addLast("discardServerHandler", new DiscardServerHandler());
                    ch.pipeline().addLast("otherHandler", new OtherHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }
    }
}
