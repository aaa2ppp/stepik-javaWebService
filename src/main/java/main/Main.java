package main;

import accounts.AccountService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import serverlets.MirrorServlet;
import serverlets.SignInServlet;
import serverlets.SignUpServlet;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    void run() throws Exception {
        //DriverManager.registerDriver((Driver) Class.forName("org.sqlite.jdbc.Driver").newInstance());

        try (Connection connection = getSqliteConnection()) {
            AccountService accountService = new AccountService(connection);

            MirrorServlet mirror = new MirrorServlet();
            SignUpServlet signUp = new SignUpServlet(accountService);
            SignInServlet signIn = new SignInServlet(accountService);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(new ServletHolder(mirror), "/mirror");
            context.addServlet(new ServletHolder(signUp), "/signup");
            context.addServlet(new ServletHolder(signIn), "/signin");

            Server server = new Server(8080);
            server.setHandler(context);

            server.start();
            System.err.println("Server started");
            server.join();
        }
    }

    public static Connection getSqliteConnection() {
        try {
            String url = "jdbc:sqlite:./testDB.db";
            String name = "tully";
            String pass = "tully";

            return DriverManager.getConnection(url, name, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        new Main().run();
    }
}
