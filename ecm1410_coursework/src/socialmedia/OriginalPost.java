package socialmedia;

/**
 * The OriginalPost class is a subclass of {@link Post} which is used to
 * create OriginalPost objects that are standalone (not linked to any parent post).
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 01-03-2023
 */
public class OriginalPost extends Post {

    /**
     * Constructor which creates an OriginalPost object.
     * Assigns a sequentially incrementing post ID.
     *
     * @param handle author's handle.
     * @param message post's message.
     */
    public OriginalPost(String handle,String message) {
        this.message = message;
        postId = numberOfPosts++;
        this.author = handle;
    }
}