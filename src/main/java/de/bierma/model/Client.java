package de.bierma.model;

import de.bierma.config.Config;
import de.bierma.config.FileLogger;
import de.bierma.controller.MadnController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Client
 *
 * @author Jannes Bierma
 * @version 1.0 - 13.12.2024
 */
public class Client extends WebSocketClient {


    private final MadnController controller = MadnController.getInstance();
    private final FileLogger logger = FileLogger.getInstance();

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.logInfo(Config.getUsername() + ": Connected to game");
    }

    @Override
    public void onMessage(String s) {
        controller.handleInComingMessage(s);
        logger.logInfo("Received message: " + s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.logInfo(Config.getUsername() + ": Disconnected from game");
    }

    @Override
    public void onError(Exception e) {
        logger.logWarning(e.getMessage());
    }

}
