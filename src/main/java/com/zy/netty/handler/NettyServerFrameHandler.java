package com.zy.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.pojo.bo.MsgBO;
import com.zy.utils.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TextWebSocketFrame 类型，表示一个文本帧（Frame）
 * 范型的内容是客户端与服务端交互的内容的类型
 */
public class NettyServerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //定义一个channel组，管理所有的channel
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        String recv = msg.text();
        MsgBO msgBO = JSON.parseObject(recv, MsgBO.class);
        if ("up".equals(msgBO.getFlag())) {
            //如果是上线，将用户的id，与channel的id加入一个map
            NettyUtil.S_C.put(msgBO.getId(),channel);
            NettyUtil.C_S.put(channel,msgBO.getId());
            NettyUtil.channelGroup.stream().forEach(node->{
                node.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msgBO)));
            });
        }else if("send".equals(msgBO.getFlag())){
            Channel targetChannel = NettyUtil.S_C.get(msgBO.getTarget());
            targetChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msgBO)));
        }
    }

    /**
     * 当web客户端连接后，就会触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的值，LongText 是唯一的 ShortText不是唯一的
        Channel channel = ctx.channel();
        System.out.println("handlerAdded 被调用" + channel.id().asLongText());
        System.out.println("handlerAdded 被调用" + channel.id().asShortText());
        //当用户上线了 注册其通道
        NettyUtil.channelGroup.add(channel);
        //返回用户的通道id
        //ctx.channel().writeAndFlush(new TextWebSocketFrame(channel.id().asLongText()));


    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String longText = channel.id().asLongText();
        System.out.println("handlerRemoved 被调用" + longText);
        NettyUtil.channelGroup.remove(channel);
        String key = NettyUtil.C_S.remove(channel);
        NettyUtil.S_C.remove(key);
        MsgBO msgBO = new MsgBO();
        msgBO.setId(key);
        msgBO.setFlag("down");
        NettyUtil.channelGroup.stream().forEach(node->{
            node.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(msgBO)));
        });

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了" + "\t" + sdf.format(new Date()));
    }

    //表示channel处于非活动状态，提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "下线了" + "\t" + sdf.format(new Date()));
    }



}
