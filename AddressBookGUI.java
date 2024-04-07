
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AddressBookGUI extends JFrame {
    private DefaultListModel<String> contactListModel;
    private JList<String> contactList;
    private JTextField firstNameField, lastNameField, emailField, phoneField, companyField, searchField;
    private JButton addButton, deleteButton, updateButton, findButton, prevButton, nextButton;

    private InitialDatabase initialDatabase;
    private int startIndex = 0; // Index to keep track of the starting point for displaying contacts

    public AddressBookGUI(DefaultListModel<String> contactListModel,  InitialDatabase initialDatabase){
        
        this.initialDatabase = initialDatabase;

        setTitle("Address Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Left panel for contact list
        JPanel leftPanel = new JPanel(new BorderLayout());
        this.contactListModel = contactListModel; // Update the contactListModel field
        contactList = new JList<>(this.contactListModel); // Use the updated contactListModel
        JScrollPane scrollPane = new JScrollPane(contactList);
         // Set the preferred width of the scroll pane
        Dimension scrollPaneSize = new Dimension(300, 400); // Adjust width and height as needed
    scrollPane.setPreferredSize(scrollPaneSize);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Right panel for contact details
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        rightPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        firstNameField = new JTextField(20);
        rightPanel.add(firstNameField, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        lastNameField = new JTextField(20);
        rightPanel.add(lastNameField, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        emailField = new JTextField(20);
        rightPanel.add(emailField, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridy++;
        phoneField = new JTextField(20);
        rightPanel.add(phoneField, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Company:"), gbc);
        gbc.gridy++;
        companyField = new JTextField(20);
        rightPanel.add(companyField, gbc);

        gbc.gridy++;
        addButton = new JButton("Add");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(addButton, gbc);
        gbc.gridy++;
        deleteButton = new JButton("Delete");
        rightPanel.add(deleteButton, gbc);
        gbc.gridy++;
        updateButton = new JButton("Update");
        rightPanel.add(updateButton, gbc);

        // Bottom panel for searching and navigation
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        findButton = new JButton("Find");

        searchPanel.add(new JLabel("Find by Last Name: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(findButton, BorderLayout.EAST);
        bottomPanel.add(searchPanel, BorderLayout.CENTER);
        prevButton = new JButton("Prev");
        nextButton = new JButton("Next");
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.add(prevButton);
        navPanel.add(nextButton);
        bottomPanel.add(navPanel, BorderLayout.EAST);

        // Apply larger font size to all components
        Font largerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16); // Adjust size as needed
        contactList.setFont(largerFont);
        firstNameField.setFont(largerFont);
        lastNameField.setFont(largerFont);
        emailField.setFont(largerFont);
        phoneField.setFont(largerFont);
        companyField.setFont(largerFont);
        addButton.setFont(largerFont);
        deleteButton.setFont(largerFont);
        updateButton.setFont(largerFont);
        findButton.setFont(largerFont);
        prevButton.setFont(largerFont);
        nextButton.setFont(largerFont);
        searchField.setFont(largerFont);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

//----------------------LISTENERS----------------------------------------------------------

// Add action listener to the Prev button
prevButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex > 0) {
            contactList.setSelectedIndex(selectedIndex - 1);
            displaySelectedContactInfo();
        }
    }
});

// Add action listener to the Next button
nextButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex < contactListModel.getSize() - 1) {
            contactList.setSelectedIndex(selectedIndex + 1);
            displaySelectedContactInfo();
        }
    }
});

 // Add action listener to the Add button
 addButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String company = companyField.getText().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !company.isEmpty()) {
            initialDatabase.addContact(firstName, lastName, email, phone, company);
            refreshContactList();
        } else {
            JOptionPane.showMessageDialog(AddressBookGUI.this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

// Add action listener to the Update button
updateButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String company = companyField.getText().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !company.isEmpty()) {
            initialDatabase.updateContact(firstName, lastName, email, phone, company);
            refreshContactList();
        } else {
            JOptionPane.showMessageDialog(AddressBookGUI.this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

// Add action listener to the Delete button
deleteButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String lastName = lastNameField.getText().trim();
        if (!lastName.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(AddressBookGUI.this, "Are you sure you want to delete the contact with last name '" + lastName + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                initialDatabase.deleteContact(lastName);
                refreshContactList();
            }
        } else {
            JOptionPane.showMessageDialog(AddressBookGUI.this, "Last name field is required.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

        // Add ListSelectionListener to the contactList
        contactList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedContact = contactList.getSelectedValue();
                    if (selectedContact != null) {
                        String[] contactInfo = selectedContact.split(", ");
                        String lastName = contactInfo[0];
                        displayContactInfo(lastName);
                    }
                }
            }
        });
        // Add action listener to the Find button
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchLastName = searchField.getText().trim();
                if (!searchLastName.isEmpty()) {
                    findContact(searchLastName);
                }
            }
        });

        setVisible(true);
        refreshContactList(); // Initially refresh the contact list
    }
//------------OTHER METHODS-----------------------------------------------------------------------------
// Method to find a contact by last name
private void findContact(String lastName) {
    DefaultListModel<String> foundContacts = initialDatabase.findContactsByLastName(lastName);
    if (foundContacts.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Contact with last name '" + lastName + "' not found.", "Contact Not Found", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // Select the found last name in the contact list
        contactList.setSelectedValue(lastName, true);
        displayContactInfo(lastName);
    }
}
// Method to display contact information
private void displayContactInfo(String lastName) {
    // firstNameField.setText(firstName);
    lastNameField.setText(lastName);

    // Fetch all contact information from the database
    DefaultListModel<String> contactInfoList = initialDatabase.getAllContactInfo(lastName);
    if (contactInfoList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Contact with last name '" + lastName + "' not found.", "Contact Not Found", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // Extract contact information from the list and display it in respective fields
        String[] contactInfo = contactInfoList.getElementAt(0).split(",");
        firstNameField.setText(contactInfo[1].trim());
        emailField.setText(contactInfo[2].trim());
        phoneField.setText(contactInfo[3].trim());
        companyField.setText(contactInfo[4].trim());
    }
}

// private void refreshContactList() {
//     contactListModel.clear();
//     DefaultListModel<String> contacts = initialDatabase.getContacts();
//     for (int i = 0; i < contacts.size(); i++) {
//         contactListModel.addElement(contacts.getElementAt(i));
//     }
// }
private void refreshContactList() {
    contactListModel.clear();
    DefaultListModel<String> contacts = initialDatabase.getContacts();
    for (int i = 0; i < contacts.size(); i++) {
        contactListModel.addElement(contacts.getElementAt(i));
    }
}
// Method to display information of the selected contact
private void displaySelectedContactInfo() {
    String selectedContact = contactList.getSelectedValue();
    if (selectedContact != null) {
        String[] contactInfo = selectedContact.split(", ");
        String lastName = contactInfo[0];
        displayContactInfo(lastName);
    }
}
}