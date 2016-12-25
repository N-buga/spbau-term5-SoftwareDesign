package ru.spbau.mit;

import ru.spbau.mit.chat_proto.ChatMsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_buga on 10.12.16.
 */
public class MockConnection implements ConnectionInterface {
    public int countCallConnection = 0;
    public int countClose = 0;
    public List<String> hosts = new ArrayList<>();
    public List<ChatMsg> sentMsgs = new ArrayList<>();

    @Override
    public void connect() throws IOException {
        countCallConnection++;
    }

    @Override
    public void connect(String host) throws IOException {
        hosts.add(host);
    }

    @Override
    public void sendMsg(ChatMsg msg) throws IOException {
        sentMsgs.add(msg);
    }

    @Override
    public void close() throws IOException {
        countClose++;
    }
}