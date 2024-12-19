package de.bierma.task;

import com.google.gson.JsonObject;
import de.bierma.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 * StartGameTask
 *
 * @author Jannes Bierma, Dalila Rustemovic
 * @version 1.0 - 19.12.2024
 */
public class StartGameTask implements Task {

    private long gameType;
    private final JsonObject jsonObject = new JsonObject();

    public StartGameTask(long gameType) {
        this.gameType = gameType;
    }

    @Override
    public HttpRequest call() throws URISyntaxException {
        String url = Config.BASE_URL + "/api/game/start";
        jsonObject.addProperty("gameType", gameType);
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Config.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();
    }
}
