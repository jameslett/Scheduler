package Scheduler;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Scheduler extends Application {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<User> users  = FXCollections.observableArrayList();

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private HashMap<Integer,String> firstLevelDivisions = new HashMap<Integer, String>();
    private HashMap<Integer,String> countries = new HashMap<Integer, String>();

    private static User user;

    public static void main(String[] args) {




        System.out.println(LocalTime.now().atOffset(ZoneOffset.UTC));


        launch(args);


    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        parseSQL();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();


        primaryStage.setTitle(user.getUsername() + "'s Appointments" );
        primaryStage.setScene(new Scene(root));
        primaryStage.show();






        MainController controller = loader.getController();
        controller.setMainApp(this);






    Appointment appointment = new Appointment(3,LocalDateTime.now(),LocalDateTime.now().plusMinutes(10),new Customer(1,"test","test",1,3,"test","test"), new Contact(3,"test","test"),new User(3,"test","test"),"test","test","test","test");
   //appointment.addToDB();
  //  appointment.deleteFromDB();
    }

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

        private StringProperty type = new SimpleStringProperty();


        private IntegerProperty appointmentID = new SimpleIntegerProperty();

        public Appointment(int appointmentID, LocalDateTime startTime, LocalDateTime endTime, Customer cus, Contact con, User userIn,String type,String title,String description, String location)
        {
            this.appointmentID.set(appointmentID);
            this.startTime.set(startTime.toLocalTime());
            this.endTime.set(endTime.toLocalTime());
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



        public String getType() {
            return type.get();
        }

        public StringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
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

        public LocalDate getDate() {
            return date.get();
        }

        public ObjectProperty<LocalDate> dateProperty() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date.set(date);
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

        public void deleteAppointment(){
            customer.get().getAppointments().remove(this);
            contact.get().getAppointments().remove(this);
            customer.get().getAppointments().remove(this);
        }
        public void deleteFromDB(){

            String deleteAppointment = "DELETE FROM appointments WHERE Appointment_ID =" + appointmentID.get();
            System.out.println(deleteAppointment);
            try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

                try(var statement = con.prepareStatement(deleteAppointment)) {
                    statement.execute();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
        public void addToDB(){


            String deleteAppointment = "INSERT INTO appointments(Appointment_ID,Title,Description,Location,Type,Start,End,Created_By,Customer_ID,User_ID,Contact_ID,Last_Updated_By) Values(?,?,?,?,?,?,?,?,?,?,?,?)";

            System.out.println(deleteAppointment);
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

        public User getUser() {
            return user.get();
        }

        public ObjectProperty<User> userProperty() {
            return user;
        }
    }

   public abstract class Schedulable{

       private IntegerProperty ID = new SimpleIntegerProperty();
       private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
       private ObservableList<LocalTime> availableTimes = FXCollections.observableArrayList();
       LocalTime startTime = LocalTime.of(8,0);
       LocalTime endTime = LocalTime.of(22,0);

       public int getID() {
           return ID.get();
       }

       public IntegerProperty IDProperty() {
           return ID;
       }

       public void setID(int ID) {
           this.ID.set(ID);
       }

       public void deleteFromDB(){

           String deleteSchedulable = "DELETE FROM "+ this.getClass().getName().toLowerCase()+ "s WHERE " + this.getClass().getName().toLowerCase()+ "_ID=" + getID();
           System.out.println(deleteSchedulable);
           try( Connection con = DriverManager.getConnection( "jdbc:mysql://wgudb.ucertify.com/WJ08HlC", "U08HlC", "53689288782" );) {

               try(var statement = con.prepareStatement(deleteSchedulable)) {
                   statement.execute();
               }

           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }


       }


       public ObservableList<LocalTime> GetAvailableStartTimes(ArrayList<Schedulable> parties,LocalDate date,Appointment exclude){


           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           ArrayList<LocalTime> removeTimes= new ArrayList<LocalTime>();
           ArrayList<Appointment>appointments = new ArrayList<Appointment>();
           for(Schedulable p : parties){
               for(Appointment a : p.getAppointmentsByDate(date)){
                   if(!appointments.contains(a)){
                      if(a.getAppointmentID()!= exclude.getAppointmentID()){
                       appointments.add(a);}
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

                   for(LocalTime t : availTimes){
                       //remove time if appointment conflicts
                       for(Appointment a : appointments) {
                           if ((t == a.getStartTime() || (t.isAfter(a.getStartTime()) && t.isBefore(a.getEndTime()))) && a.getDate().equals(date)) {
                               removeTimes.add(t);
                           }
                       }

           }

           for(LocalTime t : removeTimes){
                availTimes.remove(t);

           }



           return availTimes;
       }

       public ObservableList<LocalTime> GetAvailableEndTimes(ArrayList<Schedulable> parties, LocalTime start,LocalDate date,Appointment exclude)
       {


           ObservableList<LocalTime> availTimes = FXCollections.observableArrayList();
           LocalTime tempEnd = parties.get(0).getEndTime();

           ArrayList<Appointment>appointments = new ArrayList<Appointment>();
           for(Schedulable p : parties){
               for(Appointment a : p.getAppointmentsByDate(date)){
                   if(!appointments.contains(a)){
                      if(a.getAppointmentID()!= exclude.getAppointmentID()){
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
               System.out.println(time);

           }
           availTimes.add(tempEnd);



           return availTimes;

       }
       public ObservableList<Appointment> getAppointments() {
           return appointments;
       }

       public ObservableList<Appointment> getAppointmentsByDate(LocalDate date) {
           ObservableList<Appointment> temp = FXCollections.observableArrayList();

           for(Appointment a : appointments){
               if(a.getDate().isEqual(date)){
                   temp.add(a);
               }
           }


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


        public Contact(int contactID,String name, String email){
            this.name.set(name);
            super.setID(contactID);
            this.email.set(email);
        }

        @Override
        public String toString() {
            return this.getName();
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getEmail() {
            return email.get();
        }

        public StringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

    }

    public class Customer extends Schedulable{

        private StringProperty name = new SimpleStringProperty();
        private StringProperty address = new SimpleStringProperty();
        private IntegerProperty countryID = new SimpleIntegerProperty();
        private IntegerProperty divisionID = new SimpleIntegerProperty();
        private StringProperty phone = new SimpleStringProperty();
        private StringProperty postalCode = new SimpleStringProperty();
        private StringProperty firstLevelString = new SimpleStringProperty();
        private StringProperty countryString = new SimpleStringProperty();

        public Customer(int customerID,String name,String address, int countryID,int divisionID, String phone, String postalCode){
            super.setID(customerID);
            this.name.set(name);
            this.address.set(address);
            this.countryID.set(countryID);
            this.divisionID.set(divisionID);
            this.phone.set(phone);
            this.postalCode.set(postalCode);
        }

        @Override
        public String toString() {
            return Integer.toString(this.getID());
        }


        public String getFirstLevelString() {
            return firstLevelString.get();
        }

        public StringProperty firstLevelStringProperty() {
            return firstLevelString;
        }

        public void setFirstLevelString(String firstLevelString) {
            this.firstLevelString.set(firstLevelString);
        }

        public String getCountryString() {
            return countryString.get();
        }

        public StringProperty countryStringProperty() {
            return countryString;
        }

        public void setCountryString(String countryString) {
            this.countryString.set(countryString);
        }


        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getAddress() {
            return address.get();
        }

        public StringProperty addressProperty() {
            return address;
        }

        public void setAddress(String address) {
            this.address.set(address);
        }

        public int getCountryID() {
            return countryID.get();
        }

        public IntegerProperty countryIDProperty() {
            return countryID;
        }

        public void setCountryID(int countryID) {
            this.countryID.set(countryID);
        }

        public int getDivisionID() {
            return divisionID.get();
        }

        public IntegerProperty divisionIDProperty() {
            return divisionID;
        }

        public void setDivisionID(int divisionID) {
            this.divisionID.set(divisionID);
        }

        public String getPhone() {
            return phone.get();
        }

        public StringProperty phoneProperty() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone.set(phone);
        }

        public String getPostalCode() {
            return postalCode.get();
        }

        public StringProperty postalCodeProperty() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode.set(postalCode);
        }
    }

    public class User extends Schedulable {
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


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
                    firstLevelDivisions.put(divisionID,divisionName);



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
            user = users.get(0);



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
                    customer.setCountryString(countries.get(customer.getCountryID()));
                    customer.setFirstLevelString(firstLevelDivisions.get(customer.getDivisionID()));
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


    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(ObservableList<User> users) {
        this.users = users;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<Contact> contacts) {
        this.contacts = contacts;
    }

    public static User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Timestamp convertToUTCStamp(LocalDate date, LocalTime time){

        ZonedDateTime temp = LocalDateTime.of(date,time).atZone(ZoneId.systemDefault());
        ZonedDateTime utc = temp.withZoneSameInstant(ZoneId.of("UTC"));

        return Timestamp.valueOf(utc.toLocalDateTime());

    }


}
