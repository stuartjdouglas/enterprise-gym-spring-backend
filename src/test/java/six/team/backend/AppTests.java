package six.team.backend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import six.team.backend.dao.UserDAO;
import six.team.backend.store.UserStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    /*
    @Test
    public void simple() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }


    private static Connection getDBConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String db = "jdbc:mysql://46.101.32.73:3306/enterprisegym";
            connection = DriverManager.getConnection(db, "admin", "admin");


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }
*/

    // requires a SPIKE - does not work ("Caused by: java.lang.ClassNotFoundException: javax.servlet.SessionCookieConfig")

    @Test
    public void insertNewUser() {

        //Connection connection = null;
        //connection = getDBConnection();

        try {
            Calendar cal = Calendar.getInstance();

            UserStore mockup = new UserStore();
            mockup.setUsername("billgates");
            mockup.setPassword("baguettes");
            mockup.setFirstname("Bill");
            mockup.setLastname("Gates");
            mockup.setGender("m");
            mockup.setEmail("bgates@ms.com");
            mockup.setContactnumber("123456");
            mockup.setCountry("UK");
            mockup.setUniversity("UoD");
            mockup.setStatus("Undergraduate");
            mockup.setSubject("CompSci");
            mockup.setYear(2015);
            mockup.setMatricnumber("123123");
            mockup.setYoung_es(1);
            mockup.setUsergroup("user");
            mockup.setMobile("077123456");
            mockup.setToken("someToken");
            mockup.setRegistration_date(new java.sql.Timestamp(cal.getTimeInMillis()));

            UserDAO userDAO = new UserDAO();

            assertTrue(userDAO.Save(mockup));
        }
        catch (Exception e)
        {
            // do something appropriate with the exception, *at least*:
            e.printStackTrace();
        }
    }


}
