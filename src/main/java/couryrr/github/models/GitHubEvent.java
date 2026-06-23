package couryrr.github.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@JsonTypeInfo(defaultImpl = UnknownEvent.class, use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public sealed interface GitHubEvent permits DeleteEvent, PullRequestEvent, UnknownEvent {
    static List<GitHubEvent> fromJson(String json) throws JacksonException {
        return JsonMapper.builder().build().readerForListOf(GitHubEvent.class).readValue(json);
    }
}