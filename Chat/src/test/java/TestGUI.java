import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by n_buga on 03.12.16.
 */
public class TestGUI extends Assert{
    private GUI gui;
    private Msg msg1;
    private Msg msg2;
    private Msg msgNickname;

    @Before
    public void init() {
        gui = new GUI(null);
        msg1 = new Msg(Msg.MsgType.UsualMsg, "Ou");
        msg2 = new Msg(Msg.MsgType.UsualMsg, "Ou2");
        msgNickname = new Msg(Msg.MsgType.Nickname, "me");
    }

    @Test
    public void testChatPane() {
        GUI.ChatPane testChatPane = gui.new ChatPane();
        Field field;
        try {
            field = GUI.ChatPane.class.getDeclaredField("panelForScroll");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        field.setAccessible(true);
        try {
            JPanel panelForScroll = (JPanel) field.get(testChatPane);
            testChatPane.setAlienNickname(msgNickname.getContains());
            testChatPane.printMsg(msgNickname.getContains(), msg1.getContains());
            testChatPane.printMsg(msgNickname.getContains(), msg2.getContains());
            assertEquals(4, panelForScroll.getComponentCount());
            List<String> contains = Arrays.stream(panelForScroll.getComponents()).map(c -> (JLabel) c)
                    .map(JLabel::getText).collect(Collectors.toList());
            List<String> needToBe = new ArrayList<String>() {{
                add(testChatPane.getInfoXML(msgNickname.getContains()).getText());
                add(testChatPane.getMsgXML(msg1.getContains()).getText());
                add(testChatPane.getInfoXML(msgNickname.getContains()).getText());
                add(testChatPane.getMsgXML(msg2.getContains()).getText());
            }};
            assertEquals(needToBe, contains);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
