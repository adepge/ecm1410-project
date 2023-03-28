package socialmedia;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialMedia is a functioning implementor of
 * the SocialMediaPlatform interface.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 23-03-2023
 */
public class SocialMedia implements SocialMediaPlatform, Serializable {

    /** (Multi-)key-value pair hashmap of account handles and ids to Account objects. */
    private DualKeyMap<Integer,String,Account> accounts = new DualKeyMap<>();

    /** Key-value pair hashmap of post IDs to Post objects. */
    private Map<Integer,Post> posts = new HashMap<>();

    @Override
    public int createAccount(String handle)
            throws IllegalHandleException, InvalidHandleException {
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
    public int createAccount(String handle, String description)
            throws IllegalHandleException, InvalidHandleException {
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
    public void removeAccount(int id)
            throws AccountIDNotRecognisedException {
        if (!accounts.containsKey(id)) {
            throw new AccountIDNotRecognisedException();
        } else {
            for (Post value : posts.values()) {
                if (accounts.get(value.getAuthor()).equals(accounts.get(id))) {
                    try {
                        deletePost(value.getPostId());
                    } catch (PostIDNotRecognisedException ignored) {}
                }
            }
            accounts.remove(id);
        }
    }

    @Override
    public void removeAccount(String handle)
            throws HandleNotRecognisedException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        ArrayList<Integer> toBeDeleted = new ArrayList<>();
        for (Post value : posts.values()) {
            if (value.getAuthor().equals(handle)) {
                toBeDeleted.add(value.getPostId());
            }
        }
        for (Integer id : toBeDeleted) {
            try {
                deletePost(id);
            } catch (PostIDNotRecognisedException ignored) {
            }
        }
        accounts.remove(handle);
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
    public void updateAccountDescription(String handle, String description)
            throws HandleNotRecognisedException/*,  InvalidDescriptionException */{
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            accounts.get(handle).setDescription(description);
        }
    }

    @Override
    public String showAccount(String handle)
            throws HandleNotRecognisedException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else {
            return String.format("""
                            ID: %1$s
                            Handle: %2$s
                            Description: %3$s
                            Post count: %4$s
                            Endorse count: %5$s""",
                            accounts.get(handle).getAccountId(), handle, accounts.get(handle).getDescription(), accounts.get(handle).getPostCount(), accounts.get(handle).getEndorseCount());
        }
    }

    @Override
    public int createPost(String handle, String message)
            throws HandleNotRecognisedException, InvalidPostException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        }
        else if (stringExceedsLimit(Post.POST_CHAR_LIMIT, message)){
            throw new InvalidPostException();
        }
        else {
            OriginalPost newPost = new OriginalPost(handle,message);
            posts.put(newPost.getPostId(),newPost);
            accounts.get(handle).setPostCount(accounts.get(handle).getPostCount() + 1);
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
            posts.get(id).setEndorseCount(posts.get(id).getEndorseCount() + 1);
            accounts.get(handle).setPostCount(accounts.get(handle).getPostCount() + 1);
            accounts.get(posts.get(id).getAuthor()).setEndorseCount(accounts.get(posts.get(id).getAuthor()).getEndorseCount() + 1);
            return newEndorsement.getPostId();
        }
    }

    @Override
    public int commentPost(String handle, int id, String message)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        if (!accounts.containsKey(handle)) {
            throw new HandleNotRecognisedException();
        } else if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else if (posts.get(id) instanceof Endorsement) {
            throw new NotActionablePostException();
        } else if (stringExceedsLimit(Post.POST_CHAR_LIMIT, message)){
            throw new InvalidPostException();
        } else {
            Comment newComment = new Comment(handle, id, message);
            posts.put(newComment.getPostId(), newComment);
            posts.get(id).setCommentCount(posts.get(id).getCommentCount() + 1);
            accounts.get(handle).setPostCount(accounts.get(handle).getPostCount() + 1);
            return newComment.getPostId();
        }
    }

    @Override
    public void deletePost(int id)
            throws PostIDNotRecognisedException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else {
            accounts.get(posts.get(id).getAuthor()).setPostCount(accounts.get(posts.get(id).getAuthor()).getPostCount() - 1);
            if (posts.get(id) instanceof Comment){
                posts.get(((Comment) posts.get(id)).getParentId()).setCommentCount(posts.get(((Comment) posts.get(id)).getParentId()).getCommentCount() - 1);
            }
            else if(posts.get(id) instanceof Endorsement){
                posts.get(((Endorsement) posts.get(id)).getParentId()).setEndorseCount(posts.get(((Endorsement) posts.get(id)).getParentId()).getEndorseCount() - 1);
                accounts.get(posts.get(((Endorsement) posts.get(id)).getParentId()).getAuthor()).setEndorseCount(accounts.get(posts.get(((Endorsement) posts.get(id)).getParentId()).getAuthor()).getEndorseCount() - 1);
            }
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
    public String showIndividualPost(int id)
            throws PostIDNotRecognisedException {
        if (!posts.containsKey(id)){
            throw new PostIDNotRecognisedException();
        } else {
            return String.format("""
                            ID: %1$s
                            Account: %2$s
                            No. Endorsements: %3$s | No. Comments: %4$s
                            %5$s""",
                            id, posts.get(id).getAuthor(), posts.get(id).getEndorseCount(), posts.get(id).getCommentCount(), posts.get(id).getMessage());
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
        int highestValue = -1;  // This ensures that if there are accounts with no endorsements, the first account will be returned
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
        int highestValue = -1; // This ensures that if there are accounts with no endorsements, the first account will be returned
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
                accounts = (DualKeyMap<Integer, String, Account>) obj;
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
                postFamilyInfo.append(String.format("\n%1$s|\n%1$s| > ", indent)).append(showIndividualPost(id).indent(depth).trim());
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