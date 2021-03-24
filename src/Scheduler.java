import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;


public class Scheduler extends Application {


    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com", "U08HlC", "53689288782" );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        launch(args);


    }


    @Override
    public void start(Stage primaryStage){
       User test = new User();
       Customer cusTest = new Customer();
       cusTest.AddAppointment(new Appointment(LocalTime.of(10,0),LocalTime.of(11,0),new Customer(),new Contact(),new User()));
       ArrayList<Schedulable> list = new ArrayList<Schedulable>();
       list.add(test);
       list.add(cusTest);
        test.AddAppointment(new Appointment(LocalTime.of(9,0),LocalTime.of(10,0),new Customer(),new Contact(),new User()));

        System.out.println(test);
       ObservableList<LocalTime> times =  test.GetAvailableStartTimes(list);
       System.out.println(times);
       System.out.println(test.GetAvailableEndTimes(list,times.get(0)));
     //  System.out.println("TEST");

    }

    public class Appointment {

        private ObjectProperty<Customer> customer = new SimpleObjectProperty<Customer>();
        private ObjectProperty<User> user = new SimpleObjectProperty<User>();
        private ObjectProperty<Contact> contact = new SimpleObjectProperty<Contact>();
        private ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<LocalTime>();
        private ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<LocalTime>();
        private StringProperty title = new SimpleStringProperty();
        private StringProperty description = new SimpleStringProperty();
        private StringProperty location = new SimpleStringProperty();

        private IntegerProperty appointmentID = new SimpleIntegerProperty();

        public Appointment(LocalTime start,LocalTime end, Customer cus,Contact con,User userIn)
        {
            startTime.set(start);
            endTime.set(end);
            customer.set(cus);
            contact.set(con);
            user.set(userIn);
            title.set("");
            description.set("");
            location.set("");
        }

        public Customer getCustomer() {
            return customer.get();
        }

        public ObjectProperty<Customer> customerProperty() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer.set(customer);
        }

        public Contact getContact() {
            return contact.get();
        }

        public ObjectProperty<Contact> contactProperty() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact.set(contact);
        }

        public LocalTime getStartTime() {
            return startTime.get();
        }

        public ObjectProperty<LocalTime> startTimeProperty() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime.set(startTime);
        }

        public LocalTime getEndTime() {
            return endTime.get();
        }

        public ObjectProperty<LocalTime> endTimeProperty() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime.set(endTime);
        }

        public String getTitle() {
            return title.get();
        }

        public StringProperty titleProperty() {
            return title;
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public String getDescription() {
            return description.get();
        }

        public StringProperty descriptionProperty() {
            return description;
        }

        public void setDescription(String description) {
            this.description.set(description);
        }

        public String getLocation() {
            return location.get();
        }

        public StringProperty locationProperty() {
            return location;
        }

        public void setLocation(String location) {
            this.location.set(location);
        }

        public int getAppointmentID() {
            return appointmentID.get();
        }

        public IntegerProperty appointmentIDProperty() {
            return appointmentID;
        }

        public void setAppointmentID(int appointmentID) {
            this.appointmentID.set(appointmentID);
        }
    }

   public abstract class Schedulable{
       private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
       private ObservableList<LocalTime> availableTimes = FXCollections.observableArrayList();
       LocalTime startTime = LocalTime.of(8,0);
       LocalTime endTime = LocalTime.of(22,0);

       public ObservableList<LocalTime> GetAvailableStartTimes(ArrayList<Schedulable> parties){

           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           ArrayList<LocalTime> removeTimes= new ArrayList<LocalTime>();
           for(LocalTime time = startTime; time.isBefore(endTime);time = time.plusMinutes(15) ){
               availTimes.add(time);
               System.out.println(time);
           }
           for(Schedulable p : parties){
               for(Appointment a : p.getAppointments()){
                   for(LocalTime t : availTimes){
                        if(t == a.getStartTime() || (t.isAfter(a.getStartTime()) && t.isBefore(a.getEndTime()))){
                            removeTimes.add(t);
                        }
                   }
               }

           for(LocalTime t : removeTimes){
               availTimes.remove(t);
           }


           }
           System.out.println(removeTimes);


           return availTimes;
       }

       public ObservableList<LocalTime> GetAvailableEndTimes(ArrayList<Schedulable> parties, LocalTime start)
       {

           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           LocalTime tempEnd = null;
           for(Schedulable p : parties){
               if(tempEnd == null){
                   tempEnd = p.getEndTime();
               }
               for(Appointment a : p.getAppointments()){

                       if(a.getStartTime().isAfter(start) && a.getStartTime().isBefore(tempEnd)){
                           tempEnd = a.getStartTime();

                   }
               }

           }
           for(LocalTime time = start.plusMinutes(15); time.isBefore(tempEnd);time = time.plusMinutes(15) ){
               availTimes.add(time);

           }
           availTimes.add(tempEnd);



           return availTimes;

       }
       public ObservableList<Appointment> getAppointments() {
           return appointments;
       }

       public void AddAppointment(Appointment appointment){
           appointments.add(appointment);
       }
       public void setAppointments(ObservableList<Appointment> appointments) {
           this.appointments = appointments;
       }

       public ObservableList<LocalTime> getAvailableTimes() {
           return availableTimes;
       }

       public void setAvailableTimes(ObservableList<LocalTime> availableTimes) {
           this.availableTimes = availableTimes;
       }

       public LocalTime getStartTime() {
           return startTime;
       }

       public void setStartTime(LocalTime startTime) {
           this.startTime = startTime;
       }

       public LocalTime getEndTime() {
           return endTime;
       }

       public void setEndTime(LocalTime endTime) {
           this.endTime = endTime;
       }
   }

    public class Contact extends Schedulable{

        private StringProperty name = new SimpleStringProperty();
        private StringProperty email = new SimpleStringProperty();
        private IntegerProperty contactID = new SimpleIntegerProperty();
    }

    public class Customer extends Schedulable{
        private IntegerProperty customerID = new SimpleIntegerProperty();
        private StringProperty name = new SimpleStringProperty();
        private StringProperty address = new SimpleStringProperty();
        private IntegerProperty countryID = new SimpleIntegerProperty();
        private StringProperty phone = new SimpleStringProperty();
        private StringProperty postalCode = new SimpleStringProperty();

    }

    public class User extends Schedulable {
        User(){
            userID = 1;
            username = "test";
            password = "test";

        }
        private int userID;
        private String username;
        private String password;


    }



}
