package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("DeleteEvent") // Not strictly needed would match on name
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteEvent(String id, DeletePayload payload) implements GitHubEvent {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DeletePayload(
        String ref,
        @JsonProperty("ref_type") String refType
    ) {}
}
