package com.zy.netty.server;

import com.zy.netty.handler.NettyServerFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {
    NioEventLoopGroup bossGroup;
    NioEventLoopGroup workerGroup;
    ServerBootstrap serverBootstrap;
    ChannelFuture cf;

    public NettyServer() {
        //1)创建BossGroup 和WorkerGroup
        /*
        1.创建两个线程组bossGroup和workerGroup
        2.bossGroup只处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        3.两个都是无限循环
        4.bossGroup和workerGroup含有的(NioEventLoop)的个数，默认是cpu核心数*2(NettyRuntime.availableProcessors() )
         */
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        //2)创建服务器端的启动对象，配置启动的参数
        //使用链式编程的方式进行设置
        serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                //.handler(new LoggingHandler(LogLevel.INFO))//该handle对应的是bossGroup，如果不想在accept设置业务处理逻辑就不用写
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //基于http协议，使用http编码和解码器
                        pipeline.addLast(new HttpServerCodec());
                        //是以块方式写，添加chunkedWriteHandler处理器
                        pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            说明：
                            1.因为http数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合在一起
                            2.这就是为什么，当浏览器发送大量数据时，就会发出多次http请求。
                             */

                        pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            说明：
                            1.对于webSocket，它的数据是以 帧（frame）形式传输
                            2.可以看到webSocketFrame 下面有六个子类
                            3.浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                            4.WebSocketServerProtocolHandler 核心功能 将http协议升级为ws协议，保持长链接
                             */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                        //自定义的handler，处理业务逻辑
                        pipeline.addLast(new NettyServerFrameHandler());
                    }
                });

    }

    public void start() {
        try {
            System.out.println("启动webstocket.....");
            cf = serverBootstrap.bind(9000).sync();
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
