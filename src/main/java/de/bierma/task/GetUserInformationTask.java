package de.bierma.task;

import de.bierma.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 * GetUserNameTask
 *
 * @author Jannes Bierma
 * @version 1.0 - 18.12.2024
 */
public class GetUserInformationTask implements Task {

    @Override
    public HttpRequest call() throws URISyntaxException {
        String url = Config.BASE_URL + "/api/users";
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Config.getToken())
                .GET()
                .build();
    }
}
