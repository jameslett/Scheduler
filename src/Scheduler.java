import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;


public class Scheduler extends Application {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<User> users  = FXCollections.observableArrayList();
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    public static void main(String[] args) {




        launch(args);


    }


    @Override
    public void start(Stage primaryStage){


        String getCustomer = "SELECT * FROM customers";
        String getUser = "SELECT * FROM users";
        String getContacts = "SELECT * FROM contacts";

        try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {
            System.out.println(con);
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


                    System.out.println(name);
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



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




     /* // User test = new User();
      // Customer cusTest = new Customer();
     //  cusTest.AddAppointment(new Appointment(LocalTime.of(10,0),LocalTime.of(11,0),new Customer(),new Contact(),new User()));
       ArrayList<Schedulable> list = new ArrayList<Schedulable>();
       list.add(test);
     //  list.add(cusTest);
      //  test.AddAppointment(new Appointment(LocalTime.of(9,0),LocalTime.of(10,0),new Customer(),new Contact(),new User()));

        System.out.println(test);
       ObservableList<LocalTime> times =  test.GetAvailableStartTimes(list);
       System.out.println(times);
       System.out.println(test.GetAvailableEndTimes(list,times.get(0)));
     //  System.out.println("TEST");
*/
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

        public Contact(int contactID,String name, String email){
            this.name.set(name);
            this.contactID.set(contactID);
            this.email.set(email);
        }

    }

    public class Customer extends Schedulable{
        private IntegerProperty customerID = new SimpleIntegerProperty();
        private StringProperty name = new SimpleStringProperty();
        private StringProperty address = new SimpleStringProperty();
        private IntegerProperty countryID = new SimpleIntegerProperty();
        private IntegerProperty divisionID = new SimpleIntegerProperty();
        private StringProperty phone = new SimpleStringProperty();
        private StringProperty postalCode = new SimpleStringProperty();

        public Customer(int customerID,String name,String address, int countryID,int divisionID, String phone, String postalCode){
            this.customerID.set(customerID);
            this.name.set(name);
            this.address.set(address);
            this.countryID.set(countryID);
            this.divisionID.set(divisionID);
            this.phone.set(phone);
            this.postalCode.set(postalCode);
        }

    }

    public class User extends Schedulable {
        User(int userID,String username,String password){
            this.username= username;
            this.userID = userID;
            this.password = password;

        }
        private int userID;
        private String username;
        private String password;


    }



}
