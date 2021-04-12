package Scheduler;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.*;
import java.util.*;


/**
 * The type Scheduler.
 */
public class Scheduler extends Application {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<User> users  = FXCollections.observableArrayList();

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private ObservableList<FirstLevelDivision>divisions = FXCollections.observableArrayList();
    private HashMap<Integer,String> firstLevelDivisions = new HashMap<Integer, String>();
    private HashMap<Integer,String> countries = new HashMap<Integer, String>();
    private HashMap<Integer,Integer> firstLevelToCountry = new HashMap<Integer,Integer>();

    private static Stage loginStage = new Stage();


    private static User user;
    private  MainController controller;


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {







        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        parseSQL();

        boolean hasAppointment = false;


        Locale locale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("login", locale);

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("login.fxml"));
        loader2.setResources(resources);

        Parent root2 = null;
        try {
            root2 = loader2.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController loginCon = loader2.getController();
        loginCon.SetMainApp(this);


        loginCon.setResourceBundle(resources);

        loginCon.getZoneID().setText(loginCon.getResourceBundle().getString("timeZone") + " : " + ZoneId.systemDefault().toString() );
        loginStage.setScene(new Scene(root2));

        loginStage.setOnCloseRequest(event -> event.consume());

        loginStage.showAndWait();











        for(Appointment a : user.getAppointments()){

            if (a.getStartTime().getHour() == LocalTime.now().getHour() &&
                    a.getStartTime().getMinute()-LocalTime.now().getMinute() <= 15 &&
            a.getDate().equals(LocalDate.now())&&
            a.getStartDateTime().isAfter(LocalDateTime.now())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointments");
                alert.setHeaderText(null);
                alert.setContentText("You have an upcoming appointment with " + a.getCustomer().getName()+" in "+  (a.getStartTime().getMinute()-LocalTime.now().getMinute()) +" minutes") ;

                alert.showAndWait();
                hasAppointment = true;
                break;
        }
        }

        if(!hasAppointment){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText(null);
            alert.setContentText("You have no upcoming appointments!");

            alert.showAndWait();

        }




        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        

        primaryStage.setTitle(user.getUsername() + "'s Appointments" );
        primaryStage.setScene(new Scene(root));
        primaryStage.show();






        controller = loader.getController();
        controller.setMainApp(this);







    }

    /**
     * The appointment Class Contains all appointment info
     */
    public static class Appointment {


        private ObjectProperty<Customer> customer = new SimpleObjectProperty<Customer>();
        private ObjectProperty<User> user = new SimpleObjectProperty<User>();
        private ObjectProperty<Contact> contact = new SimpleObjectProperty<Contact>();
        private ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<LocalTime>();
        private ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<LocalTime>();
        private ObjectProperty<LocalDate> date = new SimpleObjectProperty<LocalDate>();
        private StringProperty title = new SimpleStringProperty();
        private StringProperty description = new SimpleStringProperty();
        private StringProperty location = new SimpleStringProperty();
        private ObjectProperty <LocalDateTime> startDateTime = new SimpleObjectProperty<LocalDateTime>();

        private StringProperty type = new SimpleStringProperty();


        private IntegerProperty appointmentID = new SimpleIntegerProperty();


        /**
         * Instantiates a new Appointment.
         *
         * @param appointmentID the appointment id
         * @param startTime     the start time
         * @param endTime       the end time
         * @param cus           the customer
         * @param con           the contact
         * @param userIn        the user in
         * @param type          the type
         * @param title         the title
         * @param description   the description
         * @param location      the location
         */
        public Appointment(int appointmentID, LocalDateTime startTime, LocalDateTime endTime, Customer cus, Contact con, User userIn,String type,String title,String description, String location)
        {
            this.appointmentID.set(appointmentID);
            this.startTime.set(startTime.toLocalTime());
            this.endTime.set(endTime.toLocalTime());
            this.startDateTime.set(startTime);
            customer.set(cus);
            contact.set(con);
            user.set(userIn);
            this.title.set(title);
            this.description.set(description);
            this.location.set(location);
            this.date.set(startTime.toLocalDate());
            this.type.set(type);
            cus.AddAppointment(this);
            userIn.AddAppointment(this);
            con.AddAppointment(this);

        }

