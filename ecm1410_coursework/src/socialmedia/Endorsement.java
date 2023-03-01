public class Endorsement extends Post{
    private String parentID;
    public Endorsement(String handle, long PostID){
        referenceID = PostID;
        this.PostID = numberofPosts++;
        this.handle = handle;   
    }
}