package couryrr.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

// @JsonTypeName("DeleteEvent")
@JsonIgnoreProperties(ignoreUnknown = true)
record DeleteEvent(String id, DeletePayload payload) implements GitHubEvent {
    @JsonIgnoreProperties(ignoreUnknown = true)
    record DeletePayload(
        String ref,
        @JsonProperty("ref_type") String refType
    ) {}
}