        @Override
        public String toString(){
            String temp = "Apt ID: " + appointmentID.get()+ " "  +customer.getName() +" Start: "+ startTime.get() +" End: " +endTime.get() + "Date: " + date.get() +"\n";
            return temp;
        }

        /**
         * Gets start date time.
         *
         * @return the start date time
         */
        public LocalDateTime getStartDateTime() {
            return startDateTime.get();
        }


        /**
         * Gets type.
         *
         * @return the type
         */
        public String getType() {
            return type.get();
        }


        /**
         * Type property string property.
         *
         * @return the string property
         */
        public StringProperty typeProperty() {
            return type;
        }


        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(String type) {
            this.type.set(type);
        }


        /**
         * Gets customer.
         *
         * @return the customer
         */
        public Customer getCustomer() {
            return customer.get();
        }


        /**
         * Customer property object property.
         *
         * @return the object property
         */
        public ObjectProperty<Customer> customerProperty() {
            return customer;
        }

        /**
         * Sets customer.
         *
         * @param customer the customer
         */
        public void setCustomer(Customer customer) {
            this.customer.set(customer);
        }


        /**
         * Gets contact.
         *
         * @return the contact
         */
        public Contact getContact() {
            return contact.get();
        }


        /**
         * Contact property object property.
         *
         * @return the object property
         */
        public ObjectProperty<Contact> contactProperty() {
            return contact;
        }


        /**
         * Sets contact.
         *
         * @param contact the contact
         */
        public void setContact(Contact contact) {
            this.contact.set(contact);
        }


        /**
         * Gets title.
         *
         * @return the appointment title string
         */
        public String getTitle() {
            return title.get();
        }

        /**
         * Title property string property.
         *
         * @return the appointment title property
         */
        public StringProperty titleProperty() {
            return title;
        }

        /**
         * sets the appointment title
         *
         * @param title the title to be set
         */
        public void setTitle(String title) {
            this.title.set(title);
        }

        /**
         * Gets description.
         *
         * @return the appointment description
         */
        public String getDescription() {
            return description.get();
        }

        /**
         * Description property string property.
         *
         * @return the appointment description property
         */
        public StringProperty descriptionProperty() {
            return description;
        }

        /**
         * sets the appointment description
         *
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description.set(description);
        }

        /**
         * Gets location.
         *
         * @return the location location string
         */
        public String getLocation() {
            return location.get();
        }

        /**
         * Location property string property.
         *
         * @return the appointment location property
         */
        public StringProperty locationProperty() {
            return location;
        }

        /**
         * Gets start time.
         *
         * @return the start time
         */
        public LocalTime getStartTime() {
            return startTime.get();
        }

        /**
         * Start time property object property.
         *
         * @return the object property
         */
        public ObjectProperty<LocalTime> startTimeProperty() {
            return startTime;
        }

        /**
         * Sets start time.
         *
         * @param startTime the start time
         */
        public void setStartTime(LocalTime startTime) {
            this.startTime.set(startTime);
        }

        /**
         * Gets end time.
         *
         * @return the end time
         */
        public LocalTime getEndTime() {
            return endTime.get();
        }

        /**
         * End time property object property.
         *
         * @return the object property
         */
        public ObjectProperty<LocalTime> endTimeProperty() {
            return endTime;
        }

        /**
         * Sets end time.
         *
         * @param endTime the end time
         */
        public void setEndTime(LocalTime endTime) {
            this.endTime.set(endTime);
        }

        /**
         * Gets date.
         *
         * @return the date
         */
        public LocalDate getDate() {
            return date.get();
        }

        /**
         * Date property object property.
         *
         * @return the object property
         */
        public ObjectProperty<LocalDate> dateProperty() {
            return date;
        }

        /**
         * Sets date.
         *
         * @param date the date
         */
        public void setDate(LocalDate date) {
            this.date.set(date);
        }

        /**
         * Sets location.
         *
         * @param location the location
         */
        public void setLocation(String location) {
            this.location.set(location);
        }

        /**
         * Gets appointment id.
         *
         * @return the appointment id
         */
        public int getAppointmentID() {
            return appointmentID.get();
        }

        /**
         * Appointment id property integer property.
         *
         * @return the integer property
         */
        public IntegerProperty appointmentIDProperty() {
            return appointmentID;
        }

