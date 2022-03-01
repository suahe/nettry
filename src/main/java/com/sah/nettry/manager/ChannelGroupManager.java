package com.sah.nettry.manager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author suahe
 * @date 2022/3/1
 * @ApiNote
 */
public class ChannelGroupManager {
    /**
     * 全局channel管理器，管理存活的channel
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * ChannelId 字符串和channelId映射
     */
    public static Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();
    /**
     * ChannelId 字符串和channelId映射
     */
    public static Map<ChannelId, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();

    public static void add(Channel channel, ChannelHandlerContext ctx){
        channelIdMap.put(channel.id().asLongText(),channel.id());
        channelGroup.add(channel);
        ctxMap.put(channel.id(),ctx);
    }


    public static void remove(Channel channel){
        channelIdMap.remove(channel.id().asLongText());
        channelGroup.remove(channel);
        ctxMap.remove(channel.id());
    }

    public static Channel find(String channelStrId){
        ChannelId channelId = channelIdMap.get(channelStrId);
        if(channelId==null){
            throw new RuntimeException("未找到ChannelId");
        }
        return channelGroup.find(channelId);
    }

    public static ChannelHandlerContext findCtx(String channelStrId){
        ChannelId channelId = channelIdMap.get(channelStrId);
        if(channelId==null){
            throw new RuntimeException("未找到ChannelId");
        }
        return ctxMap.get(channelId);
    }

    public static int channelLength(){
        return channelGroup.size();
    }


}
