import socialmedia.*;

import java.io.IOException;

public class SocialMediaMethodsTestApp {
    public static void main(String[] args) {
        System.out.println("System compiled successfully...");
        SocialMedia platform = new SocialMedia();
        assert (platform.getNumberOfAccounts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalOriginalPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalCommentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        assert (platform.getTotalEndorsmentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
        try {

            //Account Creation
            platform.createAccount("Germany", "The country in Europe");
            platform.createAccount("UK", "The country in Europe (but not in the EU)");
            platform.createAccount("France", "Another country in Europe");
            platform.createAccount("Netherlands");
//            platform.createAccount("Netherlands", "Yet another country in Europe");
            assert (platform.getNumberOfAccounts() == 4) : "number of accounts registered in the system does not match";


            //Original Post Creation
            int id = platform.createPost("Germany", "We are a country in Europe");
            assert (id == 0) : "post id assigned incorrectly";
            assert (platform.getTotalOriginalPosts() == 1) : "number of original posts in the system does not match";

            //Comment Creation
            platform.commentPost("France", 0, "Guten tag");
            platform.commentPost("Netherlands", 1, "(That means Hello)");
            platform.commentPost("Netherlands", 2, "...In German");
            platform.commentPost("Germany", 0, "We like beer");
            platform.commentPost("UK", 2, "you alright mate");
            assert (platform.getTotalCommentPosts() == 5) : "number of original posts in the system does not match";

//            //Mass Original Post Creation
//            platform.createPost("UK",  "Hello!");
//            for(Integer i=1;i<5000;i++){
//                platform.createPost("Germany","This is post number " + i.toString());
//            }

            //Mass Comment & Endorsement Creation
//            for(Integer i=100;i<150;i++){
//                platform.commentPost("UK",i-1, "This is also post number " + i.toString());
//                platform.endorsePost("UK",i-1);
//            }

            //Endorsement Creation
            platform.endorsePost("UK", 1);
            int endorseid = platform.endorsePost("UK", 1);
            platform.endorsePost("UK", 2);
            platform.endorsePost("UK", 2);
            assert (platform.getTotalEndorsmentPosts() == 4) : "number of endorsements in the system does not match";
            assert (platform.getMostEndorsedPost() == 1) : "most endorsed post incorrectly returned";
            assert (platform.getMostEndorsedAccount() == 2) : "most endorsed account incorrectly returned";

            //Account Modification
            platform.changeAccountHandle("Germany","Belgium");
            platform.updateAccountDescription("Belgium","Germany");
//            platform.updateAccountDescription("Belgium","");
            platform.changeAccountHandle("Belgium","Germany");
            platform.changeAccountHandle("UK","Germany");



            //I/O check
            platform.savePlatform("platform.txt");
            platform.erasePlatform();

            assert (platform.getNumberOfAccounts() == 0) : "SocialMedia platform not empty as required.";
            assert (platform.getTotalOriginalPosts() == 0) : "SocialMedia platform not empty as required.";
            assert (platform.getTotalCommentPosts() == 0) : "SocialMedia platform not empty as required.";
            assert (platform.getTotalEndorsmentPosts() == 0) : "SocialMedia platform not empty as required.";

            platform.loadPlatform("platform.txt");

            assert (platform.getNumberOfAccounts() != 0) : "SocialMedia platform did not load correctly.";
            assert (platform.getTotalOriginalPosts() != 0) : "SocialMedia platform not load correctly.";
            assert (platform.getTotalCommentPosts() != 0) : "SocialMedia platform not load correctly.";
            assert (platform.getTotalEndorsmentPosts() != 0) : "SocialMedia platform not load correctly.";

            //Post Deletion
//            platform.deletePost(4);
//            platform.deletePost(0);

            //Account Deletion
//            platform.removeAccount("UK");
//            assert (platform.getNumberOfAccounts() == 3) : "number of accounts registered in the system does not match";

            //Post formatting
//            System.out.println(platform.showIndividualPost(-1));
            System.out.println(platform.showIndividualPost(endorseid));
//            System.out.println(platform.showPostChildrenDetails(0));

            //Account Detail formatting
            System.out.println("\n" + platform.showAccount("Germany"));
            System.out.println("\n" + platform.showAccount("Netherlands"));


            //Data trackers
            System.out.println("\nThe most endorsed account id is: " + platform.getMostEndorsedAccount());
            System.out.println("The most endorsed post id is: " + platform.getMostEndorsedPost());
            System.out.println("\nTotal number of accounts on the platform: " + platform.getNumberOfAccounts());
            System.out.println("Total number of original posts on the platform: " + platform.getTotalOriginalPosts());
            System.out.println("Total number of comments on the platform: " + platform.getTotalCommentPosts());
            System.out.println("Total number of endorsements posts on the platform: " + platform.getTotalEndorsmentPosts());

            //Error catches
        } catch (IOException e) {
            assert (false) : "IOException thrown correctly";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert (false) : "ClassNotFoundException thrown correctly";
        } catch (IllegalHandleException e) {
            e.printStackTrace();
            assert (false) : "IllegalHandleException thrown correctly";
        } catch (InvalidHandleException e) {
            e.printStackTrace();
            assert (false) : "InvalidHandleException thrown correctly";
        } catch (HandleNotRecognisedException e) {
            e.printStackTrace();
            assert (false) : "HandleNotRecognised thrown correctly";
        } catch (InvalidPostException e) {
            e.printStackTrace();
            assert (false) : "InvalidPostException thrown correctly";
        } catch (PostIDNotRecognisedException e) {
            e.printStackTrace();
            assert (false) : "PostIDNotRecognised thrown correctly";
        } catch (NotActionablePostException e) {
            e.printStackTrace();
            assert (false) : "NotActionablePostException thrown correctly";
        }
    }
}
