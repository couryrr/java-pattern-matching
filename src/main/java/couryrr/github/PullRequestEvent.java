package couryrr.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

// @JsonTypeName("PullRequestEvent")
@JsonIgnoreProperties(ignoreUnknown = true)
record PullRequestEvent(String id, PullRequestPayload payload) implements GitHubEvent {
    @JsonIgnoreProperties(ignoreUnknown = true)
    record PullRequestPayload(
       String action
    ) {}
}
