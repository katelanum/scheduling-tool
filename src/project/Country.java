package project;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *  Country acts as an interface between the database and the backend code.
 *  The Country object is a container for the data stored in the database.
 *
 * @author katelanum
 */
public class Country {
    private int countryID;
    private String countryName;
    private Date creationDate;
    private String whoCreated;
    private LocalDateTime lastUpdate;
    private String whoLastChanged;

    public String toString() {
        return countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getWhoCreated() {
        return whoCreated;
    }

    public void setWhoCreated(String whoCreated) {
        this.whoCreated = whoCreated;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getWhoLastChanged() {
        return whoLastChanged;
    }

    public void setWhoLastChanged(String whoLastChanged) {
        this.whoLastChanged = whoLastChanged;
    }
}
