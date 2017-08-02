import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@ClientEndpoint
public class WordgameClientEndpoint {

    private static CountDownLatch latch;
    private Logger logger = Logger.getLogger(this.getClass().getName());


    /*When the connection is opened we send a start message to the server*/
    @OnOpen
    public void onOpen(Session session){
        logger.info("Connected ..." + session.getId());

        try{
            session.getBasicRemote().sendText("start");
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    /*The method  annotated with @OnMessage is called each time a message is received from server*/
    @OnMessage
    public String onMessage(String message, Session session){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try{
            logger.info("Received ..." + message);
            String userInput = bufferRead.readLine();
            return userInput;
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /*The method annotated with @OnClose is called when WebSocket connec
    tion is closed.*/
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
        latch.countDown();


    }

    /*In the main method we create instance of ClientManager which
    is then used to connect to Server Endpoint*/
    public static void main(String[] args){
        latch = new CountDownLatch(1);

        ClientManager client = ClientManager.createClient();
        try{
            client.connectToServer(WordgameClientEndpoint.class,
                    new URI("ws://localhost:8025/websockets/game"));
            latch.await();

        }catch (DeploymentException | URISyntaxException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

}
