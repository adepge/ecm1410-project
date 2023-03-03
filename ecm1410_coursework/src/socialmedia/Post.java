package socialmedia;
public abstract class Post {
    static final int CHARACTER_LIMIT = 100;
    static long numberofPosts = 0;
    long PostID;
    String handle;
    String message;
    public final long getPostID(){
        return PostID;
    }
    public final String getMessage(){
        return message;
    }
    public final String getMessage(long PostID){
        if (PostID == this.PostID){
            return message;
        
        } else {
            return "no";
        }

    }
    public final String getHandle(){
        return handle;
    }
    public void delete(){}
    public void erase(){}
    public void save(){}
}