public class Comment extends Post{
    private String parentID;
    public void Comment(String handle, long PostID, String message){
        this.message = message;
        referenceID = PostID;
        this.PostID = numberofPosts++;
        this.handle = handle;   
    }
    public final long getParent(){
        return parentID;
    }
}