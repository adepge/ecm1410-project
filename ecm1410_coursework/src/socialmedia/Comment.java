package socialmedia;

/**
 * The Comment class is a subclass of {@link Post} which is used to
 * create Comment objects that are linked to a parent post
 * ({@link OriginalPost} or {@link Comment} object).
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 01-03-2023
 */
public class Comment extends Post{

    /** The ID of the parent post in which the comment is linked to */
    private int parentId;

    /**
     * Constructor which creates a Comment object.
     * Assigns a sequentially incrementing post ID.
     *
     * @param handle author's handle.
     * @param postID parent post ID.
     * @param message post's message.
     */
    public Comment(String handle, int postID, String message){
        this.message = message;
        parentId = postID;
        this.postId = numberOfPosts++;
        this.author = handle;
    }

    /**
     * Sets comment's parent as deleted.
     * Sets {@link Comment#parentId} = -1.
     */
    public final void setParentDeleted() {
        parentId = -1;
    }

    /**
     * Getter method for {@link Comment#parentId}
     * @return id of parent post.
     */
    public final int getParentId(){
        return parentId;
    }
}