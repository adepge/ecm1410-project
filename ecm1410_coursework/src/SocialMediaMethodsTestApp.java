import socialmedia.*;

import java.io.IOException;

public class SocialMediaMethodsTestApp {
    public static void main(String[] args) {
        System.out.println("System compiled successfully...");
        SocialMedia platform = new SocialMedia();
        try {
            platform.createAccount("Germany", "The country in Europe");
            platform.createAccount("UK", "The country in Europe (but not in the EU)");
            int id = platform.createPost("Germany", "We are a country in Europe");
//            platform.commentPost("Germany", 0, "Guten tag");
//            platform.commentPost("Germany", 1, "(That means Hello)");
//            platform.commentPost("Germany", 2, "...In German");
//            platform.commentPost("Germany", 0, "We like beer");
//            platform.commentPost("UK", 2, "you alright mate");
//            platform.createPost("UK",  "Hello!");
            for(Integer i=1;i<5000;i++){
                platform.commentPost("Germany",i-1, "This is post number " + i.toString());
            }
//            for(Integer i=1;i<50;i++){
//                platform.commentPost("UK",i-1, "This is also post number " + i.toString());
//                platform.endorsePost("UK",i-1);
//            }
            int endorseid = platform.endorsePost("UK", 0);
            platform.changeAccountHandle("Germany","Belgium");
            platform.updateAccountDescription("Belgium","Home of French Fries");
//            platform.removeAccount("UK");
            platform.savePlatform("platform.txt");
            platform.erasePlatform();
            platform.loadPlatform("platform.txt");
//            platform.deletePost(4);
//            platform.deletePost(endorseid);
//            System.out.println(platform.showIndividualPost(endorseid));
//            System.out.println(platform.showPostChildrenDetails(0));
            System.out.println(platform.showAccount("Belgium"));
//            System.out.println(platform.showAccount("UK"));
            System.out.println("\nThe most endorsed account id is: " + platform.getMostEndorsedAccount());
            System.out.println("The most endorsed post id is: " + platform.getMostEndorsedPost());
            System.out.println("\nTotal number of accounts on the platform: " + platform.getNumberOfAccounts());
            System.out.println("Total number of original posts on the platform: " + platform.getTotalOriginalPosts());
            System.out.println("Total number of comments on the platform: " + platform.getTotalCommentPosts());
            System.out.println("Total number of endorsements posts on the platform: " + platform.getTotalEndorsmentPosts());
        } catch (IOException e) {
            System.out.println("Some I/O Exception has occurred");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("The Class has not been found");
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
