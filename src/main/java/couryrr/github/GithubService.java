package couryrr.github;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import couryrr.github.models.GitHubEvent;

public class GithubService {
    private static final String API_URL = "https://api.github.com/users/couryrr/events";

    public record GithubResponse(List<GitHubEvent> events, Integer count) {
    }

    public List<GitHubEvent> fetch() {
        List<GitHubEvent> events = new ArrayList<>();
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(API_URL)).header("Accept", "application/json").build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println(response.body());
                events = map(response.body());
            }
        } catch (Exception e) {
            System.err.println("General error: "+ e);
        }
        return events;
    }

    public List<GitHubEvent> map(String body) {
        return GitHubEvent.fromJson(body);
    }
}
