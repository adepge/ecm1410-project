package socialmedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialMedia is a minimally compiling, functioning implementor of
 * the SocialMediaPlatform interface.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 03-03-2023
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
        for (Post value : Posts.values()){
            if (accountHandles.get(value.getAuthor()).getAccountID() == id ){
                try {
                    deletePost(value.getPostID());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }

    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        } else {
            accountIDs.remove(accountHandles.get(handle).getAccountID());
            accountHandles.remove(handle);
        }
        for (Post value : Posts.values()){
            if (accountHandles.get(value.getAuthor()).equals(handle) ){
                try {
                    deletePost(value.getPostID());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }
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
            accountHandles.put(newHandle,accountHandles.get(oldHandle));
            accountHandles.remove(oldHandle);
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
            Posts.put(newPost.postID,newPost);
            return newPost.postID;
        }
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        } else if (Posts.get(id) == null){
            throw new PostIDNotRecognisedException();
        } else if (Posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        } else {
            Endorsement newEndorsement = new Endorsement(handle, id);
            Posts.put(id, newEndorsement);
            return newEndorsement.getPostID();
        }
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        } else if (Posts.get(id) == null){
            throw new PostIDNotRecognisedException();
        } else if (Posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        }else if(message.length() > Post.POST_CHAR_LIMIT){
            throw new InvalidPostException();
        } else {
            Comment newComment = new Comment(handle, id, message);
            Posts.put(id, newComment);
            return newComment.getPostID();
        }
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        if (accountIDs.get(id) == null){
            throw new PostIDNotRecognisedException();
        } else {
            Posts.remove(id);
            for (Post value : Posts.values()){
                if (value instanceof Comment && ((Comment) value).getParent() == id) {
                    ((Comment) value).setParentDeleted();
                } else if (value instanceof Endorsement && ((Endorsement) value).getParent() == id){
                    Posts.remove(value.getPostID());
                }
            }
        }
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
        int counter = 0;
        for(Post value : Posts.values()){
            if (value instanceof OriginalPost){
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        int counter = 0;
        for(Post value : Posts.values()){
            if (value instanceof Endorsement){
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    public int getTotalCommentPosts() {
        int counter = 0;
        for(Post value : Posts.values()){
            if (value instanceof Comment){
                counter += 1;
            }
        }
        return counter;
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
