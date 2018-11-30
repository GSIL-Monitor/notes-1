package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import server.UserParam;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Author   NieYinjun
 * Date     2018/9/22 21:07
 * TAG
 */
public class Client {
    private static class SingletonHolder {
        static final Client instance = new Client();
    }

    public static Client getInstance(){
        return SingletonHolder.instance;
    }

    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf ;

    private Client(){
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        sc.pipeline().addLast(new StringEncoder(Charset.forName("gbk")));
                        sc.pipeline().addLast(new StringDecoder(Charset.forName("gbk")));
                        sc.pipeline().addLast(new ClientHandler());  //客户端业务处理类
                    }
                });
    }

    public void connect(){
        try {
            this.cf = b.connect("127.0.0.1", 8765).sync();
            System.out.println("远程服务器已经连接, 可以进行数据交换..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getChannelFuture(){
        //如果管道没有被开启或者被关闭了，那么重连
        if(this.cf == null){
            this.connect();
        }
        if(!this.cf.channel().isActive()){
            this.connect();
        }
        return this.cf;
    }

    public static void main(String[] args) throws Exception{
        final Client c = Client.getInstance();

        ChannelFuture cf = c.getChannelFuture();

        cf.channel().writeAndFlush("25,A,app_tcp,tcp_advice_999,msg"+System.getProperty("line.separator"));



        TimeUnit.SECONDS.sleep(1000);
        System.out.println("断开连接,主线程结束..");
    }
}
