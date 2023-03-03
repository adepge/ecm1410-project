package socialmedia;
public class Comment extends Post{
    private long parentID;
    public void Comment(String handle, long PostID, String message){
        this.message = message;
        parentID = PostID;
        this.PostID = numberofPosts++;
        this.handle = handle;   
    }
    public final long getParent(){
        return parentID;
    }
}