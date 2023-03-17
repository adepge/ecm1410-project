import socialmedia.*;
public class SocialMediaMethodsTestApp {
    public static void main(String[] args) {
        System.out.println("System compiled successfully...");
        SocialMedia platform = new SocialMedia();
        try {
            platform.createAccount("Germany", "The country in Europe");
            platform.createAccount("UK", "The country in Europe (but not in the EU)");
            int id = platform.createPost("Germany", "We are a country in Europe");
            platform.commentPost("Germany", 0, "Guten tag");
            platform.commentPost("Germany", 1, "(That means Hello)");
            platform.commentPost("Germany", 2, "...In German");
            platform.commentPost("Germany", 0, "We lke beer");
            platform.commentPost("UK", 2, "you alright mate");
            platform.commentPost("UK", 1, "Hello!");
            platform.endorsePost("UK", 0);
            platform.changeAccountHandle("Germany","Belgium");
            System.out.println("Post id is:" + id);
            System.out.println(platform.showPostChildrenDetails(id));
            System.out.println(platform.showAccount("Belgium"));
        } catch (IllegalHandleException e) {
            System.out.println("This handle has already been taken");
        } catch (InvalidHandleException e) {
            System.out.println("This handle is too long!");
        } catch (HandleNotRecognisedException e) {
            System.out.println("This handle doesn't exist!");
        } catch (InvalidPostException e) {
            System.out.println("This post is too long!");
        } catch (PostIDNotRecognisedException e) {
            System.out.println("Post ID doesn't exist!");
        } catch (NotActionablePostException e) {
            System.out.println("This post is non-actionable!");
        }
    }
}
