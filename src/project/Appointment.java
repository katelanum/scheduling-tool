package project;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.*;

/**
 *  Appointment acts as an interface between the database and the backend code.
 *  The Appointment object is a container for the data stored in the database.
 *
 * @author katelanum
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Date creationDate;
    private String creator;
    private Date modifyTime;
    private String lastModifier;
    private int customerId;
    private String customerName;
    private int userId;
    private int contactId;
    private String contactName;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

    public ZonedDateTime getStart() {
        return start;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * The setStart function accepts a Timestamp and converts that to the current timezone.
     * The class variable start is set to the resulting LocalDateTime object.
     *
     * @param startTime Timestamp to be converted.
     */
    public void setStart(Timestamp startTime) {
        LocalDateTime localStart = startTime.toLocalDateTime();
        start = ZonedDateTime.of(localStart, ZoneId.systemDefault());
    }

    /**
     *  The setEnd function accepts a Timestamp and converts that to the current timezone.
     *  The class variable start is set to the resulting LocalDateTime object.
     *
     * @param endTime Timestamp to be converted.
     */
    public void setEnd(Timestamp endTime) {
        LocalDateTime localEnd= endTime.toLocalDateTime();
        end = ZonedDateTime.of(localEnd, ZoneId.systemDefault());
    }

    public String getEndForm() {
        return end.format(format);
    }

    public String getStartForm() {
        return start.format(format);
    }
}

