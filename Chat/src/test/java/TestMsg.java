import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.junit.Assert;

import java.io.*;

/**
 * Created by n_buga on 10.11.16.
 */

public class TestMsg extends Assert {
    @Test
    public void testGetter() throws IOException{
        BasicConfigurator.configure();
        String nick = "abc";
        String msgString = "I u to";
        Msg msg1 = new Msg(Msg.MsgType.UsualMsg, msgString);
        Msg msg2 = new Msg(Msg.MsgType.Nickname, nick);
        assertEquals(nick, msg2.getContains());
        assertEquals(Msg.MsgType.UsualMsg, msg1.getType());
        assertEquals(msgString, msg1.getContains());
        assertEquals(Msg.MsgType.Nickname, msg2.getType());
    }
}
