package project;

import java.util.Date;

public class Countries {
    private int countryID;
    private String countryName;
    private Date creationDate;
    private String whoCreated;
    private Date lastUpdate;
    private String whoLastChanged;

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getWhoLastChanged() {
        return whoLastChanged;
    }

    public void setWhoLastChanged(String whoLastChanged) {
        this.whoLastChanged = whoLastChanged;
    }
}
