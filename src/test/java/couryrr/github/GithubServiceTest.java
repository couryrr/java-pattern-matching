package couryrr.github;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import couryrr.github.models.DeleteEvent;
import couryrr.github.models.GitHubEvent;
import couryrr.github.models.PullRequestEvent;

public class GithubServiceTest {
  @Test
  public void shouldFetchData() {
    var service = new GithubService();
    var body = service.fetch();
    assertTrue(!body.isEmpty());
  }

  @Test
  public void shouldMapDeleteEventData() {
    var input = """
        [{
            "id": "11723755474",
            "type": "DeleteEvent",
            "actor": {
              "id": 2702992,
              "login": "couryrr",
              "display_login": "couryrr",
              "gravatar_id": "",
              "url": "https://api.github.com/users/couryrr",
              "avatar_url": "https://avatars.githubusercontent.com/u/2702992?"
            },
            "repo": {
              "id": 662226797,
              "name": "couryrr/scripts-conf-dot",
              "url": "https://api.github.com/repos/couryrr/scripts-conf-dot"
            },
            "payload": {
              "ref": "feature/better-bashrc-setup",
              "ref_type": "branch",
              "full_ref": "refs/heads/feature/better-bashrc-setup",
              "pusher_type": "user"
            },
            "public": true,
            "created_at": "2026-05-12T01:16:15Z"
        }]
        """;
    var body = Optional.of(input);

    var service = new GithubService();

    if (body.isPresent()) {
      var events = service.map(body.get());
      assertTrue(events.get(0) instanceof DeleteEvent);
      if (events.get(0) instanceof DeleteEvent d) {
        assertEquals("11723755474", d.id());
        assertEquals("feature/better-bashrc-setup", d.payload().ref());
        assertEquals("branch", d.payload().refType());
      }
    }
  }

  @Test
  public void shouldMapPullRequestEventData() {
    var input = """
          [{
          "id": "9341021848",
          "type": "PullRequestEvent",
          "actor": {
            "id": 2702992,
            "login": "couryrr",
            "display_login": "couryrr",
            "gravatar_id": "",
            "url": "https://api.github.com/users/couryrr",
            "avatar_url": "https://avatars.githubusercontent.com/u/2702992?"
          },
          "repo": {
            "id": 662226797,
            "name": "couryrr/scripts-conf-dot",
            "url": "https://api.github.com/repos/couryrr/scripts-conf-dot"
          },
          "payload": {
            "action": "merged",
            "number": 4,
            "pull_request": {
              "url": "https://api.github.com/repos/couryrr/scripts-conf-dot/pulls/4",
              "id": 3665537377,
              "number": 4,
              "head": {
                "ref": "feature/workflow-updates",
                "sha": "7cc7031f31b6bed926606309dd8f0ac828566644",
                "repo": {
                  "id": 662226797,
                  "url": "https://api.github.com/repos/couryrr/scripts-conf-dot",
                  "name": "scripts-conf-dot"
                }
              },
              "base": {
                "ref": "main",
                "sha": "cb3bfee5df6df0adcf6f8a2629fcf2d9c29eb2d5",
                "repo": {
                  "id": 662226797,
                  "url": "https://api.github.com/repos/couryrr/scripts-conf-dot",
                  "name": "scripts-conf-dot"
                }
              }
            }
          },
          "public": true,
          "created_at": "2026-05-12T01:15:55Z"
          }]
              """;
    var body = Optional.of(input);

    var service = new GithubService();

    if (body.isPresent()) {
      var events = service.map(body.get());
      assertTrue(events.get(0) instanceof PullRequestEvent);
      if (events.get(0) instanceof PullRequestEvent pr) {
        assertEquals("9341021848", pr.id());
        assertEquals("merged", pr.payload().action());
      }
    }
  }

  @Test
  public void shouldMapMultipleEventData() {
    var input = """
        [
          {
            "id": "11723755474",
            "type": "DeleteEvent",
            "actor": {
              "id": 2702992,
              "login": "couryrr",
              "display_login": "couryrr",
              "gravatar_id": "",
              "url": "https://api.github.com/users/couryrr",
              "avatar_url": "https://avatars.githubusercontent.com/u/2702992?"
            },
            "repo": {
              "id": 662226797,
              "name": "couryrr/scripts-conf-dot",
              "url": "https://api.github.com/repos/couryrr/scripts-conf-dot"
            },
            "payload": {
              "ref": "feature/better-bashrc-setup",
              "ref_type": "branch",
              "full_ref": "refs/heads/feature/better-bashrc-setup",
              "pusher_type": "user"
            },
            "public": true,
            "created_at": "2026-05-12T01:16:15Z"
          },
          {
            "id": "9341021848",
            "type": "PullRequestEvent",
            "actor": {
              "id": 2702992,
              "login": "couryrr",
              "display_login": "couryrr",
              "gravatar_id": "",
              "url": "https://api.github.com/users/couryrr",
              "avatar_url": "https://avatars.githubusercontent.com/u/2702992?"
            },
            "repo": {
              "id": 662226797,
              "name": "couryrr/scripts-conf-dot",
              "url": "https://api.github.com/repos/couryrr/scripts-conf-dot"
            },
            "payload": {
              "action": "merged",
              "number": 4,
              "pull_request": {
                "url": "https://api.github.com/repos/couryrr/scripts-conf-dot/pulls/4",
                "id": 3665537377,
                "number": 4,
                "head": {
                  "ref": "feature/workflow-updates",
                  "sha": "7cc7031f31b6bed926606309dd8f0ac828566644",
                  "repo": {
                    "id": 662226797,
                    "url": "https://api.github.com/repos/couryrr/scripts-conf-dot",
                    "name": "scripts-conf-dot"
                  }
                },
                "base": {
                  "ref": "main",
                  "sha": "cb3bfee5df6df0adcf6f8a2629fcf2d9c29eb2d5",
                  "repo": {
                    "id": 662226797,
                    "url": "https://api.github.com/repos/couryrr/scripts-conf-dot",
                    "name": "scripts-conf-dot"
                  }
                }
              }
            },
            "public": true,
            "created_at": "2026-05-12T01:15:55Z"
          }
        ]
        """;
    var body = Optional.of(input);

    if (body.isPresent()) {
      var events = GitHubEvent.fromJson(body.get());
      for (var event : events) {
        if (event instanceof DeleteEvent d) {
          assertEquals("11723755474", d.id());
          assertEquals("feature/better-bashrc-setup", d.payload().ref());
          assertEquals("branch", d.payload().refType());
        }
        if (event instanceof PullRequestEvent pr) {
          assertEquals("9341021848", pr.id());
          assertEquals("merged", pr.payload().action());
        }

      }
    }
  }
}
