package com.game.netty.server;

//import com.game.common.InitStaticResource;
import com.game.entity.store.*;
import com.game.service.assis.InitGame;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty服务端
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/5/12 22:32
 */

public class NettyServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        //服务器启动，从数据库/excel配置表中加载场景信息
        //new InitStaticResource();
        new EquipmentResource();
        new MonsterResource();
        new NpcResource();
        new PotionResource();
        new RoleResource();
        new SceneResource();
        new SkillResource();
        new InitGame();

        //1.定义server启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //2.定义工作组:boss分发请求给各个worker:boss负责监听端口请求，worker负责处理请求（读写）
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.定义工作组
        serverBootstrap.group(boss,worker);

        //4.设置通道channel
        serverBootstrap.channel(NioServerSocketChannel.class);

        //5.添加handler，管道中的处理器，通过ChannelInitializer来构造
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                //此方法每次客户端连接都会调用，是为通道初始化的方法

                //获得通道channel中的管道链（执行链、handler链）
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast("serverHandler1",new ServerHandler());
                pipeline.addLast(new StringEncoder());

                System.out.println("Client is requesting...");
            }
        });

        //6.设置参数
        //设置参数，TCP参数
        //连接缓冲池的大小
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);
        //维持链接的活跃，清除死链接
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //关闭延迟发送
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        //7.绑定ip和port
        try {
            //Future模式的channel对象
            ChannelFuture channelFuture = serverBootstrap.bind("127.0.0.1",8080).sync();
            //7.5.监听关闭
            //等待服务关闭，关闭后应该释放资源
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("server start got exception!");
            e.printStackTrace();
        }finally {
            //8.优雅的关闭资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
