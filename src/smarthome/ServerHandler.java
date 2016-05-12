package smarthome;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Visen (zhvisen@gmail.com) on 2016/5/12.
 */
public class ServerHandler implements Handler {

    private static final Logger log = Logger.getLogger(ServerHandler.class.getName());

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        log.info("Server: accept client socket " + socketChannel);
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ);


    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        log.info("read");
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        SocketChannel socketChannel = (SocketChannel)key.channel();
        while(true) {
            try{
                    int readBytes = socketChannel.read(byteBuffer);
                    if(readBytes>0) {
                        log.info("Server: readBytes = " + readBytes);
                        log.info("Server: data = " + new String(byteBuffer.array(), 0, readBytes));
                        System.out.println("Server: data = " + new String(byteBuffer.array(), 0, readBytes));
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer.wrap("result".getBytes()));
                        byteBuffer.clear();
                        break;
                    }




            }catch (IOException e){
                log.info("cancel");
                key.cancel();
                break;
            }

        }
        socketChannel.close();
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.flip();
        SocketChannel socketChannel = (SocketChannel)key.channel();
        socketChannel.write(byteBuffer);
        if(byteBuffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        byteBuffer.compact();
    }
}
