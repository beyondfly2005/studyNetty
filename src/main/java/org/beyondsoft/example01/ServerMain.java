package org.beyondsoft.example01;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 客户端启动类
 */
public class ServerMain {

    private final int port;

    public ServerMain(int port) {
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
            ServerBootstrap bootstrap = new ServerBootstrap();
            //3、配置Bootstrap
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                          @Override
                          protected void initChannel(SocketChannel socketChannel) throws Exception {
                              socketChannel.pipeline().addLast(new ServerHander());
                          }
                      }
                    );

            //4、连接远端、生成ChannelFuture实例
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("在" + future.channel().localAddress() + "上 开启监听");

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
        new ServerMain(18080).run();
    }
}
