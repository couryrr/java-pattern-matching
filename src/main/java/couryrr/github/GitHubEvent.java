package couryrr.github;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public sealed interface GitHubEvent permits DeleteEvent, PullRequestEvent {
    static List<GitHubEvent> fromJson(String json) throws JacksonException {
        return new JsonMapper().readerForListOf(GitHubEvent.class).readValue(json);
    }
}