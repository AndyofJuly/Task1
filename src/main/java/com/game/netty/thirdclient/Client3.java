package com.game.netty.thirdclient;

import com.game.dao.ConnectSql;
import com.game.netty.server.ServerHandler;
import com.game.service.assis.DynamicResource;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
/**
 * netty客户端3
 * @author maoyuanming0806 and andy
 * @create 2020/6/10 17:32
 */
public class Client3 {

    private final String HOST_IP;
    private final int PORT;

    public Client3(String host, int port){
        this.HOST_IP = host;
        this.PORT = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入一个解码器
                            pipeline.addLast("decoder",new StringDecoder());

                            //向pipeline加入编码器
                            pipeline.addLast("encode",new StringEncoder());

                            //加入自己的处理器
                            pipeline.addLast(new ClientHandler3());

                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(HOST_IP, PORT).sync();
            if (channelFuture.isSuccess()){
                Scanner scanner = new Scanner(System.in);
                String id = "";
                while (scanner.hasNextLine()){
                    String msg = scanner.nextLine();
                    if(msg.startsWith("loginR")){
                        String[] s = msg.split(" ");
                        id = " "+ ConnectSql.sql.selectRoleIdByName(s[1]);
                    }
                    channelFuture.channel().writeAndFlush(msg+id);
                    if("quit".equals(msg)){
                        break;
                    }
                }
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client3("127.0.0.1",7000).run();
    }
}

