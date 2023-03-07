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
    private static ArrayList<Account> Accounts = new ArrayList<>();

    public int indexByHandle(String handle) throws HandleNotRecognisedException{
        if (handleExist(handle)){
            for (int i=0; i<Accounts.size(); i++){
                if (Accounts.get(i).getHandle().equals(handle)){
                    return Accounts.get(i).getAccountID();
                }
            }
        }
        throw new HandleNotRecognisedException();
    }
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
        if (handle != "[DELETED]") {
            for (int i = 0; i < Accounts.size(); i++) {
                if (Accounts.get(i).getHandle().equals(handle)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean idExist(int id){
        if (id != -1) {
            for (int i = 0; i < Accounts.size(); i++) {
                if (Accounts.get(i).getAccountID() == id) {
                    return true;
                }
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
        if (idExist(id)){
            Accounts.get(id).delete();
        }  else {
            throw new AccountIDNotRecognisedException();
        }

        // ADD POST DELETION FOR POSTS AND ENDORSEMENTS (LIKELY ALSO COMMENTS)
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (!handleExist(handle)) {
            throw new HandleNotRecognisedException();
        } else {
            Accounts.get(indexByHandle(handle)).delete();
        }
        // ADD POST DELETION FOR POSTS AND ENDORSEMENTS (LIKELY ALSO COMMENTS)
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
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException/*,  InvalidDescriptionException */{
        if (!handleExist(handle)) {
            throw new HandleNotRecognisedException();
//        } else if (stringExceedsLimit(Account.DESC_CHAR_LIMIT,description)) {
//            throw new InvalidDescriptionException();
        } else {
            for (int i = 0; i < Accounts.size(); i++) {
                if (Accounts.get(i).getHandle().equals(handle)) {
                    Accounts.get(i).setDescription(description);
                    break;
                }
            }
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        if (handleExist(handle)){
            Account subjectAccount = Accounts.get(indexByHandle(handle));
            String info = "ID: " + Integer.toString(subjectAccount.getAccountID()) +
                    "\n" + "Handle: " + subjectAccount.getHandle() +
                    "\n" + "Description: " + subjectAccount.getDescription() /*+
                "\n" + "Posts: "  + NUMBER OF POSTS SUBJECT HAS MADE +
                "\n" + "Endorsements: "  + NUMBER OF ENDORSEMENTS SUBJECT HAS RECEIVED*/;
            return info;
        } else {
            return null;
        }
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
        int counter = 0;
        for (int i = 0; i < Accounts.size(); i++) {
            if (Accounts.get(i).getAccountID() != -1) {
                counter += 1;
            }
        }
        return counter;
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
