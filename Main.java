import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize the database and populate with dummy data
        InitialDatabase initdb = new InitialDatabase();
        // Retrieve contact list model from the database
        DefaultListModel<String> contactListModel = initdb.getContacts();
        
        // Create the GUI with the contact list model
        SwingUtilities.invokeLater(() -> {
            AddressBookGUI gui = new AddressBookGUI(contactListModel, initdb);
            gui.setVisible(true);
        });
     
    }
}
