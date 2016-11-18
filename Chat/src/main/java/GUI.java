import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

/**
 * Created by n_buga on 26.10.16.
 */
public class GUI {

    private static final Logger log = Logger.getLogger(GUI.class.getName());
    private static final ReentrantLock lock = new ReentrantLock();

    private static final JFrame mainFrame = new JFrame("SuperChat");
    private static final JRootPane defaultPane = new JRootPane();
    private static ChatPane chatPane = null;
    private static final Dimension defaultScrollDimension = new Dimension(300, 50);

    private static Connection connection;

    static {
        BasicConfigurator.configure();

        log.info("Log configured");

        mainFrame.setSize(400, 500);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        log.info("Configured mainFrame");
        createDefaultPane();
    }

    private static class ChatPane {
        private static final Logger log = Logger.getLogger(ChatPane.class.getName());

        private final JRootPane chatPane = new JRootPane();
        private final Container infoContainer = new Container();
        private final Container yourMesContainer = new Container();
        private final JPanel panelForScroll = new JPanel();
        private final JScrollPane scrollPane = new JScrollPane(panelForScroll);
        private final JTextArea yourMsg = new JTextArea();
        private final JButton returnButton = new JButton("\u25C4");
        private final JButton sendButton = new JButton("send");
        private String nickname = "?";

        {
            returnButton.addActionListener(e -> {
                try {
                    connection.close();
                } catch (IOException ignored) {
                }
                restart();
            });

            sendButton.addActionListener(e -> {
                String msg = yourMsg.getText();
                String nick = nickname;
                Connection.Msg newMsg = new Connection.Msg(nick, msg);
                GUI.printMsg(newMsg);
                sendMsg(newMsg);
            });

            log.info("Created buttons");
        }

        public ChatPane() {
            infoContainer.setLayout(new FlowLayout());
            infoContainer.add(returnButton);

            yourMesContainer.setLayout(new FlowLayout());
            ScrollPane scrollForMsg = new ScrollPane();
            scrollForMsg.setPreferredSize(defaultScrollDimension);
            scrollForMsg.add(yourMsg);
            yourMesContainer.add(scrollForMsg);
            yourMesContainer.add(sendButton);

            panelForScroll.setLayout(new BoxLayout(panelForScroll, BoxLayout.Y_AXIS));

            chatPane.setLayout(new BorderLayout());
            chatPane.add(infoContainer, BorderLayout.NORTH);
            chatPane.add(scrollPane, BorderLayout.CENTER);
            chatPane.add(yourMesContainer, BorderLayout.SOUTH);

            chatPane.setDefaultButton(sendButton);
            sendButton.requestFocus();

            log.info("Created layout");
        }

        public ChatPane(String nickname) {
            this();
            this.nickname = nickname;
            infoContainer.add(new JLabel("Your are: "));
            infoContainer.add(new JLabel("<html><font color='red'>" + nickname + "</font></html>"));

            log.info("Add nickname <" + nickname + "> to layout");
        }

        public ChatPane(String nickname, String host) {
            this(nickname);
            infoContainer.add(new JLabel("Host: "));
            infoContainer.add(new JLabel("<html><font color='green'>" + host + "</font></html>"));

            log.info("Add host name <" + host + "> to layout");
        }

        public JRootPane getChatPane() {
            return chatPane;
        }

        public void printMsg(String msg, String nick) {
            System.out.println(msg);

            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();

            JLabel info = new JLabel("<html> <font color = 'green'>" + nick + "</font>: " + df.format(dateobj) + " </html>");
            JLabel newMsg = new JLabel("<html> " +  msg.replaceAll("\n", "<br>") + " </html>");

            panelForScroll.add(info);
            panelForScroll.add(newMsg);

            panelForScroll.revalidate();
            panelForScroll.repaint();

            log.info("Show msg " + msg + " in GUI");
        }
    }

    public static void main(String[] args) {
        connection = new Connection();
        paintDefaultWindow();
    }

    public static void restart() {
        log.info("Restart");
        lock.lock();
            chatPane = null;
            if (connection.getState() == Connection.State.Run) {
                try {
                    connection.close();
                } catch (IOException ignored) {
                }
            }
        lock.unlock();
        paintDefaultWindow();
    }

    public static void printMsg(Connection.Msg msg) {
        if (chatPane == null) {
            restart();
        } else {
            chatPane.printMsg(msg.getMessage(), msg.getNickname());
            if (chatPane != null) {
                mainFrame.repaint();  //??? do i need it really?
            }
        }
    }

    private static void sendMsg(Connection.Msg newMsg) {
        try {
            connection.sendMsg(newMsg);
        } catch (IOException e) {
            log.error(e);
            JOptionPane.showMessageDialog(mainFrame, "Problem with connection!");
            restart();
        }
    }

    private static void createDefaultPane() {
        final JButton buttonServer = new JButton(
                "<html> <font color = 'blue'> Begin SuperChat in Server mode </font></html>");
        final JButton buttonClient = new JButton(
                "<html> <font color = 'red'> Begin SuperChat in Client mode </font></html>");

        buttonServer.addActionListener(e -> workInServerMode());
        buttonServer.setSize(50, 10);

        buttonClient.addActionListener(e -> workInClientMode());
        buttonClient.setSize(50, 10);

        defaultPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        defaultPane.add(buttonServer, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,0,0);
        defaultPane.add(buttonClient, gbc);

        log.info("Created default pane");
    }

    private static void paintDefaultWindow() {
        mainFrame.setTitle("SuperChat");
        lock.lock();
            mainFrame.setContentPane(defaultPane);
        lock.unlock();
        mainFrame.setVisible(true);

        log.info("Painted default window");
    }

    private static void workInClientMode() {
        log.info("Work in client mode");

        String nickname = JOptionPane.showInputDialog("Enter your Nickname:", "Meau");
        if (nickname == null) {
            restart();
            return;
        }

        String host = JOptionPane.showInputDialog("Enter server IP or host:", "localhost");
        if (host == null) {
            restart();
            return;
        }

        try {
            connection.connect(host);
        } catch (IOException e) {
            log.error(e);
            JOptionPane.showMessageDialog(mainFrame, "Problem with connection!");
            restart();
            return;
        }

        chatPane = new ChatPane(nickname, host);
        JRootPane chatRootPane = chatPane.getChatPane();

        mainFrame.setTitle("Client mode");

        mainFrame.setContentPane(chatRootPane);
        lock.lock();
            if (chatPane != null) {
                mainFrame.setVisible(true);
            }
        lock.unlock();
        log.info("Printed ChatPane for client");
    }

    private static void workInServerMode() {
        log.info("Work in server mode");
        (new Thread(() -> {
            String nickname = JOptionPane.showInputDialog("Enter your Nickname:", "Meau");
            if (nickname == null) {
                restart();
                return;
            }

            try {
                mainFrame.setEnabled(false);
                mainFrame.setTitle("Awaiting connection...");
                connection.connect();
                mainFrame.setEnabled(true);
            } catch (IOException e) {
                mainFrame.setEnabled(true);
                log.error(e);
                JOptionPane.showMessageDialog(mainFrame, "Problem with connection!");
                restart();
                return;
            }

            chatPane = new ChatPane(nickname);
            JRootPane chatRootPane = chatPane.getChatPane();

            mainFrame.setTitle("Server mode");

            mainFrame.setContentPane(chatRootPane);
            lock.lock();
            if (chatPane != null) {
                mainFrame.setVisible(true);
            }
            lock.unlock();
            log.info("Printed ChatPane for server");
        })).start();
    }
}
