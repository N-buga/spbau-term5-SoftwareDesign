package ru.spbau.mit;

import ru.spbau.mit.chat_proto.ChatMsg;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by n_buga on 03.12.16.
 * This interface describes the methods that is needed by Controller to interact with Connection.
 */
public interface ConnectionInterface extends Closeable {
    void connect() throws IOException; // Blocking
    void connect(String host) throws IOException;
    void sendMsg(ChatMsg msg) throws IOException;
}