        /**
         * Sets appointment id.
         *
         * @param appointmentID the appointment id
         */
        public void setAppointmentID(int appointmentID) {
            this.appointmentID.set(appointmentID);
        }

        /**
         * Delete appointment.
         */
        public void deleteAppointment(){
            customer.get().getAppointments().remove(this);
            contact.get().getAppointments().remove(this);
            customer.get().getAppointments().remove(this);
        }

        /**
         * Delete from db.
         */
        public void deleteFromDB(){

            String deleteAppointment = "DELETE FROM appointments WHERE Appointment_ID =" + appointmentID.get();

            try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

                try(var statement = con.prepareStatement(deleteAppointment)) {
                    statement.execute();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }

        /**
         * Add to db.
         */
        public void addToDB(){


            String deleteAppointment = "INSERT INTO appointments(Appointment_ID,Title,Description,Location,Type,Start,End,Created_By,Customer_ID,User_ID,Contact_ID,Last_Updated_By) Values(?,?,?,?,?,?,?,?,?,?,?,?)";



            try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

                try(var statement = con.prepareStatement(deleteAppointment)) {
                    statement.setInt(1,getAppointmentID());
                    statement.setString(2,getTitle());
                    statement.setString(3,getDescription());
                    statement.setString(4,getLocation());
                    statement.setString(5,getType());
                    statement.setTimestamp(6,convertToUTCStamp(getDate(),getStartTime()));
                    statement.setTimestamp(7,convertToUTCStamp(getDate(),getEndTime()));
                    statement.setString(8,getUser().getUsername());
                    statement.setInt(9,getCustomer().getID());
                    statement.setInt(10,getUser().getID());
                    statement.setInt(11,getContact().getID());
                    statement.setString(12,getUser().getUsername());
                    System.out.println(statement);


                    statement.executeUpdate();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }

        /**
         * Gets user.
         *
         * @return the user
         */
        public User getUser() {
            return user.get();
        }

        /**
         * User property object property.
         *
         * @return the object property
         */
        public ObjectProperty<User> userProperty() {
            return user;
        }

        public ArrayList<LocalTime> getUnavailableTimes(){
                ArrayList<LocalTime> temp = new ArrayList<>();
            for(LocalTime time = startTime.get(); time.isBefore(endTime.get());time = time.plusMinutes(15) ){




                  temp.add(time);



            }
            return temp;

        }
    }

    /**
     * The type Schedulable. Anything that can be scheduled
     */
    public abstract class Schedulable{

       private IntegerProperty ID = new SimpleIntegerProperty();
       private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
       private ObservableList<LocalTime> availableTimes = FXCollections.observableArrayList();
        /**
         * The Start time.
         */
        LocalTime startTime;
        /**
         * The End time.
         */
        LocalTime endTime;

        /**
         * Instantiates a new Schedulable.
         */
        Schedulable(){
           ZonedDateTime zonedStart =  ZonedDateTime.of(LocalDate.now(),LocalTime.of(8,0,0),ZoneId.of("US/Eastern"));
           startTime = zonedStart.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
           ZonedDateTime zonedEnd  =   ZonedDateTime.of(LocalDate.now(),LocalTime.of(22,0,0),ZoneId.of("US/Eastern"));
           endTime = zonedEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
       }

        /**
         * Gets id.
         *
         * @return the id
         */
        public int getID() {
           return ID.get();
       }

        /**
         * Id property integer property.
         *
         * @return the integer property
         */
        public IntegerProperty IDProperty() {
           return ID;
       }

        /**
         * Sets id.
         *
         * @param ID the id
         */
        public void setID(int ID) {
           this.ID.set(ID);
       }

        /**
         * Delete from db.
         */
        public void deleteFromDB(){

           String deleteSchedulable = "DELETE FROM "+ this.getClass().getName().toLowerCase()+ "s WHERE " + this.getClass().getName().toLowerCase()+ "_ID=" + getID();

           try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

               try(var statement = con.prepareStatement(deleteSchedulable)) {
                   statement.execute();
               }

           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }


       }


        /**
         * Get available start times observable list.
         *
         * @param parties the parties
         * @param date    the date
         * @param exclude the exclude
         * @return the observable list
         */
        public ObservableList<LocalTime> GetAvailableStartTimes(ArrayList<Schedulable> parties,LocalDate date,Appointment exclude){


           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           ArrayList<LocalTime> removeTimes= new ArrayList<LocalTime>();
           ArrayList<Appointment>appointments = new ArrayList<Appointment>();
           for(Schedulable p : parties){

               System.out.println(p.getAppointmentsByDate(date) + " BY DATE "+date +"\n");

               for(Appointment a : p.getAppointmentsByDate(date)){
                   if(!appointments.contains(a)){
                      if(exclude == null || a.getAppointmentID() != exclude.getAppointmentID()){
                       appointments.add(a);
                      removeTimes.addAll(a.getUnavailableTimes());
                      }
                   }

               }
           }



           for(LocalTime time = startTime; time.isBefore(endTime);time = time.plusMinutes(15) ){

               if(date.isAfter(LocalDate.now())){
               availTimes.add(time);
               }
               else{

                   if(time.isAfter(LocalTime.now())){
                       availTimes.add(time);

                   }

               }

           }





           for(LocalTime t : removeTimes){
                availTimes.remove(t);

           }



           return availTimes;
       }

        /**
         * Get available end times observable list.
         *
         * @param parties the parties
         * @param start   the start
         * @param date    the date
         * @param exclude the exclude
         * @return the observable list
         */
        public ObservableList<LocalTime> GetAvailableEndTimes(ArrayList<Schedulable> parties, LocalTime start,LocalDate date,Appointment exclude)
       {


           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           LocalTime tempEnd = parties.get(0).getEndTime();

           ArrayList<Appointment>appointments = new ArrayList<Appointment>();
           for(Schedulable p : parties){
               for(Appointment a : p.getAppointmentsByDate(date)){
                   if(!appointments.contains(a)){
                      if(exclude == null || a.getAppointmentID()!= exclude.getAppointmentID()){
                       appointments.add(a);}
                   }

               }
           }






               for(Appointment a : appointments){

                       if(a.getStartTime().isAfter(start) && a.getStartTime().isBefore(tempEnd)){
                           tempEnd = a.getStartTime();

                   }
               }


           for(LocalTime time = start.plusMinutes(15); time.isBefore(tempEnd);time = time.plusMinutes(15) ){
               availTimes.add(time);


           }
           availTimes.add(tempEnd);



           return availTimes;

       }

        /**
         * Gets appointments.
         *
         * @return the appointments
         */
        public ObservableList<Appointment> getAppointments() {
           return appointments;
       }

        /**
         * Gets appointments by date.
         *
         * @param date the date
         * @return the appointments by date
         */
        public ObservableList<Appointment> getAppointmentsByDate(LocalDate date) {
           ObservableList<Appointment> temp = FXCollections.observableArrayList();

           for(Appointment a : appointments){
               if(a.getDate().isEqual(date)){
                   temp.add(a);
               }
           }



           return temp;
       }


        /**
         * Add appointment.
         *
         * @param appointment the appointment
         */
        public void AddAppointment(Appointment appointment){
           appointments.add(appointment);
       }

        /**
         * Sets appointments.
         *
         * @param appointments the appointments
         */
        public void setAppointments(ObservableList<Appointment> appointments) {
           this.appointments = appointments;
       }

        /**
         * Gets available times.
         *
         * @return the available times
         */
        public ObservableList<LocalTime> getAvailableTimes() {
           return availableTimes;
       }

        /**
         * Sets available times.
         *
         * @param availableTimes the available times
         */
        public void setAvailableTimes(ObservableList<LocalTime> availableTimes) {
           this.availableTimes = availableTimes;
       }

        /**
         * Gets start time.
         *
         * @return the start time
         */
        public LocalTime getStartTime() {
           return startTime;
       }

        /**
         * Sets start time.
         *
         * @param startTime the start time
         */
        public void setStartTime(LocalTime startTime) {
           this.startTime = startTime;
       }

        /**
         * Gets end time.
         *
         * @return the end time
         */
        public LocalTime getEndTime() {
           return endTime;
       }

        /**
         * Sets end time.
         *
         * @param endTime the end time
         */
        public void setEndTime(LocalTime endTime) {
           this.endTime = endTime;
       }

        /**
         * Delete all appointments.
         */
        public void deleteAllAppointments(){

           for(Appointment a : appointments){

               a.deleteFromDB();
               a.deleteAppointment();
           }

       }


   }

