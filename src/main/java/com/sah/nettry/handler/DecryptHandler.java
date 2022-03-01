package com.sah.nettry.handler;

import com.sah.nettry.manager.ChannelGroupManager;
import com.sah.nettry.model.TestModel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author suahe
 * @date 2022/3/1
 * @ApiNote
 */
@Slf4j
public class DecryptHandler extends ByteToMessageDecoder {

    /**
     * 心跳丢失计数器
     */
    private int heaetBeatcounter;

    /**
     * 未收到心跳关闭本地连接次数
     */
    private int deleteConnectionTimes = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        ChannelGroupManager.add(ctx.channel(),ctx);
        log.info("channelId:"+channelId+"客户端已经连接了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        log.info("channelId:"+channelId+"客户端退出了！");
        ChannelGroupManager.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        log.debug("channelId:"+channelId+"连接出现异常：");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        System.out.println("对传输数据解码。。。");
        handleHeartbeat(ctx);
        String str = byteBuf.toString(CharsetUtil.UTF_8);
        TestModel testModel = new TestModel();
        testModel.setData(str);
        out.add(testModel);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                String channelId = ctx.channel().id().asLongText();
                if(heaetBeatcounter>=deleteConnectionTimes) {
                    ctx.channel().close().sync();
                    ChannelGroupManager.remove(ctx.channel());
                    log.error("channelId:"+channelId+"已与Client断开连接");
                }else {
                    heaetBeatcounter++;
                }
            }
        }
    }

    /**
     * 处理心跳包
     */
    private void handleHeartbeat(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asLongText();
        // 将心跳丢失计数器置为0
        heaetBeatcounter = 0;
        log.info("channelId:"+channelId+"收到心跳包");
    }
}
