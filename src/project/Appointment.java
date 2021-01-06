package project;

import java.util.Date;
import java.time.*;

// add, update, and delete appointments
// read in the fields from the database
public class Appointment {
    // appointmentId will be in string format because despite being a number, it will not be getting manipulated
    // like one, formatting it as a string allows for better control of what is being done with the appointmentId
    long appointmentId;
    //customerId is a foreign key to the customer record
    long customerId;
    String title;
    String description;
    String location;
    String contact;
    String type;
    Date start;
    Date end;



}