    /**
     * The type Contact.
     */
    public class Contact extends Schedulable{

        private StringProperty name = new SimpleStringProperty();
        private StringProperty email = new SimpleStringProperty();


        /**
         * Instantiates a new Contact.
         *
         * @param contactID the contact id
         * @param name      the name
         * @param email     the email
         */
        public Contact(int contactID,String name, String email){
            this.name.set(name);
            super.setID(contactID);
            this.email.set(email);
        }

        @Override
        public String toString() {
            return this.getName();
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name.get();
        }

        /**
         * Name property string property.
         *
         * @return the string property
         */
        public StringProperty nameProperty() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name.set(name);
        }

        /**
         * Gets email.
         *
         * @return the email
         */
        public String getEmail() {
            return email.get();
        }

        /**
         * Email property string property.
         *
         * @return the string property
         */
        public StringProperty emailProperty() {
            return email;
        }

        /**
         * Sets email.
         *
         * @param email the email
         */
        public void setEmail(String email) {
            this.email.set(email);
        }

    }

    /**
     * The type Customer.
     */
    public class Customer extends Schedulable{

        private StringProperty name = new SimpleStringProperty();
        private StringProperty address = new SimpleStringProperty();
        private IntegerProperty countryID = new SimpleIntegerProperty();
        private IntegerProperty divisionID = new SimpleIntegerProperty();
        private StringProperty phone = new SimpleStringProperty();
        private StringProperty postalCode = new SimpleStringProperty();
        private StringProperty firstLevelString = new SimpleStringProperty();
        private StringProperty countryString = new SimpleStringProperty();

