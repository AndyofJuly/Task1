package com.game.netty.secondclient;

import com.game.common.protobuf.DataInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Scanner;
/**
 * netty客户端2
 * @author maoyuanming0806 and andy
 * @create 2020/6/10 17:32
 */
@Service("clientTwo")
public class ClientTwo {

    private final String HOST_IP = "127.0.0.1";
    private final int PORT = 7000;

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
                            //解码器，通过Google Protocol Buffers序列化框架动态的切割接收到的ByteBuf
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            //将接收到的二进制文件解码成具体的实例，这边接收到的是服务端的ResponseBank对象实列
                            pipeline.addLast(new ProtobufDecoder(DataInfo.ResponseMsg.getDefaultInstance()));
                            //Google Protocol Buffers编码器
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            //Google Protocol Buffers编码器
                            pipeline.addLast(new ProtobufEncoder());
                            //加入自己的处理器
                            pipeline.addLast(new ClientHandlerTwo());
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
                        id = " "+s[1];
                    }
                    DataInfo.RequestMsg requestMsg = DataInfo.RequestMsg.newBuilder().setMsg(msg+id).build();
                    channelFuture.channel().writeAndFlush(requestMsg);
                }
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        ClientTwo clientTwo = (ClientTwo)ac.getBean("clientTwo");
        clientTwo.run();
    }
}
