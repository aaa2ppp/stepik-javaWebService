package accounts;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Connection connection;
    //private final Map<String, UserProfile> loginToProfile = new HashMap<>();
    private final Map<String, UserProfile> sessionToProfile = new HashMap<>();

    public AccountService(Connection connection) {
        this.connection = connection;
    }

    public UserProfile getUserProfileByLogin(String login) throws SQLException {
        //return loginToProfile.get(login);

        PreparedStatement statement = connection.prepareStatement(
                "select * from users where login=?");

        try (statement) {
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                return null;
            } else {
                return new UserProfile(
                        result.getString("login"),
                        result.getString("password"));
            }
        }
    }

    public UserProfile getUserProfileBySession(String session) {
        return sessionToProfile.get(session);
    }

    public void addNewProfile(String login, String password) throws SQLException {
        //loginToProfile.put(login, new UserProfile(login, password));

        PreparedStatement statement = connection.prepareStatement(
                "insert into users (login, password) values (?, ?)");

        try (statement) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.execute();
        }
    }

    public void addNewSession(String session, UserProfile profile) {
        sessionToProfile.put(session, profile);
    }
}
