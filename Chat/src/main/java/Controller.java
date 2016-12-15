import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by n_buga on 17.11.16.
 * This method controls interaction between Connection and GUI. It catch all exception, which appears
 * during this interaction. It keeps instances of GUI and Connection to call their methods.
 * It contains also the main method, that initialise GUI and Connection and run GUI.
 */
public class Controller {
    private static final Logger log = Logger.getLogger(Controller.class.getName());

    private GUIInterface gui;
    private ConnectionInterface connection;

    static {
        BasicConfigurator.configure();

        log.info("Log configured");
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setGUI(new GUI(controller));
        controller.setConnection(new Connection(controller));
    }

    public void sendMsg(Msg msg) {
        try {
            connection.sendMsg(msg);
        } catch (IOException e) {
            log.error(e);
            restart();
        }
    }

    public void printMsg(Msg msg) {
        gui.printMsg(msg);
    }

    public boolean createConnection(String host) {
        try {
            connection.connect(host);
        } catch (IOException e) {
            log.error(e);
            return false;
        }
        return true;
    }

    public boolean createConnection() {
        try {
            connection.connect();
        } catch (IOException e) {
            log.error(e);
            return false;
        }
        return true;
    }

    public void restart() {
        try {
            connection.close();
        } catch (IOException ignored) {
        }
        gui.restart();
    }

    public void setGUI(GUIInterface gui) {
        this.gui = gui;
    }

    public void setConnection(ConnectionInterface connection) {
        this.connection = connection;
    }
}
