package com.sah.nettry.handler;

import com.sah.nettry.model.TestModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author suahe
 * @date 2022/3/1
 * @ApiNote
 */
public class OtherHandler extends SimpleChannelInboundHandler<TestModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TestModel testModel) throws Exception {
        System.out.println("第二个处理器，处理数据:"+testModel.getData()+"。。。");
    }
}
