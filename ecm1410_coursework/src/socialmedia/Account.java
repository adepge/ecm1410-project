package socialmedia;

import java.io.Serializable;

/**
 * The Account class is used to create accounts and implements
 * methods to access an accounts' relevant identifying information
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 09-03-2023
 */
public class Account implements Serializable {
    /** The sequential numerical id of the account. */
    private int accountId;

    /** Constant character limit for all account handles. */
    public static final int HANDLE_CHAR_LIMIT = 30;

    /** The string handle unique to each account. */
    private String handle;

    /** Optional text description for each account. */
    private String description;

    /** The total number of Account objects created (including deleted accounts). */
    static int numberOfAccounts = 0;

    /** The number of posts made by the account */
    private int postCount = 0;

    /** The number of endorsements  made by the account */
    private int endorsementCount = 0;

    /**
     * Constructor which creates an Account object.
     * Assigns a sequentially incrementing account id.
     *
     * @param handle account's handle.
     */
    public Account(String handle){
        this.handle = handle;
        description = null;
        accountId = numberOfAccounts++;
    }

    /**
     * Constructor which performs same task as {@link Account#Account(String)}.
     * Also adds an account description.
     *
     * @param handle account's handle.
     * @param description account's description.
     * @see Account#Account(String)
     */
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountId = numberOfAccounts++;
    }

    /**
     * Delete method, superseded by the .remove() method for HashMap
     * @see SocialMedia#removeAccount(int)
     */
    @Deprecated
    public int delete(){
        accountId = -1;
        handle = "[DELETED]";
        description = null;
        return accountId;
    }

    /**
     * Getter method for {@link Account#accountId}.
     * @return account's id.
     */
    public int getAccountId(){
        return accountId;
    }

    /**
     * Getter method for {@link Account#handle}.
     * @return account's handle.
     */
    public String getHandle(){
        return handle;
    }

    /**
     * Getter method for {@link Account#description}.
     * @return account's description.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Setter method for {@link Account#handle}.
     * Changes the handle for the Account object
     * which this method is invoked on.
     *
     * @param handle account's handle.
     */
    public void setHandle(String handle){
        this.handle = handle;
    }

    /**
     * Setter method for {@link Account#description}.
     * Changes the description for the Account object
     * which this method is invoked on.
     *
     * @param description account's handle.
     */
    public void setDescription(String description){
        this.description = description;
    }

    /** Increments {@link Account#postCount} by 1*/
    public void addPostCount() {postCount += 1;}

    /** Increments {@link Account#endorsementCount} by 1*/
    public void addEndorseCount() {endorsementCount += 1;}

    /**
     * Getter method for {@link Account#postCount}.
     * @return number of posts from account
     */
    public int getPostCount() {return postCount;}

    /**
     * Getter method for {@link Account#endorsementCount}.
     * @return number of endorsements from account
     */
    public int getEndorseCount() {return endorsementCount;}
}
