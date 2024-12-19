package de.bierma;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.bierma.config.Config;
import de.bierma.controller.MadnController;
import de.bierma.model.Client;
import de.bierma.model.Input;
import de.bierma.model.View;
import de.bierma.task.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    private static final View view = View.getInstance();
    private static final Input input = Input.getInstance();
    static HttpClient client = HttpClient.newHttpClient();
    private static final JsonParser parser = new JsonParser();
    private static final JsonObject jsonObject = new JsonObject();
    private static String gameRoomID;
    private static String command;
    private MadnController controller = MadnController.getInstance();

    public static void main(String[] args) {
        try {
            String username = getUsername();
            if(username == null){
                Config.setToken("");
            }
            // ********** Login **********
            do {
                System.out.println("Mensch ärgere dich nicht");
                if(!Config.getToken().isEmpty()){
                    System.out.println("Logged in as: " + username);
                    Config.setUsername(username);
                    setGameRoomID();
                    break;
                }
            } while (!login());
            // ********** User is logged in **********
            do {
                view.print("Enter command: ");
                command = input.readLine();
                switch (command) {
                    case "rooms":
                        displayRooms();
                        break;
                    case "exit":
                        view.print("Goodbye");
                        break;
                    case "join":
                        joinRoom();
                        break;
                    case "self":
                        displayJoinedRoom();
                        break;
                    case "play":
                        play();
                        break;
                    case "start":
                        start();
                    default:
                        System.out.println("Unknown command");
                }
            } while (!command.equals("exit"));


        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static void start()throws Exception {
        view.print("Gametype:");
        String gameType = input.readLine();
        HttpRequest request = new StartGameTask(Long.parseLong(gameType)).call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        view.print(response.body());
    }

    private static void play() throws URISyntaxException {
        Client client = new Client(
                new URI("ws://localhost:10000/websocket/"+gameRoomID+"?token=" + Config.getToken()));
        client.connect();
        String playCommand;
        view.print("Enter PlayCommands: ");
        do {
             playCommand = input.readLine();
             MadnController.getInstance().handleInput(playCommand, client);

        } while (!playCommand.equals("exit"));
    }


    /**
     * Login
     * @return boolean true if login successful
     * @throws Exception if an error occurs
     */
    private static boolean login() throws Exception {
        view.print("Enter username: ");
        String username = input.readLine();
        view.print("Enter password: ");
        String password = input.readLine();
        HttpRequest request = new LoginTask(username, password).call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Config.setToken(parser.parse(response.body()).getAsJsonObject().get("token").getAsString());
            System.out.println("Login successful");
            return true;
        } else {
            System.out.println("Login failed");
            return false;
        }
    }

    /**
     *  Display rooms
     * @throws Exception
     */
    private static void displayRooms() throws Exception {
        GetGameRoomTask getGameRoomTask = new GetGameRoomTask();
        HttpRequest request = getGameRoomTask.call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        view.print("Rooms: ");
        view.print(response.body());
    }

    /**
     * Display joined room
     */
    private static void displayJoinedRoom() throws Exception {
        GetJoinedGameRoomTask getGameRoomTask = new GetJoinedGameRoomTask();
        HttpRequest request = getGameRoomTask.call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        view.print("Joined Room: ");
        if(response.statusCode() != 200){
            view.print("No room joined");

        }else {
            view.print(response.body());
        }
    }

    /**
     * Join room
     * @throws Exception
     */
    private static void joinRoom() throws Exception {
        view.print("Enter room id: ");
        String roomId = input.readLine();
        gameRoomID = roomId;
        HttpRequest request = new JoinRoomTask(Long.parseLong(roomId)).call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        view.print(response.body());
    }

    /**
     * Get username of logged in user
     * @return
     * @throws Exception
     */
    private static String getUsername() throws Exception {
        GetUserInformationTask getUserInformationTask = new GetUserInformationTask();
        HttpRequest request = getUserInformationTask.call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            return null;
        }
       return parser.parse(response.body())
               .getAsJsonObject().get("user")
               .getAsJsonObject().get("username").getAsString();

    }

    /**
     * Set game room id if user has joined a room
     * @throws Exception
     */
    private static void setGameRoomID() throws Exception {
        GetJoinedGameRoomTask getGameRoomTask = new GetJoinedGameRoomTask();
        HttpRequest request = getGameRoomTask.call();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
            gameRoomID = parser.parse(response.body()).getAsJsonObject().get("id").getAsString();
        }
    }

}