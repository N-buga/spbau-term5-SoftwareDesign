import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by n_buga on 03.12.16.
 * This class describes messages that send through connection.
 */
public class Msg {
    private final static Logger log = Logger.getLogger(Msg.class.getName());

    private String nickname;
    private String message;

    public Msg() {
        log.info("Create empty msg");
    }

    public Msg(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
        log.info("Create msg: nickname = " + nickname + "; message = " + message);
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(nickname);
        out.writeUTF(message);
        log.info("Send message: nickname = " + nickname + "; message = " + message);
    }

    public Msg read(DataInputStream in) throws IOException {
        nickname = in.readUTF();
        message = in.readUTF();
        log.info("Read message: nickname = " + nickname + "; message = " + message);
        return this;

    }

}
