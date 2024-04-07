
import java.sql.Statement;

import javax.swing.DefaultListModel;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitialDatabase {

    private ResultSet resultSet;

// Method to add a new contact to the database
public void addContact(String firstName, String lastName, String email, String phone, String company) {
    String jdbcURL = "jdbc:derby:addressbook;create=true";
    try (Connection connection = DriverManager.getConnection(jdbcURL)) {
        String sql = "INSERT INTO book (fname, lname, email, phone, company_name) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setString(4, phone);
        statement.setString(5, company);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to update an existing contact in the database
public void updateContact(String firstName, String lastName, String email, String phone, String company) {
    String jdbcURL = "jdbc:derby:addressbook;create=true";
    try (Connection connection = DriverManager.getConnection(jdbcURL)) {
        String sql = "UPDATE book SET email = ?, phone = ?, company_name = ? WHERE fname = ? AND lname = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, phone);
        statement.setString(3, company);
        statement.setString(4, firstName);
        statement.setString(5, lastName);
        int rowsUpdated = statement.executeUpdate(); // Check if any rows were updated
        if (rowsUpdated > 0) {
            System.out.println("Contact updated successfully.");
        } else {
            System.out.println("No contact found with the given first and last names.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to delete a contact from the database
public void deleteContact(String lastName) {
    String jdbcURL = "jdbc:derby:addressbook;create=true";
    try (Connection connection = DriverManager.getConnection(jdbcURL)) {
        String sql = "DELETE FROM book WHERE lname = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, lastName);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

        // Method to retrieve contacts by last name
        public DefaultListModel<String> findContactsByLastName(String lastName) {
            DefaultListModel<String> contactListModel = new DefaultListModel<>();
            String jdbcURL = "jdbc:derby:addressbook;create=true";
            try (Connection connection = DriverManager.getConnection(jdbcURL)) {
                String sql = "SELECT fname, lname FROM book WHERE lname = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, lastName);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String firstName = resultSet.getString("fname");
                    String foundLastName = resultSet.getString("lname");
                    contactListModel.addElement(foundLastName + ", " + firstName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return contactListModel;
        }

        //Method to retrieve all info of contact to display
        public DefaultListModel<String> getAllContactInfo(String lastName) {
            DefaultListModel<String> contactListModel = new DefaultListModel<>();
            String jdbcURL = "jdbc:derby:addressbook;create=true";
            try (Connection connection = DriverManager.getConnection(jdbcURL)) {
                String sql = "SELECT fname, email, phone, company_name FROM book WHERE lname = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, lastName);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String foundFName = resultSet.getString("fname");
                    String foundEmail = resultSet.getString("email");
                    String foundPhone = resultSet.getString("phone");
                    String foundCompany = resultSet.getString("company_name");
                    contactListModel.addElement(lastName +","+foundFName+","+ foundEmail+","+ foundPhone+","+ foundCompany);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return contactListModel;
        }

    public DefaultListModel<String> getContacts() {
        DefaultListModel<String> contactListModel = new DefaultListModel<>();
        String jdbcURL = "jdbc:derby:addressbook;create=true";
        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT fname, lname FROM book");
            while (resultSet.next()) {
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                contactListModel.addElement(lastName + ", " + firstName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactListModel;
    }

    // // Method to retrieve the next set of contacts
    // public DefaultListModel<String> getNextContacts() {
    //     DefaultListModel<String> contactListModel = new DefaultListModel<>();
    //     try {
    //         int count = 0;
    //         while (count < 10 && resultSet.next()) { // Retrieve next 10 contacts
    //             String firstName = resultSet.getString("fname");
    //             String lastName = resultSet.getString("lname");
    //             contactListModel.addElement(lastName + ", " + firstName);
    //             count++;
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return contactListModel;
    // }
    public DefaultListModel<String> getNextContacts() {
        DefaultListModel<String> contactListModel = new DefaultListModel<>();
        try {
            if (resultSet.next()) {
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                contactListModel.addElement(lastName + ", " + firstName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactListModel;
    }
    
    public DefaultListModel<String> getPreviousContacts() {
        DefaultListModel<String> contactListModel = new DefaultListModel<>();
        try {
            if (resultSet.previous()) {
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                contactListModel.addElement(lastName + ", " + firstName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactListModel;
    }


//----------------CONSTRUCTOR---------------------------------------------------------
    public InitialDatabase(){
        // // Load the Derby JDBC driver
        // try {
        //     Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        // } catch (ClassNotFoundException e) {
        //     System.err.println("Derby JDBC driver not found.");
        //     e.printStackTrace();
        //     return;
        // }
        String jdbcURL = "jdbc:derby:addressbook;create=true";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            System.out.println("Database has been connected!");

            Statement statement = connection.createStatement();

            // Drop existing table if it exists
            String dropSql = "DROP TABLE BOOK";
            statement.executeUpdate(dropSql);
            //create a new table in db
            String sql = "CREATE TABLE book (" +
             "id INT NOT NULL GENERATED ALWAYS AS IDENTITY," +
             "fname VARCHAR(25)," +
             "lname VARCHAR(25)," +
             "email VARCHAR(50)," + // Assuming a standard email length
             "phone VARCHAR(15)," + // Assuming phone numbers might include symbols like '+' or '-'
             "company_name VARCHAR(50)" + // Assuming a standard company name length
             ")";
            statement.executeUpdate(sql);
            System.out.println("Table created");

            //insert initial rows
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('bob', 'marley', 'bobsemail.com', '12341212', 'BOBS Comp')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('hehe', 'naynay', 'lulemail.com', '133341212', 'HEHE Comp')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('Casey', 'Mian', 'liikail.com', '00341212', 'Cas hotels')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('Dicaprio', 'Jhon', 'john.com', '9999212', 'JohnnnyComp')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('sheesh', 'mar', 'bobsemail.com', '12341212', 'BOBS Comp')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('lol', 'na', 'lulemail.com', '133341212', 'HEHE Comp')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('nope', 'Mi', 'liikail.com', '00341212', 'Cas hotels')";
            statement.executeUpdate(sql);
            sql = "Insert into book (fname, lname, email, phone, company_name) values ('Diss', 'Joe', 'john.com', '9999212', 'JohnnnyComp')";
            statement.executeUpdate(sql);
            System.out.println("row created");

            // Retrieve scrollable result set
            resultSet = statement.executeQuery("SELECT fname, lname FROM book");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


   
}


