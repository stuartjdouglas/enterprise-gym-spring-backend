package six.team.backend.dao;



import six.team.backend.controller.RegisterController;
import six.team.backend.model.User;
import six.team.backend.store.UserLoginStore;
import six.team.backend.store.UserStore;
import six.team.backend.store.*;
import six.team.backend.utils.Config;


import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.Date;
//import java.sql.Date;

public class UserDAO {

    public int Save(UserStore userStore) {
        UserStore userStoreSave = new UserStore();
        Connection connection = null;
        int result = 3;

        try {
            connection = Config.getDBConnection();

            // get a resuls set with all usernames
            PreparedStatement psUsernameChecker = connection.prepareStatement("select * from Users where username = ?");
            psUsernameChecker.setString(1, userStore.getUsername());
            ResultSet rsUsernameChecker = psUsernameChecker.executeQuery();

            // get a resuls set with all emails
            PreparedStatement psEmailChecker = connection.prepareStatement("select * from Users where email = ?");
            psEmailChecker.setString(1, userStore.getEmail());
            ResultSet rsEmailChecker = psEmailChecker.executeQuery();

            Boolean usernameExists = false;
            Boolean emailExists = false;

            // check if the username or the email have been already used
            while (rsUsernameChecker.next()) {
                if (rsUsernameChecker.getString("username").equals(userStore.getUsername())) {
                    usernameExists = true;
                }
            }

            while (rsEmailChecker.next()) {
                if (rsEmailChecker.getString("email").equals(userStore.getEmail())) {
                    emailExists = true;
                }
            }

            if (!usernameExists && !emailExists) {

                // not in the DB yet - , activated, token, registration_date
                PreparedStatement ps = connection.prepareStatement("INSERT INTO Users (username, password, firstname, lastname, gender, email, contactnumber, country, university, status, subject, matricnumber, young_es, usergroup, token, registration_date, yearofstudy, bio ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
                // for the timestamp
                Calendar cal = Calendar.getInstance();

                ps.setString(1, userStore.getUsername());
                ps.setString(2, userStore.getPassword());
                ps.setString(3, userStore.getFirstname());
                ps.setString(4, userStore.getLastname());
                ps.setString(5, userStore.getGender());

                ps.setString(6, userStore.getEmail());
                ps.setString(7, userStore.getContactnumber());
                ps.setString(8, userStore.getCountry());
                ps.setString(9, userStore.getUniversity());
                ps.setString(10, userStore.getStatus());

                ps.setString(11, userStore.getSubject());
                ps.setString(12, userStore.getMatricnumber());
                ps.setInt(13, userStore.getYoung_es());
                ps.setString(14, "unauthorised");
                ps.setString(15, "testToken");
                ps.setTimestamp(16, (new java.sql.Timestamp(cal.getTimeInMillis())));
                ps.setInt(17, userStore.getYearofstudy());
                ps.setString(18, userStore.getBio());
                ps.executeUpdate();
                RegisterController registerController = new RegisterController();
                result = 0;
            } else
                if(usernameExists){
                    result=  1;
                }else if (emailExists) {
                    result = 2;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        return result;
    }


    public void Update(UserStore userStore) {

    }



    /*public UserStore get(int userId) {
        UserStore userStore = new UserStore();
        Connection connection = null;

        try {
            connection = getDBConnection();

            PreparedStatement ps = connection.prepareStatement("select userid, username from Users where id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userStore.setId(rs.getInt("userid"));
                userStore.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }

        }

        return userStore;
    }*/

    public UserInfoStore getUserInfo (String userName) {
        UserInfoStore userInfoStore = new UserInfoStore();
        Connection connection = null;

        try {
            connection = Config.getDBConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT userid, bio, username, firstname, lastname, gender, email, contactnumber, country, " +
                    "university, status, subject, yearofstudy, matricnumber, usergroup, young_es, registration_date FROM Users WHERE username = ?");

            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userInfoStore.setId(rs.getInt("userid"));
                userInfoStore.setUsername(rs.getString("username"));
                userInfoStore.setFirstName(rs.getString("firstname"));
                userInfoStore.setLastName(rs.getString("lastname"));
                userInfoStore.setEmail(rs.getString("email"));
                userInfoStore.setCountry(rs.getString("country"));
                userInfoStore.setUniversity(rs.getString("university"));
                userInfoStore.setStatus(rs.getString("status"));
                userInfoStore.setDegreeSubject(rs.getString("subject"));
                userInfoStore.setContactNo(rs.getString("contactnumber"));
                userInfoStore.setYearOfStudy(rs.getInt("yearofstudy"));
                userInfoStore.setMmtricNo(rs.getString("matricnumber"));
                userInfoStore.setUserGroup("Students");
                userInfoStore.setGender(rs.getString("gender"));
                userInfoStore.setRegDate(rs.getDate("registration_date"));
                userInfoStore.setYoung_e_s(Integer.parseInt(rs.getString("young_es")));
                userInfoStore.setBio(rs.getString("bio"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }

        }

        return userInfoStore;
    }

    public boolean userCheck(String username) {
        Connection connection = null;
        boolean found = false;
        try {
            connection = Config.getDBConnection();

            PreparedStatement ps = connection.prepareStatement("select userid from Users where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next() == true) {
                found = true;
            } else {
                found = false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
            return found;
        }
    }

    public LinkedList<UserInfoStore> list(){
        LinkedList<UserInfoStore> users = new LinkedList<UserInfoStore>();
        Connection connection = null;

        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("select userid, username, firstname, lastname, gender, email, contactnumber, country, university," +
                    " status, subject, yearofstudy, matricnumber, usergroup, young_es, registration_date from Users");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserInfoStore userInfoStore = new UserInfoStore();
                userInfoStore.setId(rs.getInt("userid"));
                userInfoStore.setUsername(rs.getString("username"));
                userInfoStore.setFirstName(rs.getString("firstname"));
                userInfoStore.setLastName(rs.getString("lastname"));
                userInfoStore.setContactNo(rs.getString("contactnumber"));
                userInfoStore.setEmail(rs.getString("email"));
                userInfoStore.setCountry(rs.getString("country"));
                userInfoStore.setUniversity(rs.getString("university"));
                userInfoStore.setStatus(rs.getString("status"));
                userInfoStore.setDegreeSubject(rs.getString("subject"));
                userInfoStore.setYearOfStudy(rs.getInt("yearofstudy"));
                userInfoStore.setMmtricNo(rs.getString("matricnumber"));
                userInfoStore.setUserGroup("Students");
                userInfoStore.setGender(rs.getString("gender"));
                userInfoStore.setRegDate(rs.getDate("registration_date"));
                userInfoStore.setYoung_e_s(Integer.parseInt(rs.getString("young_es")));
                users.add(userInfoStore);
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }

        }
        return users;
    }

    public LinkedList<UserStore> unauthorisedList(){
        LinkedList<UserStore> users = new LinkedList<UserStore>();
        Connection connection = null;

        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("select userid, username, usergroup from Users where usergroup = ?");
            ps.setString(1, "unauthorised");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserStore user = new UserStore();
                user.setId(rs.getInt("userid"));
                user.setUsername(rs.getString("username"));
                //user.setUsergroup(rs.getString("usergroup"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }

        }
        return users;
    }

    public boolean resetPassword(String username,String newpassword)
    {
        Connection connection = null;
        boolean success=false;
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("update Users set password=? where username=?");
            ps.setString(1, newpassword);
            ps.setString(2, username);
            ps.executeUpdate();
            success=true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            success=false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }

        }
        return success;
    }

    public UserLoginStore verifyUser(String username, String password){
        Connection connection = null;
        UserLoginStore user = new UserLoginStore();
        user.setMessage("LoginFailed");
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT* FROM Users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs != null) {
                    String passwordUser = rs.getString("password");
                    if (password.equals(passwordUser)) {
                        String randomToken = UUID.randomUUID().toString();
                        randomToken = randomToken.replaceAll("-", "");
                        PreparedStatement ps1 = connection.prepareStatement("UPDATE Users SET token=? WHERE username=?");
                        ps1.setString(1, randomToken);
                        ps1.setString(2, username);
                        int rs1 = ps1.executeUpdate();
                        user.setToken(randomToken);
                        user.setUsergroup(rs.getString("usergroup"));
                        user.setMessage("Login Success");
                        if(rs.getString("usergroup").equals("unauthorised")){
                            user.setMessage("Unapproved");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        return user;
    }

    public String getUserGroup(String token){
        Connection connection = null;
        String userGroup = "";
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT usergroup FROM Users WHERE token=?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs != null) {
                    userGroup = rs.getString("usergroup");
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }

        return userGroup;
    }


    public Boolean getUserGroupPermissions(String userGroup, String columnName){
        Connection connection = null;
        int permissionGranted = 0;
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT " + columnName + " FROM RolePermissions WHERE usergroup = ?");
            ps.setString(1, userGroup);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs != null) {
                    permissionGranted = rs.getInt(columnName);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        if(permissionGranted == 1){
            return true;
        }else {
            return false;
        }
    }

    public String getUserName(String token){
        Connection connection = null;
        String userName = "";
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT username FROM Users WHERE token=?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs != null) {
                    userName = rs.getString("username");
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        return userName;
    }

    public int getUserID(String token){
        Connection connection = null;
        int userID = 0;
        try {
            connection = Config.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT userid FROM Users WHERE token=?");
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs != null) {
                    userID = rs.getInt("userid");
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        return userID;
    }

    public boolean approveUser(int user_id,String user_group){
        Connection connection;
        connection = Config.getDBConnection();

        try {

            PreparedStatement ps = connection.prepareStatement("update Users set usergroup=? where userid = ?");
            ps.setString(1, "Students");
            ps.setInt(2, user_id);
            ps.executeUpdate();
            return true;
        }
        catch(SQLException e){
            System.out.print(e.getMessage());
            return false;
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }

    }

    public boolean deleteUser(int user_id)
    {
        Connection connection;
        connection = Config.getDBConnection();
        try {


                PreparedStatement ps = connection.prepareStatement("delete from Points where user_id = ?");
                ps.setInt(1, user_id);
                ps.executeUpdate();
                PreparedStatement ps2 = connection.prepareStatement("delete from Users where userid = ?");
                ps2.setInt(1, user_id);
                ps2.executeUpdate();
            return true;

        }
        catch(SQLException e){
            System.out.print(e.getMessage());
            return false;
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }

    }

    public boolean updateUser(UserStore user)
    {
        Connection connection;
        connection = Config.getDBConnection();
        boolean success=false;
        try {

            PreparedStatement ps = connection.prepareStatement("update Users set username=?, firstname=?, lastname=?, gender=?, email=?,contactnumber=?, country=?, university=?, status=?, subject=?, matricnumber=?,young_es=?,yearofstudy=?, bio=? where userid = ?");
            ps.setString(1, user.getUsername());
//            ps.setString(2, user.getPassword());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastname());
            ps.setString(4, user.getGender());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getContactnumber());
            ps.setString(7, user.getCountry());
            ps.setString(8, user.getUniversity());
            ps.setString(9, user.getStatus());
            ps.setString(10, user.getSubject());
            ps.setString(11, user.getMatricnumber());
            ps.setInt(12, user.getYoung_es());
//            ps.setString(13, "unauthorised");
            ps.setInt(13, user.getYearofstudy());
            ps.setString(14, user.getBio());
            ps.setInt(15, user.getId());
            System.out.println(ps.executeUpdate());
            success=true;
        }
        catch(SQLException e){
            System.out.print(e.getMessage());
            success= false;
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                //failed to close connection
                System.err.println(e.getMessage());
            }
        }
        return success;
    }
}
