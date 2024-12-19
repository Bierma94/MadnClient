package de.bierma.task;

import com.google.gson.JsonObject;
import de.bierma.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 * LoginTask
 *
 * @author Jannes Bierma, Dalila Rustemovic
 * @version 1.0 - 17.12.2024
 */
public class LoginTask implements Task {
    private final String username;
    private final String password;
    private final JsonObject jsonObject = new JsonObject();
    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public HttpRequest call() throws URISyntaxException {
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        String url = Config.BASE_URL + "/api/login";
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();
    }


}
