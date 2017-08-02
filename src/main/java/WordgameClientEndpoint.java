import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
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

}
