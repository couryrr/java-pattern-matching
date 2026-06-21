package couryrr.github;
public class App {
    public static void main(String[] args) {
        GithubService service = new GithubService();
        var body = service.fetch();
        System.out.println(body);
    }
}