        /**
         * Instantiates a new Customer.
         *
         * @param customerID the customer id
         * @param name       the name
         * @param address    the address
         * @param countryID  the country id
         * @param divisionID the division id
         * @param phone      the phone
         * @param postalCode the postal code
         */
        public Customer(int customerID,String name,String address, int countryID,int divisionID, String phone, String postalCode){
            super.setID(customerID);
            this.name.set(name);
            this.address.set(address);
            this.countryID.set(countryID);
            this.divisionID.set(divisionID);
            this.phone.set(phone);
            this.postalCode.set(postalCode);
            this.firstLevelString.set(getDivisionName(divisionID));
            this.countryString.set(countries.get(countryID));
            System.out.println(divisionID + "Div ID");
            System.out.println();
            System.out.println(firstLevelStringProperty().get());
        }

        @Override
        public String toString() {
            return Integer.toString(this.getID());
        }


        /**
         * Gets first level string.
         *
         * @return the first level string
         */
        public String getFirstLevelString() {
            return firstLevelString.get();
        }

        /**
         * First level string property string property.
         *
         * @return the string property
         */
        public StringProperty firstLevelStringProperty() {
            return firstLevelString;
        }

        /**
         * Sets first level string.
         *
         * @param firstLevelString the first level string
         */
        public void setFirstLevelString(String firstLevelString) {
            this.firstLevelString.set(firstLevelString);
        }

        /**
         * Gets country string.
         *
         * @return the country string
         */
        public String getCountryString() {
            return countryString.get();
        }

        /**
         * Country string property string property.
         *
         * @return the string property
         */
        public StringProperty countryStringProperty() {
            return countryString;
        }

        /**
         * Sets country string.
         *
         * @param countryString the country string
         */
        public void setCountryString(String countryString) {
            this.countryString.set(countryString);
        }


        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name.get();
        }

