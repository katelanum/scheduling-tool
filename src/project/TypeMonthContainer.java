package project;

import java.util.ResourceBundle;

/**
 * TypeMonthContainer is a container for both months and types from Appointments as well as a count.
 * This allows for the generation of the reports for type and month of appointments.
 *
 * @author katelanum
 */
public class TypeMonthContainer {
    private String type;
    private int monthNum;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }

    /**
     * Takes in the languageBundle and translates the month number to a localized string of the month name
     *
     * @param languageBundle used to figure out which language to translate the month name to
     *
     * @return a Localized string of the name of the month
     */
    public String getMonth(ResourceBundle languageBundle) {
        String resourceName;
        if (monthNum == 1) {
            resourceName = "jan";
        }
        else if (monthNum == 2) {
            resourceName = "feb";
        }
        else if (monthNum == 3) {
            resourceName = "mar";
        }
        else if (monthNum == 4) {
            resourceName = "apr";
        }
        else if (monthNum == 5) {
            resourceName = "may";
        }
        else if (monthNum == 6) {
            resourceName = "jun";
        }
        else if (monthNum == 7) {
            resourceName = "jul";
        }
        else if (monthNum == 8) {
            resourceName = "aug";
        }
        else if (monthNum == 9) {
            resourceName = "sep";
        }
        else if (monthNum == 10) {
            resourceName = "oct";
        }
        else if (monthNum == 11) {
            resourceName = "nov";
        }
        else {
            resourceName = "dec";
        }

        return languageBundle.getString(resourceName);
    }
}
