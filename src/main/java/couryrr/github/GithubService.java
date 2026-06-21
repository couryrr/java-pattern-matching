package couryrr.github;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import tools.jackson.databind.ObjectMapper;

public class GithubService {
    private static final String API_URL = "https://api.github.com/users/couryrr/events";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public record GithubResponse(List<GitHubEvent> events, Integer count) {
    }

    public Optional<String> fetch() {
        Optional<String> body = Optional.empty();
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(API_URL)).header("Accept", "application/json").build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                body = Optional.of(response.body());
            }
        } catch (Exception e) {

        }
        return body;
    }

    public GitHubEvent map(String body) {
        return objectMapper.readValue(body, GitHubEvent.class);
    }
}
