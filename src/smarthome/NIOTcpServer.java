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
public class NIOTcpServer extends Thread{

    private static final Logger log = Logger.getLogger(NIOTcpServer.class.getName());
    private InetSocketAddress inetSocketAddress;
    private Handler handler = new ServerHandler();

    public NIOTcpServer(String hostname, int port) {
        inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    @Override
    public void run() {

            try {
                Selector selector = Selector.open(); // ��ѡ����
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // ��ͨ��
                serverSocketChannel.configureBlocking(false); // ������
                serverSocketChannel.socket().bind(inetSocketAddress);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // ��ͨ��ע��ѡ�����Ͷ�Ӧ�¼���ʶ
                log.info("Server: socket server started.");
                while (true) { // ��ѯ

                    int nKeys = selector.select();
                    if (nKeys > 0) {
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();
                        Iterator<SelectionKey> it = selectedKeys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = it.next();
                            if (key.isAcceptable()) {
                                log.info("Server: SelectionKey is acceptable.");
                                handler.handleAccept(key);
                            } else if (key.isReadable()) {
                                log.info("Server: SelectionKey is readable.");
                                handler.handleRead(key);
                            } else if (key.isWritable()) {
                                log.info("Server: SelectionKey is writable.");
                                handler.handleWrite(key);
                            }
                            it.remove();
                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {
        NIOTcpServer server = new NIOTcpServer("localhost", 9999);
        server.start();
    }
}
