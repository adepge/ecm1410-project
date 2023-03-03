import java.util.ArrayList;
public class PostClasses {
    public static void main(String[] args) {
        OriginalPost newPost;
		newPost = new OriginalPost("Howdy", "Ben");
        System.out.println(newPost.getPostID());
        System.out.println(newPost.getHandle());
        System.out.println(newPost.getMessage());
        if(newPost instanceof OriginalPost == true) {
            System.out.println("This post is an original post");
        }		
        if(newPost instanceof Post == true) {
         System.out.println("This post is a post");
        }

    }
}
