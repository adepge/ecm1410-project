package socialmedia;

public class Account {
    private int accountID;
    public static final int HANDLE_CHAR_LIMIT = 30;
    public static final int DESC_CHAR_LIMIT = 100;
    private String handle;
    public String description;
    public static int numberOfAccounts = 0;
    public Account(String handle){
        this.handle = handle;
        description = null;
        accountID = numberOfAccounts++;
    }
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID = numberOfAccounts++;
    }
    public int getAccountID(){
        return accountID;
    }
    public String getHandle(){
        return handle;
    }
    public void setHandle(String handle){
        this.handle = handle;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
