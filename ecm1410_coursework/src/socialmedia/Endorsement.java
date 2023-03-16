package socialmedia;

/**
 * The Endorsement class is a subclass of {@link Post} which is used to
 * create Endorsement objects that are linked to an {@link OriginalPost} object.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 01-03-2023
 */
public class Endorsement extends Post{

    /** The ID of the parent post which is endorsed by this endorsement */
    private int parentId;

    /**
     * Constructor which creates an Endorsement object.
     * Assigns a sequentially incrementing post ID.
     *
     * @param handle author's handle.
     * @param postID parent post ID.
     */
    public Endorsement(String handle, int postID, String message){
        parentId = postID;
        this.postId = numberOfPosts++;
        this.author = handle;
        this.message = message;
    }

    /**
     * Getter method for {@link Endorsement#parentId}
     * @return id of parent post.
     */
    public final int getParentId(){
        return parentId;
    }
}