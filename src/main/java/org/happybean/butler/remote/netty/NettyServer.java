package org.happybean.butler.remote.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class NettyServer {

    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

    public void start(String host, int port) throws InterruptedException {
        bootstrap.group(eventExecutors)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        //pipeline.addLast("handler");
                    }
                });
        bootstrap.bind(host, port).sync();
    }
}