        /**
         * Name property string property.
         *
         * @return the string property
         */
        public StringProperty nameProperty() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name.set(name);
        }

        /**
         * Gets address.
         *
         * @return the address
         */
        public String getAddress() {
            return address.get();
        }

        /**
         * Address property string property.
         *
         * @return the string property
         */
        public StringProperty addressProperty() {
            return address;
        }

        /**
         * Sets address.
         *
         * @param address the address
         */
        public void setAddress(String address) {
            this.address.set(address);
        }

        /**
         * Gets country id.
         *
         * @return the country id
         */
        public int getCountryID() {
            return countryID.get();
        }

        /**
         * Country id property integer property.
         *
         * @return the integer property
         */
        public IntegerProperty countryIDProperty() {
            return countryID;
        }

        /**
         * Sets country id.
         *
         * @param countryID the country id
         */
        public void setCountryID(int countryID) {
            this.countryID.set(countryID);
        }

        /**
         * Gets division id.
         *
         * @return the division id
         */
        public int getDivisionID() {
            return divisionID.get();
        }

        /**
         * Division id property integer property.
         *
         * @return the integer property
         */
        public IntegerProperty divisionIDProperty() {
            return divisionID;
        }

        /**
         * Sets division id.
         *
         * @param divisionID the division id
         */
        public void setDivisionID(int divisionID) {
            this.divisionID.set(divisionID);
        }

        /**
         * Gets phone.
         *
         * @return the phone
         */
        public String getPhone() {
            return phone.get();
        }

        /**
         * Phone property string property.
         *
         * @return the string property
         */
        public StringProperty phoneProperty() {
            return phone;
        }

        /**
         * Sets phone.
         *
         * @param phone the phone
         */
        public void setPhone(String phone) {
            this.phone.set(phone);
        }

        /**
         * Gets postal code.
         *
         * @return the postal code
         */
        public String getPostalCode() {
            return postalCode.get();
        }

        /**
         * Postal code property string property.
         *
         * @return the string property
         */
        public StringProperty postalCodeProperty() {
            return postalCode;
        }

        /**
         * Sets postal code.
         *
         * @param postalCode the postal code
         */
        public void setPostalCode(String postalCode) {
            this.postalCode.set(postalCode);
        }

        public void deleteFromDB(){

            String deleteAppointment = "DELETE FROM customers WHERE Customer_ID =" + getID();
            System.out.println(deleteAppointment);
            try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

                try(var statement = con.prepareStatement(deleteAppointment)) {
                    statement.execute();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }

        /**
         * Add to db.
         */
        public void addToDB(){


            String deleteAppointment = "INSERT INTO customers(Customer_ID,Customer_Name,Postal_Code,Address,Phone,Division_ID,Last_Updated_By) Values(?,?,?,?,?,?,?)";

            System.out.println(deleteAppointment);
            try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

                try(var statement = con.prepareStatement(deleteAppointment)) {
                    statement.setInt(1,getID());
                    statement.setString(2,getName());
                    statement.setString(3,getPostalCode());
                    statement.setString(4,getAddress());
                    statement.setString(5,getPhone());
                    statement.setInt(6,getDivisionID());
                    statement.setString(7,user.getUsername());



                    statement.executeUpdate();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }



    }

    /**
     * The type User.
     */
    public class User extends Schedulable {
        /**
         * Instantiates a new User.
         *
         * @param userID   the user id
         * @param username the username
         * @param password the password
         */
        User(int userID,String username,String password){
            this.username= username;
            super.setID(userID);
            this.password = password;

        }

        private String username;
        private String password;

        @Override
        public String toString() {
            return Integer.toString(this.getID());
        }

        /**
         * Gets username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Sets username.
         *
         * @param username the username
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Gets password.
         *
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Sets password.
         *
         * @param password the password
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }


    /**
     * Parse sql for all customers,contacts,countries,divisions and appointments and sets up classes
     */
    public void parseSQL(){


        String getCustomer = "SELECT * FROM customers";
        String getUser = "SELECT * FROM users";
        String getContacts = "SELECT * FROM contacts";
        String getAppointments = "SELECT * FROM appointments";
        String getFirstLevel = "SELECT * FROM first_level_divisions";
        String getCountries = "SELECT * FROM countries;";

        try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {




            try (var statement = con.prepareStatement(getCountries)){

                ResultSet results = statement.executeQuery();

                while(results.next()){
                    String countryName = results.getString("Country");
                    int countryID = results.getInt("Country_ID");
                    countries.put(countryID,countryName);



                }

            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }

            try (var statement = con.prepareStatement(getFirstLevel)){

                ResultSet results = statement.executeQuery();

                while(results.next()){
                    String divisionName = results.getString("Division");
                    int divisionID = results.getInt("Division_ID");
                    int countryID = results.getInt("Country_ID");

                    divisions.add(new FirstLevelDivision(countryID,divisionID,countries.get(countryID),divisionName));



                }

            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }





            try (var statement = con.prepareStatement(getUser)){

                ResultSet results = statement.executeQuery();

                while(results.next()){
                    String name = results.getString("User_Name");
                    int user_id = results.getInt("User_ID");
                    String password = results.getString("Password");


                    User user = new User(user_id,name,password);
                    users.add(user);


                }

            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }

            //replace with login window later




            try (var statement = con.prepareStatement(getCustomer)){

                ResultSet results = statement.executeQuery();

                while(results.next()){
                    String name = results.getString("Customer_Name");
                    int customerID = results.getInt("Customer_ID");
                    String address = results.getString("Address");

                    int divisionID = results.getInt("Division_ID");
                    String phone = results.getString("Phone");
                    String postalCode = results.getString("Postal_Code");

                    int countryID;
                    String sql = "SELECT * FROM first_level_divisions WHERE Division_ID =  " + Integer.toString(divisionID);

                    try(var statement2 = con.prepareStatement(sql)){
                        ResultSet results2 = statement2.executeQuery();
                        results2.next();





                        countryID = results2.getInt("COUNTRY_ID");
                    }



                    Customer customer = new Customer(customerID,name,address,countryID,divisionID,phone,postalCode);


                    customers.add(customer);



                }

            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }




            try (var statement = con.prepareStatement(getContacts)){

                ResultSet results = statement.executeQuery();

                while(results.next()){
                    String name = results.getString("Contact_Name");
                    int contact_id = results.getInt("Contact_ID");
                    String email = results.getString("Email");


                    Contact contact = new Contact(contact_id,name,email);
                    contacts.add(contact);


                }

            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }

            try (var statement = con.prepareStatement(getAppointments)){

                ResultSet results = statement.executeQuery();
                Customer customer = null;
                Contact contact = null;
                User user = null;
                Appointment appointment = null;

                while(results.next()){
                    ZonedDateTime zonedStart =  ZonedDateTime.of(results.getTimestamp("Start").toLocalDateTime(),ZoneId.of("UTC"));
                    LocalDateTime startTime = zonedStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    ZonedDateTime zonedEnd  =  ZonedDateTime.of(results.getTimestamp("End").toLocalDateTime(),ZoneId.of("UTC"));
                    LocalDateTime endTime = zonedEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int appointmentID = results.getInt("Appointment_ID")            ;
                    String type = results.getString("Type");
                    String location = results.getString("Location");
                    String description = results.getString("Description");
                    String title = results.getString("Title");

                    for(Customer c: customers){
                        if(c.getID() == results.getInt("Customer_ID")){
                            customer = c;
                            break;
                        }
                    }
                    for(Contact c : contacts){
                        if(c.getID() == results.getInt("Contact_ID")){
                            contact = c;
                            break;
                        }
                    }
                    for(User u : users){

                        if(u.getID()== results.getInt("User_ID")){
                            user = u;
                            break;
                        }
                    }
                    appointment = new Appointment(appointmentID,startTime,endTime,customer,contact,user,type,title,description,location);

                }



            }



            catch(SQLException throwables){
                throwables.printStackTrace();
            }





        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        for(User u : users){
            for(Appointment a : u.getAppointments()){
                System.out.println(a.getAppointmentID());
                System.out.println(a.getStartTime());
                System.out.println(a.getEndTime());
                System.out.println(a.getCustomer().getName());
            }
        }


    }


    /**
     * Gets customers.
     *
     * @return the customers
     */
    public ObservableList<Customer> getCustomers() {
        return customers;
    }


    /**
     * Gets users.
     *
     * @return the users
     */
    public ObservableList<User> getUsers() {
        return users;
    }


    /**
     * Gets contacts.
     *
     * @return the contacts
     */
    public ObservableList<Contact> getContacts() {
        return contacts;
    }


    /**
     * Gets user.
     *
     * @return the user
     */
    public static User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Convert to utc stamp timestamp.
     *
     * @param date the date
     * @param time the time
     * @return the timestamp
     */
    public static Timestamp convertToUTCStamp(LocalDate date, LocalTime time){

        ZonedDateTime temp = LocalDateTime.of(date,time).atZone(ZoneId.systemDefault());
        ZonedDateTime utc = temp.withZoneSameInstant(ZoneId.of("UTC"));

        return Timestamp.valueOf(utc.toLocalDateTime());

    }

    /**
     * Gets divisions by country.
     *
     * @param countryName the country name
     * @return the divisions by country
     */
    public ObservableList<FirstLevelDivision> getDivisionsByCountry(String countryName) {
        ObservableList<FirstLevelDivision> temp = FXCollections.observableArrayList();

        for(FirstLevelDivision div : divisions){

            if (div.getCountryName() == countryName) {
                temp.add(div);
            }
        }


        return temp;
    }

    /**
     * Get country names observable list.
     *
     * @return the observable list
     */
    public ObservableList<String> getCountryNames(){
        ObservableList<String> temp = FXCollections.observableArrayList();
        for(String s : countries.values()){
            temp.add(s);
        }

        return temp;

    }

    /**
     * Sets divisions.
     *
     * @param divisions the divisions
     */
    public void setDivisions(ObservableList<FirstLevelDivision> divisions) {
        this.divisions = divisions;
    }

    /**
     * The type First level division.
     */
    public class FirstLevelDivision  {

        /**
         * The Country name.
         */
        String CountryName;
        /**
         * The First level name.
         */
        String firstLevelName;
        /**
         * The Country id.
         */
        int countryID;
        /**
         * The First level id.
         */
        int firstLevelID;

        /**
         * Instantiates a new First level division.
         *
         * @param countryID      the country id
         * @param firstLevelID   the first level id
         * @param countryName    the country name
         * @param firstLevelName the first level name
         */
        FirstLevelDivision(int countryID,int firstLevelID,String countryName,String firstLevelName){
            this.countryID = countryID;
            this.firstLevelName = firstLevelName;
            this.CountryName = countryName;
            this.firstLevelID = firstLevelID;
        }

      public String toString(){
            return firstLevelName;
      }

        /**
         * Gets country name.
         *
         * @return the country name
         */
        public String getCountryName() {
          return CountryName;
      }

        /**
         * Sets country name.
         *
         * @param countryName the country name
         */
        public void setCountryName(String countryName) {
          CountryName = countryName;
      }

        /**
         * Gets first level name.
         *
         * @return the first level name
         */
        public String getFirstLevelName() {
          return firstLevelName;
      }

        /**
         * Sets first level name.
         *
         * @param firstLevelName the first level name
         */
        public void setFirstLevelName(String firstLevelName) {
          this.firstLevelName = firstLevelName;
      }

        /**
         * Gets country id.
         *
         * @return the country id
         */
        public int getCountryID() {
          return countryID;
      }

        /**
         * Sets country id.
         *
         * @param countryID the country id
         */
        public void setCountryID(int countryID) {
          this.countryID = countryID;
      }

        /**
         * Gets first level id.
         *
         * @return the first level id
         */
        public int getFirstLevelID() {
          return firstLevelID;
      }

        /**
         * Sets first level id.
         *
         * @param firstLevelID the first level id
         */
        public void setFirstLevelID(int firstLevelID) {
          this.firstLevelID = firstLevelID;
      }
  }

    /**
     * Create customer customer.
     *
     * @param customerID the customer id
     * @param name       the name
     * @param address    the address
     * @param countryID  the country id
     * @param divisionID the division id
     * @param phone      the phone
     * @param postalCode the postal code
     * @return the customer
     */
    public Customer createCustomer(int customerID,String name,String address, int countryID,int divisionID, String phone, String postalCode){
        Customer customer = new Customer(customerID,name,address,countryID,divisionID,phone,postalCode);
        return customer;
  }

    /**
     * Get division name string.
     *
     * @param divID the div id
     * @return the string
     */
    public String getDivisionName(int divID){
        String temp = "";
        for(FirstLevelDivision d : divisions){
            if(d.getFirstLevelID() == divID){
              temp= d.getFirstLevelName();
            }
        }

        return temp;
  }

    /**
     * Get division from division by div id.
     *
     * @param divID the div id
     * @return the first level division
     */
    public FirstLevelDivision getDivision(int divID){
        for(FirstLevelDivision d : divisions){
            if (d.getFirstLevelID()== divID){
                return d;
            }
        }

        return null;

  }





}
