package com.sah.nettry.handler;

import com.sah.nettry.model.TestModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author suahe
 * @date 2022/3/1
 * @ApiNote
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<TestModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TestModel testModel) throws Exception {
        System.out.println("第一个处理器，处理数据:"+testModel.getData()+"。。。");
        //传递给下一个处理器
        ctx.fireChannelRead(testModel);
    }
}
