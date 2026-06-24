package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
 
public record CreateEvent(
        String id,
        Actor actor,
        Repo repo,
        Payload payload,
        @JsonProperty("public") boolean isPublic,
        @JsonProperty("created_at") Instant createdAt
) implements GitHubEvent {
 
    public record Payload(
            String ref,
            @JsonProperty("ref_type") String refType,
            @JsonProperty("full_ref") String fullRef,
            @JsonProperty("master_branch") String masterBranch,
            String description,
            @JsonProperty("pusher_type") String pusherType
    ) { }
}
