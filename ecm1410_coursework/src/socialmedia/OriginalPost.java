package socialmedia;
public class OriginalPost extends Post {
    public OriginalPost(String handle,String message) {
        this.message = message;
        PostID = numberofPosts++;
        this.handle = handle;   
    }

}