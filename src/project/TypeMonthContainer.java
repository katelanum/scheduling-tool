package project;

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
}
