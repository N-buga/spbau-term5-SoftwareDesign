import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by n_buga on 09.11.16.
 * This class create a connection. It can work as client(you need to call accept method with argument) and as
 * server(you need to call accept method without argument - it's blocking).
 * This class implements interface, so it implements methods needed for Chat interaction.
 * It can be in three different states.
 */
public class Connection implements ConnectionInterface {
    private final static Logger log = Logger.getLogger(Connection.class.getName());
    private final int PORT = 6666;
    private final Controller controller;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private State state = State.Wait;

    public enum State {Wait, Run, Close}

    public Connection(Controller controller) {
        this.controller = controller;
        log.info("Create connection");
    }

    public void connect() throws IOException {
        log.info("Try to connect. Empty arguments.");
        state = State.Wait;
        try (ServerSocket serverSocket = new ServerSocket(PORT) ) {
            serverSocket.setSoTimeout(10);
            while (socket == null && state != State.Close) {
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException ignored) {
                }
            }
            log.info("Successful connection");
            createStreams();
            log.info("Created streams");
        }
        (new Thread(this::tryToReadMsg)).start();
    }

    public void connect(String host) throws IOException {
        log.info("Try to connect. Host = " + host);
        state = State.Wait;
        socket = new Socket(host, PORT);
        log.info("Successful connection");
        createStreams();
        log.info("Created streams");
        (new Thread(this::tryToReadMsg)).start();
    }

    private void tryToReadMsg() {
        while (state == State.Run) {
            try {
                Msg msg = readMsg();
                log.info("Read msg");
                controller.printMsg(msg);
            } catch (IOException e) {
                log.error(e);
                break;
            }
        }
        try {
            close();
        } catch (IOException ignored) {
        }
        controller.restart();
    }

    public void createStreams() throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        state = State.Run;
    }

    @Override
    public void close() throws IOException {
        if (state == State.Run) {
            state = State.Close;
            in.close();
            out.close();
            socket.close();
            socket = null;
        }
        log.info("Connection closed");
    }

    public void sendMsg(Msg msg) throws IOException {
        msg.write(out);
    }

    public Msg readMsg() throws IOException {
        return (new Msg()).read(in);
    }
}
