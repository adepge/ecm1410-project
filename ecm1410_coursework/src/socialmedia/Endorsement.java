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
    private int parentID;

    /**
     * Constructor which creates an Endorsement object.
     * Assigns a sequentially incrementing post ID.
     *
     * @param handle author's handle.
     * @param postID parent post ID.
     */
    public Endorsement(String handle, int postID){
        parentID = postID;
        this.postID = numberofPosts++;
        this.author = handle;
    }
}