package socialmedia;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialMedia is a functioning implementor of
 * the SocialMediaPlatform interface.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 03-03-2023
 */
public class SocialMedia implements SocialMediaPlatform, Serializable {

    /** Key-value pair hashmap of account IDs to Account objects. */
    private Map<Integer,Account> accountIDs = new HashMap<>();

    /** Key-value pair hashmap of account handles to Account objects. */
    private Map<String,Account> accountHandles = new HashMap<>();

    /** Key-value pair hashmap of post IDs to Post objects. */
    private Map<Integer,Post> posts = new HashMap<>();

    /** Key-value pair hashmap of post IDs to the number of endorsements the post under that ID has. */
    private Map<Integer,Integer> endorsementLeaderboard = new HashMap<>();

    /** Key-value pair hashmap of account IDs to the number of endorsements the account has made. */
    private Map<String, Integer> accountEndorsements = new HashMap<>();

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,handle) || handle.contains(" ")){
            throw new InvalidHandleException();
        } else if(accountHandles.containsKey(handle)){
            throw new IllegalHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle);
            accountIDs.put(newAccount.getAccountId(),newAccount);
            accountHandles.put(handle,newAccount);
            return newAccount.getAccountId();
        }
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT, handle) || handle.contains(" ")) {
            throw new InvalidHandleException();
        } else if (accountHandles.containsKey(handle)) {
            throw new IllegalHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle, description);
            accountIDs.put(newAccount.getAccountId(),newAccount);
            accountHandles.put(handle,newAccount);
            return newAccount.getAccountId();
        }
}

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        if (!accountIDs.containsKey(id)){
            throw new AccountIDNotRecognisedException();
        }  else {
            accountHandles.remove(accountIDs.get(id).getHandle());
            accountIDs.remove(id);
        }
        for (Post value : posts.values()){
            if (accountHandles.get(value.getAuthor()).getAccountId() == id ){
                try {
                    deletePost(value.getPostId());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }

    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            accountIDs.remove(accountHandles.get(handle).getAccountId());
            accountHandles.remove(handle);
        }
        for (Post value : posts.values()){
            if (accountHandles.get(value.getAuthor()).equals(handle) ){
                try {
                    deletePost(value.getPostId());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }
        updateEndorsements();
    }


    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        if (!accountHandles.containsKey(oldHandle)) {
            throw new HandleNotRecognisedException();
        } else if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,newHandle)) {
            throw new InvalidHandleException();
        } else if (accountHandles.containsKey(newHandle)) {
            throw new IllegalHandleException();
        } else {
            accountHandles.get(oldHandle).setHandle(newHandle);
            accountHandles.put(newHandle,accountHandles.get(oldHandle));
            accountHandles.remove(oldHandle);
            for (Post value : posts.values()){
                if (value.getAuthor().equals(oldHandle)){
                    value.setAuthor(newHandle);
                }
            }
        }
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException/*,  InvalidDescriptionException */{
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            accountHandles.get(handle).setDescription(description);
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else {
            return String.format(	"ID: %1$s\n" +
                                    "Handle: %2$s\n" +
                                    "Post count: %3$s\n" +
                                    "Endorse count: %4$s",
                                    accountHandles.get(handle).getAccountId(), handle, countAccountPosts(handle), accountEndorsements.get(handle));
        }
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else if (message.length() > OriginalPost.POST_CHAR_LIMIT || message.length() < 1){
            throw new InvalidPostException();
        }
        else {
            OriginalPost newPost = new OriginalPost(handle,message);
            posts.put(newPost.getPostId(),newPost);
            endorsementLeaderboard.put(newPost.getPostId(), 0);
            return newPost.getPostId();
        }
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        } else {
            String message = String.format("EP@%1$s: %2$s", posts.get(id).getAuthor(), posts.get(id).message);
            Endorsement newEndorsement = new Endorsement(handle, id, message);
            posts.put(newEndorsement.getPostId(), newEndorsement);
            endorsementLeaderboard.put(id, endorsementLeaderboard.get(id) + 1);
            updateEndorsements();
            return newEndorsement.getPostId();
        }
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        if (!accountHandles.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        }else if(message.length() > Post.POST_CHAR_LIMIT){
            throw new InvalidPostException();
        } else {
            Comment newComment = new Comment(handle, id, message);
            posts.put(newComment.getPostId(), newComment);
            endorsementLeaderboard.put(newComment.getPostId(), 0);
            return newComment.getPostId();
        }
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else {
            posts.remove(id);
            for (Post value : posts.values()){
                if (value instanceof Comment && ((Comment) value).getParentId() == id) {
                    ((Comment) value).setParentDeleted();
                } else if (value instanceof Endorsement && ((Endorsement) value).getParentId() == id){
                    posts.remove(value.getPostId());
                    updateEndorsements();
                }
            }
        }
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (posts.get(id) instanceof Endorsement) {
            return String.format(   "ID: %1$s\n" +
                                    "Account: %2$s\n" +
                                    "No. Endorsements: %3$s | No. Comments: %3$s\n" +
                                    "%4$s",
                                    id, posts.get(id).getAuthor(), 0, posts.get(id).getMessage());
        } else {
            return String.format(   "ID: %1$s\n" +
                                    "Account: %2$s\n" +
                                    "No. Endorsements: %3$s | No. Comments: %4$s\n" +
                                    "%5$s",
                                    id, posts.get(id).getAuthor(), endorsementLeaderboard.get(id), countPostComments(id), posts.get(id).getMessage());
        }
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        }
        else if (posts.get(id) instanceof Endorsement){
            throw new NotActionablePostException();
        }
        else {
            StringBuilder postFamilyInfo = new StringBuilder();
            return findChildComments(id, postFamilyInfo, 0);
            }
    }

    @Override
    public int getNumberOfAccounts() {
        return accountHandles.size();
    }


    @Override
    public int getTotalOriginalPosts() {
        int counter = 0;
        for(Post value : posts.values()){
            if (value instanceof OriginalPost){
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        int counter = 0;
        for(Post value : posts.values()){
            if (value instanceof Endorsement){
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    public int getTotalCommentPosts() {
        int counter = 0;
        for(Post value : posts.values()){
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
                return accountHandles.get(key).getAccountId() ;
            }
        }
        return -1;
    }
    @Override
    public void erasePlatform() {
        accountIDs.clear();
        accountHandles.clear();
        posts.clear();
        endorsementLeaderboard.clear();
        accountEndorsements.clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(accountIDs);
        out.writeObject(accountHandles);
        out.writeObject(posts);
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
        posts = (Map<Integer, Post>)obj;
        endorsementLeaderboard = (Map<Integer, Integer>)obj;
        accountEndorsements = (Map<String,Integer>)obj;
        in.close();
    }

    /**
     * This method checks if input string exceeds given character limit.
     * Only returns true if string exceeds limit, or is empty.
     *
     * @param limit character limit.
     * @param input string text.
     * @return boolean if string exceeds character limit.
     */
    private boolean stringExceedsLimit(int limit, String input) {
        if (input.length() > limit || input.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method counts the number of posts the given account has made.
     *
     * @param handle account's handle.
     * @return number of posts.
     */
    private int countAccountPosts(String handle) {
        int counter = 0;
        for (Post value: posts.values()){
            if (value.getAuthor().equals(handle)){
                counter += 1;
            }
        }
        return counter;
    }

    /** This method updates the number of endorsements each account has made. */
    private void updateEndorsements() {
        accountEndorsements = new HashMap<>();
        for (Post value : posts.values()) {
            if (accountEndorsements.containsKey(value.getAuthor()) && !(value instanceof Endorsement)){
                accountEndorsements.put(value.getAuthor(), accountEndorsements.get(value.getAuthor()) + endorsementLeaderboard.get(value.getPostId()));
            } else {
                accountEndorsements.put(value.getAuthor(), endorsementLeaderboard.get(value.getPostId()));
            }
        }
    }

    /**
     * Counts the number of comments under a post
     *
     * @param id post id.
     * @return number of comments
     */
    private int countPostComments(int id) {
        int counter = 0;
        for (Post value : posts.values()){
            if (value instanceof Comment && ((Comment) value).getParentId() == id) {
                counter += 1;
            }
        }
        return counter;
    }

    /**
     * Recursively builds a formatted {@link StringBuilder} object of all children posts.
     *
     * @param id post id.
     * @return children posts.
     */
    private StringBuilder findChildComments(int id, StringBuilder postFamilyInfo, int depth) {
        try {
            if (postFamilyInfo.length() == 0) {
                postFamilyInfo.append(showIndividualPost(id));
            } else {
                String indent = " ".repeat(depth - 4);
                postFamilyInfo.append(String.format("\n%1$s|\n%1$s| > ", indent) + showIndividualPost(id).indent(depth).trim());
            }
        } catch (PostIDNotRecognisedException ignore) {}
        for (Post value : posts.values()){
            if (value instanceof Comment && ((Comment) value).getParentId() == id){
                postFamilyInfo = findChildComments(value.getPostId(), postFamilyInfo, depth+4);
            }
        }
        return postFamilyInfo;
    }

    /**
     * Gets parent {@link Post} object
     *
     * @param id child post id.
     * @return parent post.
     */
    private Post getParentPost(int id){
        Post childPost = posts.get(id);
        if(childPost instanceof Comment){
            return posts.get(((Comment) childPost).getParentId());
        } else {
            return posts.get(((Endorsement) childPost).getParentId());
        }
    }
}