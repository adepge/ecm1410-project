package socialmedia;

/**
 * The Account class is used to create accounts and implements
 * methods to access an accounts' relevant identifying information
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 09-03-2023
 */
public class Account {
    /** The sequential numerical ID of the account. */
    private int accountID;

    /** Constant character limit for all account handles. */
    public static final int HANDLE_CHAR_LIMIT = 30;

    /** Constant character limit for all account descriptions */
    @Deprecated static final int DESC_CHAR_LIMIT = 100;

    /** The string handle unique to each account. */
    private String handle;

    /** Optional text description for each account. */
    private String description;

    /** The total number of Account objects created (including deleted accounts). */
    public static int numberOfAccounts = 0;

    /**
     * Constructor which creates an Account object.
     * Assigns a sequentially incrementing account ID.
     *
     * @param handle account's handle.
     */
    public Account(String handle){
        this.handle = handle;
        description = null;
        accountID = numberOfAccounts++;
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
        accountID = numberOfAccounts++;
    }

    /**
     * Delete method, superseded by the .remove() method for HashMap
     * @see SocialMedia#removeAccount(int)
     */
    @Deprecated
    public int delete(){
        accountID = -1;
        handle = "[DELETED]";
        description = null;
        return accountID;
    }

    /**
     * Getter method for {@link Account#accountID}.
     * @return account's ID.
     */
    public int getAccountID(){
        return accountID;
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
}
