package de.bierma.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.bierma.config.Config;
import de.bierma.model.Input;
import de.bierma.model.View;
import org.java_websocket.client.WebSocketClient;

import java.net.http.WebSocket;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MadnController
 *
 * @author Jannes Bierma
 * @version 1.0 - 18.12.2024
 */
public class MadnController {

    private static MadnController instance;
    private final JsonParser parser = new JsonParser();
    private View view = View.getInstance();
    private Input input = Input.getInstance();
    private boolean isPlaying;
    private String currentPlayer;
    private int currentPips;

    private MadnController() {
    }

    public static MadnController getInstance() {
        if (instance == null) {
            instance = new MadnController();
        }
        return instance;
    }

    public void handleInComingMessage(String message) {
        try {
            String action = parser.parse(message).getAsJsonObject().get("action").getAsString();
            switch (action) {
                case "error":
                    view.print(parser.parse(message).getAsJsonObject().get("message").getAsString());
                    break;
                case "sync":
                    sync(message);
                    break;
                case "chat":
                    // end
                    break;
                    case "performedAction":
                    view.print(parser.parse(message).getAsJsonObject().get("message").getAsString());
                    break;
                default:
                    view.print("Unknown action");
                    break;
            }
        } catch (Exception e) {
            view.print("Error: " + e.getMessage());
        }
    }

    public void handleInput(String playCommand, WebSocketClient client) {
        switch (playCommand) {
            case "roll":
                if(!isPlaying){
                    view.print("Not your turn");
                    break;
                }
                client.send(message("roll", ""));
                break;
            case "move":
                if(!isPlaying){
                    view.print("Not your turn");
                    break;
                }
                view.print("Enter Figure id: ");
                String id = input.readLine();
                client.send(message("move", id));
                break;
            case "chat":
                view.print("Enter Message: ");
                String message = input.readLine();
                client.send(message("char", message));
                break;
            case "place":
                if(!isPlaying){
                    view.print("Not your turn");
                    break;
                }
                client.send(message("place", ""));
                break;
            case "exit":
                client.close();
                break;
            default:
                view.print("Unknown command");
        }
    }

    private void sync(String message) {
        //Check if it is the current players turn
        currentPlayer = parser.parse(message).getAsJsonObject().get("currentPlayer").getAsString();
        isPlaying = currentPlayer.equals(Config.getUsername());
        //Current pips
        currentPips = parser.parse(message).getAsJsonObject().get("currentPips").getAsInt();
        view.print("----Gamestatus:----");
        view.print("Current Player: " + currentPlayer);
        view.print("Current Pips: " + currentPips);

        Map<Integer, String> gameBoard = parser.parse(message).getAsJsonObject().get("gameBoard").getAsJsonObject().entrySet()
                .stream()
                .filter(entry -> entry.getKey().matches("\\d+"))
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()), // Schlüssel in Integer konvertieren
                        entry -> entry.getValue() != null ? entry.getValue().toString() : null // Wert als String speichern oder null setzen
                ));
        view.print("---GameBoard:---");
        for(Map.Entry<Integer, String> entry : gameBoard.entrySet()){
            if(!entry.getValue().equals("null")){
                view.print(entry.getKey() + ":" + entry.getValue());
                continue;
            }
        }

    }

    /**
     * Create message to send to server
     * @param action action to perform
     * @param payload payload to send
     * @return String message
     */
    private String message(String action, String payload) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", action);
        jsonObject.addProperty("payload", payload);
        return jsonObject.toString();
    }
}
