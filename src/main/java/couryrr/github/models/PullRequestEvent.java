package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
 
@JsonTypeName("PullRequestEvent")
public record PullRequestEvent(
        String id,
        Actor actor,
        Repo repo,
        Payload payload,
        @JsonProperty("public") boolean isPublic,
        @JsonProperty("created_at") Instant createdAt
) implements GitHubEvent {
 
    public record Payload(
            String action,
            int number,
            @JsonProperty("pull_request") PullRequest pullRequest
    ) { }
 
    public record PullRequest(
            String url,
            long id,
            int number,
            Branch head,
            Branch base
    ) { }
 
    public record Branch(
            String ref,
            String sha,
            Repo repo
    ) { }
}