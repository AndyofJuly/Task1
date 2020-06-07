package com.game.netty.newclient;

import com.game.dao.ConnectSql;
import com.game.netty.client.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * netty客户端
 * 参考来源：csdn上作者maoyuanming0806的文章，参考了如何用netty实现客户端与服务端的通信，属于通用类代码
 * @author maoyuanming0806 and andy
 * @create 2020/06/02 9:32
 */

public class NewNettySingleClient {

    public static void main(String[] args) {
        startClient();
    }

    public static void startClient(){
        //1.定义服务类
        Bootstrap clientBootstap = new Bootstrap();

        //2.定义执行线程组
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.设置线程池
        clientBootstap.group(worker);

        //4.设置通道
        clientBootstap.channel(NioSocketChannel.class);

        //5.添加Handler
        clientBootstap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                System.out.println("client2 channel init!");
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("StringDecoder",new StringDecoder());
                pipeline.addLast("StringEncoder",new StringEncoder());
                pipeline.addLast("ClientHandler2",new NewClientHandler());
            }
        });

        //6.建立连接
        ChannelFuture channelFuture = clientBootstap.connect("127.0.0.1",8080);
        try {
            //7.测试输入
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("请输入命令：");
            String id = "";
            while(true){
                String msg = bufferedReader.readLine();
                if(msg.startsWith("loginR")||msg.startsWith("registerR")){
                    String[] s = msg.split(" ");
                    id = " "+ ConnectSql.sql.selectRoleIdByName(s[1]);
                }
                channelFuture.channel().writeAndFlush(msg+id);
                if("quit".equals(msg)){
                    break;
                }
                if("quit".equals(msg)){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //8.关闭连接
            worker.shutdownGracefully();
        }
    }
}