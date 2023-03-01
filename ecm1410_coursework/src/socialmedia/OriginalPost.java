public class OriginalPost extends Post {
    public OriginalPost(String message, String handle){
        this.message = message;
        PostID = numberofPosts++;
        this.handle = handle;   
    }

}