package services;

import entities.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;


public class ServiceUser implements  IService<User>{
    Connection connection;
    public ServiceUser(){
        connection= MyDatabase.getInstance().getConnection();

    }


    @Override
    public void ajouter(User user) throws SQLException {
        // Using a prepared statement to avoid SQL injection
        String req ="insert into user (nom,prenom,email,passwd,signUpDate,role,adresse,tel)"+
                "values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(req);

        // Setting values for the prepared statement
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setTimestamp(5, new Timestamp(user.getSignupDate().getTime())); // Convert Date to Timestamp for database compatibility
        ps.setString(6, user.getRole());
        ps.setString(7, user.getAdresse());
        ps.setInt(8, user.getTel());

        // Execute the prepared statement
        ps.executeUpdate();

        System.out.println("User ajouté");
    }
    @Override
    public void modifier(User user) throws SQLException {
        String req="update user set nom=? , prenom=? ,email=? , passwd=? , signUpDate=? , role=? , adresse=? , tel=? where userID=?";
        PreparedStatement ps= connection.prepareStatement(req);
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setDate(5, new java.sql.Date(user.getSignupDate().getTime()));
        ps.setString(6, user.getRole());
        ps.setString(7, user.getAdresse());
        ps.setInt(8, user.getTel());
        ps.setInt(9, user.getId());
        ps.executeUpdate();
        System.out.println("user modifie");

    }

    @Override
    public void supprimer(int id) throws SQLException {
String req = "DELETE FROM user WHERE userID=?";
PreparedStatement ps = connection.prepareStatement(req);
ps.setInt(1,id);
ps.executeUpdate();
System.out.println("Personne supprimée");
    }

    @Override
    public List<User> afficher() throws SQLException {

        List<User> users = new ArrayList<>();
        String req = "select * from user";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("userID"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("passwd"));
                u.setRole(rs.getString("role"));
                u.setAdresse(rs.getString("adresse"));
                u.setTel(rs.getInt("tel"));

                // Convert Timestamp to Date for signupDate
                Timestamp timestamp = rs.getTimestamp("signUpDate");
                if (timestamp != null) {
                    u.setSignupDate(new Date(timestamp.getTime()));
                }

                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return users;
    }

}
