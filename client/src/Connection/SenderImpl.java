package Connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Singleton
public class SenderImpl implements Sender{
    private final SocketChannel socketChannel;

    @Inject
    public SenderImpl(ClientConnectionManagerImpl clientConnectionManagerImpl) {
        this.socketChannel = clientConnectionManagerImpl.getSocketChannel();
    }

    public void send(Object serializedObject) throws IOException {
        System.out.println("sender 1");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(serializedObject);
        byte [] data = byteArrayOutputStream.toByteArray();
        System.out.println("sender 2");
        socketChannel.write(ByteBuffer.wrap(data));
    }
}
