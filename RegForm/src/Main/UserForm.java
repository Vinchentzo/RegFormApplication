package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class UserForm extends JFrame{
    private JPanel userPanel;
    private JLabel userName;
    private JButton registerButton;
    private JButton btnLogout;
    private JPasswordField pfOldPass;
    private JPasswordField pfNewPass;
    private JTextField tfName;
    private JTextField tfEmail;
    private JButton btnSave;
    private JLabel userEmail;
    private JLabel actEmail;
    private JLabel actName;
    private JButton btnDelete;

    public UserForm(User user){
        setTitle("User account");
        setContentPane(userPanel);
        setMinimumSize(new Dimension(500, 429));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (user!= null){
            actName.setText(user.name);
            actEmail.setText(user.email);
            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            dispose();
        }

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeUser();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
    }

    public UserForm(){
        setTitle("User account");
        setContentPane(userPanel);
        setMinimumSize(new Dimension(500, 429));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();
        if (hasRegisteredUsers){
            LoginForm loginForm = new LoginForm();
        } else {
            RegistrationForm registrationForm = new RegistrationForm();
        }

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeUser();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
    }



    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:MySQL://localhost:3306/";
        final String DB_URL = "jdbc:MySQL://localhost:3306/regform";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);


            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS RegForm");
            statement.close();
            conn.close();

            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(200) NOT NULL,"+
                    "email VARCHAR(200) NOT NULL UNIQUE," +
                    "password VARCHAR(200) NOT NULL)";
            statement.executeUpdate(sql);

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if (numUsers>0){
                    hasRegisteredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    //ot tuk
    private void changeUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String newPassword = String.valueOf(pfNewPass.getPassword());

        if (name.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!",
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


        final String DB_URL = "jdbc:MySQL://localhost:3306/regform";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            PreparedStatement np = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
            np.setString(1, actEmail.getText());
            ResultSet rs = np.executeQuery();
            rs.next();
            String n = rs.getString("id");

            String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, newPassword);
            preparedStatement.setString(4, n);
            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }
        dispose();
        LoginForm loginForm = new LoginForm();
    }


    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private void deleteUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String newPassword = String.valueOf(pfNewPass.getPassword());

        final String DB_URL = "jdbc:MySQL://localhost:3306/regform";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            PreparedStatement np = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
            np.setString(1, actEmail.getText());
            ResultSet rs = np.executeQuery();
            rs.next();
            String n = rs.getString("id");

            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, n);

            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        dispose();
        LoginForm loginForm = new LoginForm();
    }

    public static void main(String[] args) {
        UserForm myForm = new UserForm();
    }
}
