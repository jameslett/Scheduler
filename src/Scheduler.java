import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

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

    }

    public class Appointment{
        private ObjectProperty<User> user = new SimpleObjectProperty<User>();
        private ObjectProperty<Customer> customer = new SimpleObjectProperty<Customer>();
        private ObjectProperty<LocalDate> startTime = new SimpleObjectProperty<LocalDate>();
        private ObjectProperty<LocalDate> endTime = new SimpleObjectProperty<LocalDate>();
        private StringProperty title = new SimpleStringProperty();
        private StringProperty description = new SimpleStringProperty();
        private StringProperty location = new SimpleStringProperty();
        private ObjectProperty<Contact> contact = new SimpleObjectProperty<Contact>();
        private IntegerProperty appointmentID = new SimpleIntegerProperty();



    public class Contact{
        private StringProperty name = new SimpleStringProperty();
        private StringProperty email = new SimpleStringProperty();
        private IntegerProperty contactID = new SimpleIntegerProperty();
    }

    public class Customer{
        private IntegerProperty customerID = new SimpleIntegerProperty();
        private StringProperty name = new SimpleStringProperty();
        private StringProperty address = new SimpleStringProperty();
        private IntegerProperty countryID = new SimpleIntegerProperty();
        private StringProperty phone = new SimpleStringProperty();

    }

    public class User {
        private String username;
        private String password;
        private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.parse("8:00");
        LocalTime endTime = LocalTime.parse("22:00");

    }



}}
