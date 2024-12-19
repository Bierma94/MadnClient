package de.bierma.task;

import com.google.gson.JsonObject;
import de.bierma.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 * JoinRoomTask
 *
 * @author Jannes Bierma
 * @version 1.0 - 17.12.2024
 */
public class JoinRoomTask implements Task {

    private long roomId;
    private final JsonObject jsonObject = new JsonObject();

    public JoinRoomTask(long roomId) {
        this.roomId = roomId;
    }


    @Override
    public HttpRequest call() throws URISyntaxException {
        String url = Config.BASE_URL + "/api/game/join";
        jsonObject.addProperty("roomId", roomId);
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Config.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();
    }
}
