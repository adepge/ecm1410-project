package socialmedia;

import java.io.*;
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
public class SocialMedia implements SocialMediaPlatform, Serializable {

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
     * Key-value pair hashmap of post IDs to the number of endorsements the post under that ID has.
     */
    private Map<Integer,Integer> endorsementLeaderboard = new HashMap<>();

    /**
     * Key-value pair hashmap of account IDs to the number of endorsements the account has made.
     */
    private Map<String, Integer> accountEndorsements = new HashMap<>();

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
        updateEndorsements();
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
        } else {
            accountHandles.get(handle).setDescription(description);
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        }
        else {
            String info = "ID: " + Integer.toString(accountHandles.get(handle).getAccountID()) +
                    "\n" + "Handle: " + handle +
                    "\n" + "Description: " + accountHandles.get(handle).getDescription() +
                    "\n" + "Post count: "  + Integer.toString(postsByAccount(handle)) +
                    "\n" + "Endorse count: " + Integer.toString(accountEndorsements.get(handle));
            return info;
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
            Posts.put(newPost.getPostID(),newPost);
            endorsementLeaderboard.put(newPost.getPostID(), 0);
            return newPost.getPostID();
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
            Posts.put(newEndorsement.getPostID(), newEndorsement);
            endorsementLeaderboard.put(id, endorsementLeaderboard.get(id) + 1);
            updateEndorsements();
            return newEndorsement.getPostID();
        }
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        if (accountHandles.get(handle) == null) {
            throw new HandleNotRecognisedException();
        } else if (!Posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (Posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        }else if(message.length() > Post.POST_CHAR_LIMIT){
            throw new InvalidPostException();
        } else {
            Comment newComment = new Comment(handle, id, message);
            Posts.put(newComment.getPostID(), newComment);
            endorsementLeaderboard.put(newComment.getPostID(), 0);
            return newComment.getPostID();
        }
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        if (!Posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else {
            Posts.remove(id);
            for (Post value : Posts.values()){
                if (value instanceof Comment && ((Comment) value).getParent() == id) {
                    ((Comment) value).setParentDeleted();
                } else if (value instanceof Endorsement && ((Endorsement) value).getParentID() == id){
                    Posts.remove(value.getPostID());
                    updateEndorsements();
                }
            }
        }
    }



    @Override
    // "EP@" + [endorsed account handle] + ": " + [endorsed message]
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        if (!Posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (Posts.get(id) instanceof Endorsement) {
            String info = "ID: " + Integer.toString(id) +
                    "\n" + "Account: " + Posts.get(id).getAuthor() +
                    "\n" + "No. Endorsements: 0" +
                    " | No. Comments: 0" +
                    "\n" + "EP@" + Posts.get(((Endorsement) Posts.get(id)).getParentID()).getAuthor() + ": " +Posts.get(((Endorsement) Posts.get(id)).getParentID()).getMessage();
            return info;
        } else {
            String info = "ID: " + Integer.toString(id) +
                    "\n" + "Account: " + Posts.get(id).getAuthor() +
                    "\n" + "No. Endorsements: " + Integer.toString(endorsementLeaderboard.get(id)) +
                    " | No. Comments: " + Integer.toString(countPostComments(id)) +
                    "\n" + Posts.get(id).getMessage();
            return info;
        }
    }

    @Override
    /* The method builds a StringBuilder showing the details of the current post and all its children posts. The format is as follows (you can use tabs or spaces to represent indentation):
    showIndividualPost(id)
    |
    [for reply: replies to the post sorted by ID]
         |  > showIndividualPost(reply) */

    /*See an example:
  ID: 1
  Account: user1
  No. endorsements: 2 | No. comments: 3
  I like examples.
  |
  | > ID: 3
      Account: user2
      No. endorsements: 0 | No. comments: 1
      No more than me...
      |
      | > ID: 5
          Account: user1
          No. endorsements: 0 | No. comments: 1
          I can prove!
          |
          | > ID: 6
              Account: user2
              No. endorsements: 0 | No. comments: 0
              prove it
  | > ID: 4
      Account: user3
      No. endorsements: 4 | No. comments: 0
      Can't you do better than this?

  | > ID: 7
      Account: user5
      No. endorsements: 0 | No. comments: 1
      where is the example?
      |
      | > ID: 10
          Account: user1
          No. endorsements: 0 | No. comments: 0
          This is the example!

Continuing with the example, if the method is called for post ID=5 ( showIndividualPost(5)), the return would be:
  ID: 5
  Account: user1
  No. endorsements: 0 | No. comments: 1
  I can prove!
  |
  | > ID: 6
      Account: user2
      No. endorsements: 0 | No. comments: 0
      prove it*/

    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        if (!Posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        }
        else if (Posts.get(id) instanceof Endorsement){
            throw new NotActionablePostException();
        }
        else {
            StringBuilder postFamilyInfo;
            return findChildComments(id, postFamilyInfo= new StringBuilder(), 0);
            }
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
        int highestValue = 0;
        for (int value : endorsementLeaderboard.values()){
            if (value > highestValue) {
                highestValue = value;
            }
        }
        for (int key : endorsementLeaderboard.keySet()){
            if (endorsementLeaderboard.get(key) == highestValue){
                return key;
            }
        }
        return -1;
    }

    @Override
    public int getMostEndorsedAccount() {
        int highestValue = 0;
        for (int value : accountEndorsements.values()){
            if (value > highestValue) {
                highestValue = value;
            }
        }
        for (String key : accountEndorsements.keySet()){
            if (accountEndorsements.get(key) == highestValue){
                return accountHandles.get(key).getAccountID() ;
            }
        }
        return -1;
    }
    @Override
    public void erasePlatform() {
        accountIDs.clear();
        accountHandles.clear();
        Posts.clear();
        endorsementLeaderboard.clear();
        accountEndorsements.clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(accountIDs);
        out.writeObject(accountHandles);
        out.writeObject(Posts);
        out.writeObject(endorsementLeaderboard);
        out.writeObject(accountEndorsements);
        out.close();
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        Object obj = in.readObject();
        accountIDs = (Map<Integer, Account>)obj;
        accountHandles = (Map<String, Account>)obj;
        Posts = (Map<Integer, Post>)obj;
        endorsementLeaderboard = (Map<Integer, Integer>)obj;
        accountEndorsements = (Map<String,Integer>)obj;
        in.close();
    }

    /**
     * This method checks the number of posts the
     * Only returns true if string exceeds limit, or is empty.
     *
     * @param handle account's handle
     * @return number of posts
     */
    public int postsByAccount(String handle) {
        int counter = 0;
        for (Post value: Posts.values()){
            if (value.getAuthor().equals(handle)){
                counter += 1;
            }
        }
        return counter;
    }

    private void updateEndorsements() {
        accountEndorsements = new HashMap<>();
        for (Post value : Posts.values()) {
            if (accountEndorsements.containsKey(value.getAuthor()) && !(value instanceof Endorsement)){
                accountEndorsements.put(value.getAuthor(), accountEndorsements.get(value.getAuthor()) + endorsementLeaderboard.get(value.getPostID()));
            } else {
                accountEndorsements.put(value.getAuthor(), endorsementLeaderboard.get(value.getPostID()));
            }
        }
    }
    private int countPostComments(int id) {
        int counter = 0;
        for (Post value : Posts.values()){
            if (value instanceof Comment && ((Comment) value).getParent() == id) {
                counter += 1;
            }
        }
        return counter;
    }

    private StringBuilder findChildComments(int id, StringBuilder postFamilyInfo, int depth) {
        try {
            if (postFamilyInfo.length() == 0) {
                postFamilyInfo.append(showIndividualPost(id));
            } else {
                postFamilyInfo.append("\n" + " ".repeat(depth - 4) + "|\n" + " ".repeat(depth - 4) + "| > " + (showIndividualPost(id).indent(depth)).trim());
            }

        }catch (PostIDNotRecognisedException ignore) {
        }
        for (Post value : Posts.values()){
            if (value instanceof Comment && ((Comment) value).getParent() == id){
                postFamilyInfo = findChildComments(value.getPostID(), postFamilyInfo, depth+4);
            }
        }
        return postFamilyInfo;
    }
}