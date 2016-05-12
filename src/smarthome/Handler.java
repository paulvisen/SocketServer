package smarthome;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Visen (zhvisen@gmail.com) on 2016/5/12.
 */
public interface Handler {

        /**
         * ����{@link SelectionKey#OP_ACCEPT}�¼�
         * @param key
         * @throws IOException
         */

        void handleAccept(SelectionKey key) throws IOException;
        /**
         * ����{@link SelectionKey#OP_READ}�¼�
         * @param key
         * @throws IOException
         */
        void handleRead(SelectionKey key) throws IOException;
        /**
         * ����{@link SelectionKey#OP_WRITE}�¼�
         * @param key
         * @throws IOException
         */
        void handleWrite(SelectionKey key) throws IOException;

}
