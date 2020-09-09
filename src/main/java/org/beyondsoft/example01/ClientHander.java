package org.beyondsoft.example01;

/**
 * 通用handler，处理I/O事件
 */
/*1、创建Handler类
  2、类添加Shrable注解*/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable  //线程安全
public class ClientHander extends SimpleChannelInboundHandler<ByteBuf> {//3、类继承SimpleChannelInboundHandler

    //4、重写channelRead()方法，处理接收到的消息
    /**
     * 处理接收到的消息
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        System.out.println("接收到的消息:"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    //5、重写execeptionCaught()反复处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /**
         * 处理IO异常  出现异常连接关闭
         */
        cause.printStackTrace();
        ctx.close();
    }
}
