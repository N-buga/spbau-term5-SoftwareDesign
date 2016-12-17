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

    public enum MsgType {
        UsualMsg,
        Nickname
    }

    private String contains;
    private MsgType type;

    public Msg() {
        log.info("Create empty msg");
    }

    public Msg(MsgType type, String contains) {
        this.type = type;
        this.contains = contains;
        log.info("Create msg: type = " + type + "; contains = " + contains);
    }

    public String getContains() {
        return contains;
    }

    public MsgType getType() {
        return type;
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeByte(type.ordinal());
        out.writeUTF(contains);
        log.info("Send message: type = " + type + "; contains = " + contains);
    }

    public Msg read(DataInputStream in) throws IOException {
        Byte intType = in.readByte();
        if (intType > 1) {
            in.readUTF();
            throw new IllegalArgumentException("Got wrong type");
        }
        type = MsgType.values()[intType];
        contains = in.readUTF();
        log.info("Read message: type = " + type + "; contains = " + contains);
        return this;

    }

}
