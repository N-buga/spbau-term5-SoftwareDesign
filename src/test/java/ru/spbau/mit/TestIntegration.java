package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.chat_proto.ChatMsg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by n_buga on 10.12.16.
 */
public class TestIntegration extends Assert {
    private GUI gui;
    private Controller controller;
    private MockConnection connection;
    private ChatMsg msg1;
    private ChatMsg msg2;
    private String nickname = "aa";
    private ChatMsg msgNickname;

    @Before
    public void init() {

        controller = new Controller();
        gui = new GUI(controller);
        connection = new MockConnection();
        controller.setConnection(connection);
        controller.setGUI(gui);
        msgNickname = ChatMsg.newBuilder().setType(ChatMsg.Type.NICK).setContains(nickname).build();
        msg1 = ChatMsg.newBuilder().setType(ChatMsg.Type.USUAL).setContains("Ou").build();
        msg2 = ChatMsg.newBuilder().setType(ChatMsg.Type.USUAL).setContains("Ou2").build();
    }

    @Test
    public void testGUI() {
        Method method;
        try {
            method = GUI.class.getDeclaredMethod("sendMsg", ChatMsg.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        method.setAccessible(true);
        try {
            method.invoke(gui, msgNickname);
            method.invoke(gui, msg1);
            method.invoke(gui, msg2);
            assertEquals(connection.sentMsgs.size(), 3);
            assertEquals(connection.sentMsgs, new ArrayList<ChatMsg>(3) {{add(msgNickname); add(msg1); add(msg2);}});
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
