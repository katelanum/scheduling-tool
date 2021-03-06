package project;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *  FirstLevelDivision acts as an interface between the database and the backend code.
 *  The FirstLevelDivision object is a container for the data stored in the database.
 *
 * @author katelanum
 */
public class FirstLevelDivision {
    private int divisionId;
    private String divisionName;
    private Date createDate;
    private String creator;
    private LocalDateTime lastChanged;
    private String modifiedBy;
    private int countryId;
    private String countryName;

    public String toString() {
        return divisionName;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
