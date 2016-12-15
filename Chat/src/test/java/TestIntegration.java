import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_buga on 10.12.16.
 */
public class TestIntegration extends Assert {
    private GUI gui;
    private Controller controller;
    private MockConnection connection;
    private Msg msg1;
    private Msg msg2;

    @Before
    public void init() {
        controller = new Controller();
        gui = new GUI(controller);
        connection = new MockConnection();
        controller.setConnection(connection);
        controller.setGUI(gui);
        msg1 = new Msg("me", "Ou");
        msg2 = new Msg("me", "Ou2");
    }

    @Test
    public void testGUI() {
        Method method;
        try {
            method = GUI.class.getDeclaredMethod("sendMsg", Msg.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        method.setAccessible(true);
        try {
            method.invoke(gui, msg1);
            method.invoke(gui, msg2);
            assertEquals(connection.sentMsgs.size(), 2);
            assertEquals(connection.sentMsgs, new ArrayList<Msg>(2) {{add(msg1); add(msg2);}});
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
