package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("PullRequestEvent")
@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestEvent(String id, PullRequestPayload payload) implements GitHubEvent {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PullRequestPayload(
       String action
    ) {}
}
