package socialmedia;
public class Endorsement extends Post{
    private long parentID;
    public Endorsement(String handle, long PostID){
        parentID = PostID;
        this.PostID = numberofPosts++;
        this.handle = handle;   
    }
}