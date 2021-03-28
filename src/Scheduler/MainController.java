package Scheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class MainController {

    Scheduler scheduler;
    @FXML
    private TableView<Scheduler.Appointment> userAppointments;
    private Scheduler.User user;

    @FXML
    TableColumn<Scheduler.Appointment,Integer> appointmentID;

    @FXML
    TableColumn<Scheduler.Appointment,String> titleColumn;

    @FXML
    TableColumn<Scheduler.Appointment,String> descriptionColumn;

    @FXML
    TableColumn<Scheduler.Appointment, String> locationColumn;

    @FXML
    TableColumn<Scheduler.Appointment,String> contactColumn;

    @FXML
    TableColumn<Scheduler.Appointment,String> typeColumn;

    @FXML
    TableColumn<Scheduler.Appointment, LocalTime> startColumn;

    @FXML
    TableColumn<Scheduler.Appointment, LocalTime> endColumn;

    @FXML
    TableColumn<Scheduler.Appointment,Integer> customerIDColumn;

    @FXML
    TableColumn<Scheduler.Appointment, LocalDate> dateColumn;


    @FXML
    private void initialize(){
        appointmentID.setCellValueFactory(cellData -> cellData.getValue().appointmentIDProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().getContact().nameProperty());
        typeColumn.setCellValueFactory(cellData->cellData.getValue().typeProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        customerIDColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer().customerIDProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

    }


    public void setMainApp(Scheduler scheduler) {
        this.scheduler = scheduler;

        // Add observable list data to the tables
        user = scheduler.getUser();

        userAppointments.setItems(user.getAppointments());

    }

}
