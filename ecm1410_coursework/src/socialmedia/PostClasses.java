package socialmedia;
public class PostClasses {
    public static void main(String[] args) {
        SocialMedia socialmedia;
        socialmedia = new SocialMedia();
        try {
            System.out.println(socialmedia.createAccount("Adam", "Hello welcome to my account"));
            System.out.println(socialmedia.createAccount("Team", "Hello welcome to my account"));
            System.out.println(socialmedia.createAccount("Sean", "Hello welcome to my account"));
            System.out.println(socialmedia.createAccount("Ben", "Hello welcome to my account"));
            System.out.println(socialmedia.createAccount("Dan", "Hello welcome to my account"));
            socialmedia.changeAccountHandle("Tom","Thomas");
            for (int i=0;i<socialmedia.Accounts.size();i++){
                System.out.println(socialmedia.Accounts.get(i).getHandle());
                System.out.println(socialmedia.Accounts.get(i).description);
                System.out.println(socialmedia.Accounts.get(i).getAccountID());
            }
        } catch (InvalidHandleException ex) {
            System.out.println("Invalid handle");
        } catch (IllegalHandleException ex) {
            System.out.println("Handle has already been chosen");
        } catch (HandleNotRecognisedException ex) {
            System.out.println("Handle doesn't exist");
        }