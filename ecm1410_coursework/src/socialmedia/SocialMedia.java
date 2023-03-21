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

    /** (Multi-)key-value pair hashmap of account handles and ids to Account objects. */
    private DualKeyMap<String,Integer,Account> accounts = new DualKeyMap<>();

    /** Key-value pair hashmap of post IDs to Post objects. */
    private Map<Integer,Post> posts = new HashMap<>();

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,handle) || handle.contains(" ")){
            throw new InvalidHandleException();
        } else if(accounts.containsKey(handle)){
            throw new IllegalHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle);
            accounts.put(handle, newAccount.getAccountId(), newAccount);
            return newAccount.getAccountId();
        }
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT, handle) || handle.contains(" ")) {
            throw new InvalidHandleException();
        } else if (accounts.containsKey(handle)) {
            throw new IllegalHandleException();
        } else {
            Account newAccount = new Account(handle, description);
            accounts.put(handle, newAccount.getAccountId(), newAccount);
            return newAccount.getAccountId();
        }
}

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        if (!accounts.containsKey(id)){
            throw new AccountIDNotRecognisedException();
        }  else {
            accounts.remove(id);
        }
        for (Post value : posts.values()){
            if (accounts.get(value.getAuthor()).equals(accounts.get(id))){
                try {
                    deletePost(value.getPostId());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            accounts.remove(handle);
        }
        for (Post value : posts.values()){
            if (value.getAuthor().equals(handle)){
                try {
                    deletePost(value.getPostId());
                } catch (PostIDNotRecognisedException ignored){}
            }
        }
    }


    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        if (!accounts.containsKey(oldHandle)) {
            throw new HandleNotRecognisedException();
        } else if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,newHandle)) {
            throw new InvalidHandleException();
        } else if (accounts.containsKey(newHandle)) {
            throw new IllegalHandleException();
        } else {
            accounts.get(oldHandle).setHandle(newHandle);
            int id = accounts.get(oldHandle).getAccountId();
            accounts.put(newHandle,id,accounts.get(id));
            accounts.remove(oldHandle);
            for (Post value : posts.values()){
                if (value.getAuthor().equals(oldHandle)){
                    value.setAuthor(newHandle);
                }
            }
        }
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException/*,  InvalidDescriptionException */{
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            accounts.get(handle).setDescription(description);
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else {
            Account thisAccount = accounts.get(handle);
            return String.format(	"ID: %1$s\n" +
                                    "Handle: %2$s\n" +
                                    "Post count: %3$s\n" +
                                    "Endorse count: %4$s",
                                    thisAccount.getAccountId(), handle, thisAccount.getPostCount() , thisAccount.getEndorseCount());
        }
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else if (message.length() > OriginalPost.POST_CHAR_LIMIT || message.length() < 1){
            throw new InvalidPostException();
        }
        else {
            OriginalPost newPost = new OriginalPost(handle,message);
            posts.put(newPost.getPostId(),newPost);
            accounts.get(handle).addPostCount();
            return newPost.getPostId();
        }
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        } else {
            String message = String.format("EP@%1$s: %2$s", posts.get(id).getAuthor(), posts.get(id).message);
            Endorsement newEndorsement = new Endorsement(handle, id, message);
            posts.put(newEndorsement.getPostId(), newEndorsement);
            posts.get(id).addEndorseCount();
            accounts.get(handle).addPostCount();
            accounts.get(handle).addEndorseCount();
            return newEndorsement.getPostId();
        }
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        if (!accounts.containsKey(handle)) {
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
            posts.get(id).addCommentCount();
            accounts.get(handle).addPostCount();
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
                }
            }
        }
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else {
            Post thisPost = posts.get(id);
            return String.format(   "ID: %1$s\n" +
                                    "Account: %2$s\n" +
                                    "No. Endorsements: %3$s | No. Comments: %4$s\n" +
                                    "%5$s",
                                    id, thisPost.getAuthor(), thisPost.getEndorseCount(), thisPost.getCommentCount(), thisPost.getMessage());
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
        return accounts.size();
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
        int mostEndorsedPostId = -1;
        for (Post value : posts.values()) {
            if (value.getEndorseCount() > highestValue) {
                highestValue = value.getEndorseCount();
                mostEndorsedPostId = value.getPostId();
            }
        }
        return mostEndorsedPostId;
    }

    @Override
    public int getMostEndorsedAccount() {
        int highestValue = 0;
        int mostEndorsedAccountId = -1;
        for (Account value : accounts.values()) {
            if (value.getEndorseCount() > highestValue) {
                highestValue = value.getEndorseCount();
                mostEndorsedAccountId = value.getAccountId();
            }
        }
        return mostEndorsedAccountId;
    }

    @Override
    public void erasePlatform() {
        accounts.clear();
        posts.clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(accounts);
        out.writeObject(posts);
        out.close();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        for(int i=0;i<2;i++){
            Object obj = in.readObject();
            if (obj instanceof DualKeyMap) {
                accounts = (DualKeyMap<String, Integer, Account>) obj;
            } else if (obj instanceof Map) {
                posts = (Map<Integer, Post>) obj;
            }
        }
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
        return input.length() > limit || input.length() == 0;
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
}