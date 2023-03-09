package socialmedia;
public abstract class Post {
    static final int POST_CHAR_LIMIT = 100;
    static int numberofPosts = 0;
    int PostID;
    String handle;
    String message;
    public final int getPostID(){
        return PostID;
    }
    public final String getMessage(){
        return message;
    }
    public final String getHandle(){
        return handle;
    }
    public void delete(){}
    public void erase(){}
    public void save(){}
}