package project;

public class typeMonthContainer {
    private String type;
    private int monthNum;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        if (monthNum == 1) {
            return "January";
        }
        else if (monthNum == 2) {
            return "February";
        }
        else if (monthNum == 3) {
            return "March";
        }
        else if (monthNum == 4) {
            return "April";
        }
        else if (monthNum == 5) {
            return "May";
        }
        else if (monthNum == 6) {
            return "June";
        }
        else if (monthNum == 7) {
            return "July";
        }
        else if (monthNum == 8) {
            return "August";
        }
        else if (monthNum == 9) {
            return "September";
        }
        else if (monthNum == 10) {
            return "October";
        }
        else if (monthNum == 11) {
            return "November";
        }
        else {
            return "December";
        }
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
