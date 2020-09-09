package org.beyondsoft.example01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 客户端启动类
 */
public class ClientMain {
    private final String host;
    private final int port;

    public ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 配置参数 提供连接到远端的方法
     */
    public void run() throws Exception {
        //1、创建EventLoopGroup实例
        EventLoopGroup group = new NioEventLoopGroup();//IO连接线程池
        try {
            //2、创建Bootstrp实例
            Bootstrap bootstrap = new Bootstrap();
            //3、配置Bootstrap
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                             @Override
                             protected void initChannel(SocketChannel socketChannel) throws Exception {
                                 socketChannel.pipeline().addLast(new ClientHander());
                             }
                         }
                    );

            //4、连接远端、生成ChannelFuture实例
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello word", CharsetUtil.UTF_8));
            //阻塞操作，closeFuture()开启一个channel的监听器
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //5、关闭连接、释放资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new ClientMain("127.0.0.1",18080).run();
    }
}
