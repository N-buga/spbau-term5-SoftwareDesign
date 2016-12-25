package ru.spbau.mit;

import io.grpc.*;
import io.grpc.stub.StreamObserver;
import ru.spbau.mit.chat_proto.ChatMsg;
import ru.spbau.mit.chat_proto.MessengerGrpc;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by n_buga on 24.12.16.
 */
public class MessengerConnection extends MessengerGrpc.MessengerImplBase implements ConnectionInterface {
    private static final int port = 6666;
    private static final Logger log = Logger.getLogger(MessengerConnection.class.getName());

    private CountDownLatch arrivedClient;

    private MessengerGrpc.MessengerStub asyncStub;
    private StreamObserver<ChatMsg> observer;

    private Controller controller;
    private Server server;

    public MessengerConnection(Controller controller) {
        this.controller = controller;
    }

    @Override
    public StreamObserver<ChatMsg> sendMsgs(final StreamObserver<ChatMsg> responseObserver) {
        observer = responseObserver;
        arrivedClient.countDown();
        return new StreamObserver<ChatMsg>() {
            @Override
            public void onNext(ChatMsg chatMsg) {
                controller.printMsg(chatMsg);
            }

            @Override
            public void onError(Throwable throwable) {
                log.log(Level.WARNING, "Error with connection");
                controller.problemWithConnection();
            }

            @Override
            public void onCompleted() {
                controller.restart();
            }
        };
    }

    @Override
    public void connect() throws IOException {
        ServerBuilder serverBuilder = ServerBuilder.forPort(port);
        server = serverBuilder.addService(this).build();
        arrivedClient = new CountDownLatch(1);
        server.start();
        log.info("Server started, listening on " + port);
        try {
            arrivedClient.await();
        } catch (InterruptedException e) {
            log.warning(e.fillInStackTrace().toString());
            controller.restart();
        }
    }

    @Override
    public void connect(String host) throws IOException {
        ManagedChannelBuilder managedChannelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true);
        Channel channel = managedChannelBuilder.build();
        asyncStub = MessengerGrpc.newStub(channel);
        observer =
                asyncStub.sendMsgs(new StreamObserver<ChatMsg>() {
                    @Override
                    public void onNext(ChatMsg msg) {
                        controller.printMsg(msg);
                        log.info(String.format("Got message type %s with contains %s", msg.getType().toString(),
                                msg.getContains()));
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.log(Level.WARNING, Status.fromThrowable(t).toString());
                        controller.problemWithConnection();
                    }

                    @Override
                    public void onCompleted() {
                        log.info("Finished RouteChat");
                        controller.restart();
                    }
                });
    }

    @Override
    public void sendMsg(ChatMsg msg) throws IOException {
        observer.onNext(msg);
    }

    @Override
    public void close() throws IOException {
        try {
            if (server != null) {
                server.shutdown();
            }
            observer.onCompleted();
        } catch (StatusRuntimeException | IllegalStateException ignored) {}
    }
}
