import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by n_buga on 09.11.16.
 */
public class Connection implements Closeable{
    private final static Logger log = Logger.getLogger(Connection.class.getName());
    private final int PORT = 6666;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private State state = State.Wait;

    public enum State {Wait, Run, Close}

    public static class Msg {
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

    public Connection() {
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
                GUI.printMsg(msg);
            } catch (IOException e) {
                log.error(e);
                break;
            }
        }
        try {
            close();
        } catch (IOException ignored) {
        }
        GUI.restart();
    }

    public void createStreams() throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        state = State.Run;
    }

    @Override
    public void close() throws IOException {
        state = State.Close;
        in.close();
        out.close();
        socket.close();
        socket = null;
        log.info("Connection closed");
    }

    public void sendMsg(Msg msg) throws IOException {
        msg.write(out);
    }

    public Msg readMsg() throws IOException {
        return (new Msg()).read(in);
    }

    public State getState() {
        return state;
    }
}
