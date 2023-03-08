package socialmedia;
public class Endorsement extends Post{
    private int parentID;
    public Endorsement(String handle, int PostID){
        parentID = PostID;
        this.PostID = numberofPosts++;
        this.handle = handle;   
    }
}