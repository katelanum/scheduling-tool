package project;

import java.util.Date;
import java.time.*;
import java.sql.*;

// add, update, and delete appointments
// read in the fields from the database
public class Appointment {
    // appointmentId will be in string format because despite being a number, it will not be getting manipulated
    // like one, formatting it as a string allows for better control of what is being done with the appointmentId
    int appointmentId;
    String title;
    String description;
    String location;
    String type;
    Date start;
    Date end;
    Date creationDate;
    String creator;
    Date modifyTime;
    String lastModifier;
    //customerId is a foreign key to the customer record
    int customerId;
    int userId;
    int contactId;
}
