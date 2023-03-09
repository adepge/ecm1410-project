package socialmedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialMedia is a minimally compiling, but non-functioning implementor of
 * the SocialMediaPlatform interface.
 *
 * @author Diogo Pacheco
 * @version 1.0
 */
public class SocialMedia implements SocialMediaPlatform {

    /**
     * Key-value pair hashmap of account IDs to Account objects.
     */
    private Map<Integer,Account> accountIDs = new HashMap<>();

    /**
     * Key-value pair hashmap of account handles to Account objects.
     */
    private Map<String,Account> accountHandles = new HashMap<>();

    /**
     * Key-value pair hashmap of post IDs to Post objects.
     */
    private Map<Integer,Post> Posts = new HashMap<>();

    /**
     * This method checks if input string exceeds given character limit.
     * Only returns true if string exceeds limit, or is empty.
     *
     * @param limit character limit.
     * @param input string text.
     * @return boolean if string exceeds character limit.
     */
    public boolean stringExceedsLimit(int limit, String input) {
        if (input.length() > limit) {
            return true;
        } else if (input.length() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,handle) || handle.contains(" ")){
            throw new InvalidHandleException();
        } else if(accountHandles.get(handle) != null){
            throw new IllegalHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle);
            accountIDs.put(newAccount.getAccountID(),newAccount);
            accountHandles.put(handle,newAccount);
            return newAccount.getAccountID();
        }
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT, handle) || handle.contains(" ")) {
            throw new InvalidHandleException();
        } else if (accountHandles.get(handle) != null) {
            throw new IllegalHandleException();
        } else if (stringExceedsLimit(Account.DESC_CHAR_LIMIT, description)) {
            // Needs replacing
            throw new InvalidHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle, description);
            accountIDs.put(newAccount.getAccountID(),newAccount);
            accountHandles.put(handle,newAccount);
            return newAccount.getAccountID();
        }
}

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        if (accountIDs.get(id) == null){
            throw new AccountIDNotRecognisedException();
        }  else {
            accountHandles.remove(accountIDs.get(id).getHandle());
            accountIDs.remove(id);
        }

        // ADD POST DELETION FOR POSTS AND ENDORSEMENTS (LIKELY ALSO COMMENTS)
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        } else {
            accountIDs.remove(accountHandles.get(handle).getAccountID());
            accountHandles.remove(handle);
        }
        // ADD POST DELETION FOR POSTS AND ENDORSEMENTS (LIKELY ALSO COMMENTS)
    }


    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        if (accountHandles.get(oldHandle) == null) {
            throw new HandleNotRecognisedException();
        } else if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,newHandle)) {
            throw new InvalidHandleException();
        } else if (accountHandles.get(newHandle) != null) {
            throw new IllegalHandleException();
        } else {
            accountHandles.get(oldHandle).setHandle(newHandle);
        }
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException/*,  InvalidDescriptionException */{
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
//        } else if (stringExceedsLimit(Account.DESC_CHAR_LIMIT,description)) {
//            throw new InvalidDescriptionException();
        } else {
            accountHandles.get(handle).setDescription(description);
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if (accountHandles.get(handle) != null){
            String info = "ID: " + Integer.toString(accountHandles.get(handle).getAccountID()) +
                    "\n" + "Handle: " + accountHandles.get(handle).getHandle() +
                    "\n" + "Description: " + accountHandles.get(handle).getDescription() /*+
                "\n" + "Posts: "  + NUMBER OF POSTS SUBJECT HAS MADE +
                "\n" + "Endorsements: "  + NUMBER OF ENDORSEMENTS SUBJECT HAS RECEIVED*/;
            return info;
        } else {
            return null;
        }
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        }
        else if (message.length() > OriginalPost.POST_CHAR_LIMIT || message.length() < 1){
            throw new InvalidPostException();
        }
        else {
            OriginalPost newPost = new OriginalPost(handle,message);
            Posts.put(newPost.PostID,newPost);
            return newPost.PostID;
        }
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        }
        return 0;
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfAccounts() {
        return accountHandles.size();
    }


    @Override
    public int getTotalOriginalPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalCommentPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMostEndorsedPost() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMostEndorsedAccount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void erasePlatform() {
        // TODO Auto-generated method stub

    }

    @Override
    public void savePlatform(String filename) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub

    }
}
