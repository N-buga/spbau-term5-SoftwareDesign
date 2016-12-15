import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by n_buga on 26.10.16.
 * It is the class implements GUI. It has default pane, that is showed in any wrong situation and at the begin.
 * ChatPane is a class that describe the chat pane, that is showed then users are chatting to each other.
 * It has different constructors(for client and server mode) and method printMsg which shows the message on the screen.
 * Class implements interface GUIInterface needed to Controller work.
 */
public class GUI implements GUIInterface {

    private static final Logger log = Logger.getLogger(GUI.class.getName());
    private static final Dimension defaultScrollDimension = new Dimension(300, 50);

    private final ReentrantLock lock = new ReentrantLock();
    private final JFrame mainFrame = new JFrame("SuperChat");
    private final JRootPane defaultPane = new JRootPane();
    private final Controller controller;
    private ChatPane chatPane = null;

    {
        mainFrame.setSize(400, 500);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        log.info("Configured mainFrame");
        createDefaultPane();
    }

    public GUI(Controller controller) {
        this.controller = controller;
        paintDefaultWindow();
    }

    public class ChatPane {
        private final Logger log = Logger.getLogger(ChatPane.class.getName());

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
                controller.restart();
            });

            sendButton.addActionListener(e -> {
                String msg = yourMsg.getText();
                String nick = nickname;
                Msg newMsg = new Msg(nick, msg);
                GUI.this.printMsg(newMsg);
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

        public void printMsg(Msg msg) {
            JLabel info = getInfoXML(msg);
            JLabel newMsg = getMsgXML(msg);

            panelForScroll.add(info);
            panelForScroll.add(newMsg);

            panelForScroll.revalidate();
            panelForScroll.repaint();

            log.info("Show msg " + msg.getMessage() + " in GUI");
        }

        public JLabel getInfoXML(Msg msg) {
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();

            return new JLabel("<html> <font color = 'green'>" + msg.getNickname()
                    + "</font>: " + df.format(dateobj) + " </html>");
        }

        public JLabel getMsgXML(Msg msg) {
            return new JLabel("<html> " +  msg.getMessage().replaceAll("\n", "<br>") + " </html>");
        }
    }

    public void restart() {
        log.info("Restart");
        lock.lock();
            chatPane = null;
        lock.unlock();
        paintDefaultWindow();
    }

    public void printMsg(Msg msg) {
        lock.lock();
        if (chatPane == null) {
            lock.unlock();
            controller.restart();
        } else {
            chatPane.printMsg(msg);
            mainFrame.repaint();  //??? do i need it really?
            lock.unlock();
        }
    }

    protected void sendMsg(Msg newMsg) {
        controller.sendMsg(newMsg);
    }

    private void createDefaultPane() {
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

    private void paintDefaultWindow() {
        mainFrame.setTitle("SuperChat");
        lock.lock();
            mainFrame.setContentPane(defaultPane);
        lock.unlock();
        mainFrame.setVisible(true);

        log.info("Painted default window");
    }

    private void workInClientMode() {
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

        boolean lucky = controller.createConnection(host);
        if (!lucky) {
            JOptionPane.showMessageDialog(mainFrame, "Problem with connection!");
            controller.restart();
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

    private void workInServerMode() {
        log.info("Work in server mode");
        String nickname = JOptionPane.showInputDialog("Enter your Nickname:", "Meau");
        if (nickname == null) {
            restart();
            return;
        }

        new Thread(() -> {
            frozeMainFrame("Awaiting connection...");
            boolean lucky = controller.createConnection();
            if (lucky) {
                unfrozeMainFrame("Server Mode");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Problem with connection!");
                controller.restart();
                return;
            }

            chatPane = new ChatPane(nickname);
            JRootPane chatRootPane = chatPane.getChatPane();

            mainFrame.setContentPane(chatRootPane);
            lock.lock();
            if (chatPane != null) {
                mainFrame.setVisible(true);
            }
            lock.unlock();
            log.info("Printed ChatPane for server");
        }).start();
    }

    private void frozeMainFrame(String titleMsg) {
        mainFrame.setEnabled(false);
        mainFrame.setTitle(titleMsg);
    }

    private void unfrozeMainFrame(String titleMsg) {
        mainFrame.setEnabled(true);
        mainFrame.setTitle(titleMsg);
    }
}
