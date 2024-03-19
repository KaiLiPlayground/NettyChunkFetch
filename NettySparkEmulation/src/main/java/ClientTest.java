import com.nettychunkfetch.client.NettyClient;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient("localhost", 8080);
        client.sendFetchRequest("chunk1"); // Send a request for "chunk1"
    }
}
