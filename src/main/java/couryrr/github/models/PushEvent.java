package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
 
public record PushEvent(
        String id,
        Actor actor,
        Repo repo,
        Payload payload,
        @JsonProperty("public") boolean isPublic,
        @JsonProperty("created_at") Instant createdAt
) implements GitHubEvent {
 
    public record Payload(
            @JsonProperty("repository_id") long repositoryId,
            @JsonProperty("push_id") long pushId,
            String ref,
            String head,
            String before
    ) { }
}