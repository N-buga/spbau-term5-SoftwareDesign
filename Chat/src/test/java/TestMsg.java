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
        Msg msg1 = new Msg(nick, msgString);
        assertEquals(nick, msg1.getNickname());
        assertEquals(msgString, msg1.getMessage());
    }
}
