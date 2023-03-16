package socialmedia;

import java.io.Serializable;

/**
 * The Post class is an abstract class which is the parent class
 * of {@link OriginalPost}, {@link Comment}, and {@link Endorsement}.
 * <p>
 * The attribute fields defined in this class belong to all posts on the platform.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 01-03-2023
 */
public abstract class Post implements Serializable {

    /** Constant character limit for all post messages. */
    static final int POST_CHAR_LIMIT = 100;

    /** The total number of Post objects created (including deleted posts). */
    static int numberOfPosts = 0;

    /** The sequential numerical ID of the post. */
    int postId;

    /** The handle which is the author of the post. */
    String author;

    /** The message which belongs to the post. */
    String message;

    /**
     * Getter method for {@link Post#postId}
     * @return post's ID.
     */
    public final int getPostId(){
        return postId;
    }

    /**
     * Getter method for {@link Post#message}
     * @return post's message.
     */
    public final String getMessage(){
        return message;
    }

    /**
     * Getter method for {@link Post#author}
     * @return author's handle.
     */
    public final String getAuthor(){
        return author;
    }

    /**
     * Setter method for {@link Post#author}
     * @param handle author's handle
     */
    public void setAuthor(String handle){this.author = handle;}
}