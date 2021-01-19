package project;

/**
 *  Contact acts as an interface between the database and the backend code.
 *  The Contact object is a container for the data stored in the database.
 *
 *  @author katelanum
 */
public class Contact {
    private int contactId;
    private String name;
    private String email;

    /**
     * toString returns the Contact's name
     *
     * @return The name String from the Contact.
     */
    @Override
    public String toString() {
        return name;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
