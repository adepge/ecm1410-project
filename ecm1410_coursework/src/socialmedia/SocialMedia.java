package socialmedia;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SocialMedia is a minimally compiling, but non-functioning implementor of
 * the SocialMediaPlatform interface.
 *
 * @author Diogo Pacheco
 * @version 1.0
 */
public class SocialMedia implements SocialMediaPlatform {
    public ArrayList<Account> Accounts = new ArrayList<>();
    public boolean stringExceedsLimit(int limit, String input) {
        if (input.length() > limit) {
            return true;
        } else if (input.length() == 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean handleExist(String handle){
        for (int i=0;i < Accounts.size();i++){
            if (Accounts.get(i).getHandle().equals(handle)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,handle)){
            throw new InvalidHandleException();
        } else if(handleExist(handle)){
            throw new IllegalHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle);
            Accounts.add(newAccount);
            return newAccount.getAccountID();
        }
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT, handle)) {
            throw new InvalidHandleException();
        } else if (handleExist(handle)) {
            throw new IllegalHandleException();
        } else if (stringExceedsLimit(Account.DESC_CHAR_LIMIT, description)) {
            // Needs replacing
            throw new InvalidHandleException();
        } else {
            Account newAccount;
            newAccount = new Account(handle, description);
            Accounts.add(newAccount);
            return newAccount.getAccountID();
        }
}

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        if (!handleExist(oldHandle)) {
            throw new HandleNotRecognisedException();
        } else if (stringExceedsLimit(Account.HANDLE_CHAR_LIMIT,newHandle)) {
            throw new InvalidHandleException();
        } else if (handleExist(newHandle)) {
            throw new IllegalHandleException();
        } else {
            for(int i=0;i<Accounts.size();i++){
                if (Accounts.get(i).getHandle().equals(oldHandle)){
                    Accounts.get(i).setHandle(newHandle);
                    break;
                }
            }
        }

    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return 0;
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
