package six.team.backend.model;

import six.team.backend.dao.UserDAO;
import six.team.backend.store.UserStore;

import java.util.LinkedList;

/**
 * Created by remon on 14/09/2015.
 */
public class User {




    public static LinkedList<UserStore> getAll() {
        UserDAO userDAO = new UserDAO();
        return userDAO.list();
    }
}