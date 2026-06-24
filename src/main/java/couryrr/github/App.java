package couryrr.github;

import couryrr.github.models.CreateEvent;
import couryrr.github.models.DeleteEvent;
import couryrr.github.models.PullRequestEvent;
import couryrr.github.models.PushEvent;
import couryrr.github.models.UnknownEvent;

public class App {
    public static void main(String[] args) {
        GithubService service = new GithubService();
        var ghEvents = service.fetch();

        for (var event : ghEvents) {
            switch (event) {
                case CreateEvent ce -> System.out.printf("Created %s \n", ce.payload().masterBranch());
                case DeleteEvent de -> System.out.printf("Deleted %s \n", de.payload().ref());
                case PullRequestEvent pre -> System.out.printf("Pull Request Event %s \n", pre.payload().pullRequest().url());
                case PushEvent pe -> System.out.printf("Push Event %s \n", pe.payload().ref());
                case UnknownEvent ue -> System.out.printf("Unknown stuff %s \n", ue.type());
            }

        }
    }
}
