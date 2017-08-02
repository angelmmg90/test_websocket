import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

@ClientEndpoint
public class WordgameClientEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @OnOpen
    public void onOpen(Session session){
        logger.info("Connected ..." + session.getId());

        try{
            session.getBasicRemote().sendText("start");
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

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

}
