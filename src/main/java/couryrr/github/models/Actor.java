package couryrr.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;
 
public record Actor(
        long id,
        String login,
        @JsonProperty("display_login") String displayLogin,
        @JsonProperty("gravatar_id") String gravatarId,
        String url,
        @JsonProperty("avatar_url") String avatarUrl
) { }