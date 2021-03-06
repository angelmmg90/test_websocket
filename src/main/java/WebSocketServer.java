import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*WebSocket Server it is neccesary for deploying our websocket server endpoint.
The server is created using tyrus server API.*/
public class WebSocketServer {

    public static void main(String[] args){
        runServer();
    }

    private static void runServer() {
        Server server = new Server("localhost",8025, "/websockets",
                WordgameServerEndpoint.class);

        try{
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please press a key to stop the server.");
            reader.readLine();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            server.stop();
        }
    }
}
