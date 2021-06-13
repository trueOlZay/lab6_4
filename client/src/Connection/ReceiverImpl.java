package Connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Singleton
public class ReceiverImpl implements Receiver{

    private final Decrypting decryptingImpl;

    @Inject
    public ReceiverImpl(Decrypting decryptingImpl) {
        this.decryptingImpl = decryptingImpl;
    }

    public void receive(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024*500);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int n;
        int i=0;
        while ((n=socketChannel.read(byteBuffer))>0) {
            i++;
            byteBuffer.flip();
            byteArrayOutputStream.write(byteBuffer.array(),0, n);
            byteBuffer.clear();
        }
        if(i!=0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            System.out.println("receiver, wow");
            decryptingImpl.decrypt(objectInputStream.readObject());
        }

    }
}
