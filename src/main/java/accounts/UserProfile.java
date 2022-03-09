package accounts;

public class UserProfile {
    private final String login;
    private final String passowrd;

    public UserProfile(String login, String password) {
        this.login = login;
        this.passowrd = password;
    }

    public boolean checkPasswd(String password) {
        return this.passowrd.equals(password);
    }
}
