import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint(value = "/game")
public class WordgameServerEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /*The @OnOpen annotation is used to annotate a method
    which will be called after WebSocket connection is opened.*/
    @OnOpen
    public void onOpen(Session session){

        logger.info("Connected ..." + session.getId());
    }

    /*The @OnMessage annotation is used to annotate
    a method which will be called each time a message is received.*/
    @OnMessage
    public String onMessage(String unscrambleWord, Session session){
        switch(unscrambleWord){
            case "start":
                logger.info("Starting the game by sending first word");
                String scrambledWord = WordRepository.getInstance().getRandomWord().getScrambledWord();
                session.getUserProperties().put("scrambleWord", scrambledWord);
                return scrambledWord;
            case "quit":
                try {
                    session.close(
                            new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                                    "Game ended"));

                }catch (IOException e){
                    throw new RuntimeException(e);
                }
                break;
        }
        String scrambleWord = (String) session.getUserProperties().get("scrambleWord");
        return checkLastWordAndSendANewWord(scrambleWord, unscrambleWord, session);

    }


    /*The @OnClose annotation is used to annotate
    a method which will be called when WebSocket connection is closed*/
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        logger.info(String.format("Session %s closed because of %s", session.getId(),
                closeReason));

    }

    private String checkLastWordAndSendANewWord (String scrambleWord, String unscrambleWord, Session session){
        WordRepository repository = WordRepository.getInstance();
        Word word = repository.getWord(scrambleWord);

        String nextScrambleWord = repository.getRandomWord().getScrambledWord();

        session.getUserProperties().put("scrambleWord", nextScrambleWord);

        String correctUnscrambleWord = word.getUnscrambbledWord();

        if(word == null || !correctUnscrambleWord.equals(unscrambleWord)){
            return String.format("You guessed it wrong. Correct answer %s. Try the next  one .. %s",
                   correctUnscrambleWord, nextScrambleWord );

        }
        return String.format("You guessed it right. Try the next word ... %s", nextScrambleWord);
    }









}
