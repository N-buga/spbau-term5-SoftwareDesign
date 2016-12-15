import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @Before
    public void init() {
        gui = new GUI(null);
        msg1 = new Msg("me", "Ou");
        msg2 = new Msg("me", "Ou2");
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
            testChatPane.printMsg(msg1);
            testChatPane.printMsg(msg2);
            assertEquals(4, panelForScroll.getComponentCount());
            List<String> contains = Arrays.stream(panelForScroll.getComponents()).map(c -> (JLabel) c)
                    .map(JLabel::getText).collect(Collectors.toList());
            List<String> needToBe = new ArrayList<String>() {{
                add(testChatPane.getInfoXML(msg1).getText());
                add(testChatPane.getMsgXML(msg1).getText());
                add(testChatPane.getInfoXML(msg2).getText());
                add(testChatPane.getMsgXML(msg2).getText());
            }};
            assertEquals(needToBe, contains);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
