package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.regex.Pattern;

public class RegistrationForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPass;
    private JTextField tfConfPass;
    private JButton btnRegister;
    private JButton btnClose;
    private JPanel regPanel;
    private JPasswordField pfPass;
    private JPasswordField pfConfPass;
    private JButton btnLogin;

    public RegistrationForm() {
        setTitle("Create a new account");
        setContentPane(regPanel);
        setMinimumSize(new Dimension(550, 474));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String password = String.valueOf(pfPass.getPassword());
        String confPassword = String.valueOf(pfConfPass.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do no match!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean emailMatch = patternMatches(email, regexPattern);

        if (!emailMatch) {
            JOptionPane.showMessageDialog(this, "Not a valid email!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = addUserToDatabase(name, email, password);

        if (user != null){
//            SendEmail send = new SendEmail("vinchentzotestemail@gmail.com", name); // this is used but gmail doesn't
                                                                                    // accept third parties from 30 mart of 2022
                                                                                    // I could use app password, but I don't have the time
            dispose();
            LoginForm loginForm = new LoginForm();

        } else {
            JOptionPane.showMessageDialog(this, "Failed to register new user.",
                    "Try again", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private User addUserToDatabase(String name, String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:MySQL://localhost:3306/regform";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, password) " +
                    "VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows>0){
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
            }

            stmt.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }
}
