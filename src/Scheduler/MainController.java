package Scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class MainController {

    Scheduler scheduler;
    Scheduler.Appointment selectedAppointment;
    Scheduler.Customer selectedCustomer;


    private Scheduler.User user;
    private ObservableList<Scheduler.Appointment> appointmentObservableList;

    // appointments
    @FXML
    private TableView<Scheduler.Appointment> userAppointments;
    @FXML
    private TableColumn<Scheduler.Appointment,Integer> appointmentID;

    @FXML
    private  TableColumn<Scheduler.Appointment,String> titleColumn;

    @FXML
    private TableColumn<Scheduler.Appointment,String> descriptionColumn;

    @FXML
    private TableColumn<Scheduler.Appointment, String> locationColumn;

    @FXML
    private TableColumn<Scheduler.Appointment,String> contactColumn;

    @FXML
    private TableColumn<Scheduler.Appointment,String> typeColumn;

    @FXML
    TableColumn<Scheduler.Appointment, LocalTime> startColumn;

    @FXML
    private TableColumn<Scheduler.Appointment, LocalTime> endColumn;

    @FXML
    private TableColumn<Scheduler.Appointment,Integer> customerIDColumn;

    @FXML
    private TableColumn<Scheduler.Appointment, LocalDate> dateColumn;

    @FXML
    private TableView<Scheduler.Customer> customerTableView;

    @FXML
    private TableColumn<Scheduler.Customer,Integer> customersIDColumn;
    @FXML
    private TableColumn<Scheduler.Customer,String> customersNameColumn;

    @FXML
    private TableColumn<Scheduler.Customer,String> customersAddressColumn;

    @FXML
    private TableColumn<Scheduler.Customer,String> customersPostalCodeColumn;

    @FXML
    private TableColumn<Scheduler.Customer,String> customersPhoneColumn;

    @FXML
    private TableColumn<Scheduler.Customer,String> customersFirstLevelColumn;
    @FXML
    private TableColumn<Scheduler.Customer,String> customersCountryColumn;

    @FXML
    private ToggleGroup sortToggle;

    @FXML
    private RadioButton allButton;
    @FXML
    private RadioButton weeklyButton;
    @FXML
    private RadioButton monthlyButton;

    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;





    @FXML
    private void initialize(){

        //appointments tableview setup
        appointmentID.setCellValueFactory(cellData -> cellData.getValue().appointmentIDProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().getContact().nameProperty());
        typeColumn.setCellValueFactory(cellData->cellData.getValue().typeProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        customerIDColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer().IDProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        //customers tableview setup

        customersIDColumn.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
        customersNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customersAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        customersPostalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        customersFirstLevelColumn.setCellValueFactory(cellData -> cellData.getValue().firstLevelStringProperty());
        customersCountryColumn.setCellValueFactory(cellData -> cellData.getValue().countryStringProperty());
        customersPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        userAppointments.setRowFactory(tableView -> {
            TableRow<Scheduler.Appointment> row = new TableRow<Scheduler.Appointment>();
            row.setOnMouseClicked(mouseEvent -> {
                if(!row.isEmpty() && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    selectedAppointment = row.getItem();
                    System.out.println(selectedAppointment);
                }
                 else{
                    userAppointments.getSelectionModel().clearSelection();
                    selectedAppointment = null;
                }

            });
            return row;
        });

        customerTableView.setRowFactory(tableView -> {
            TableRow<Scheduler.Customer> row = new TableRow<Scheduler.Customer>();
            row.setOnMouseClicked(mouseEvent -> {
                if(!row.isEmpty() && mouseEvent.getButton() == MouseButton.PRIMARY){
                selectedCustomer = row.getItem();



                System.out.println(selectedCustomer);}
                else{
                    customerTableView.getSelectionModel().clearSelection();
                    selectedAppointment = null;
                }

            });
            return row;
        });




    }


    public void setMainApp(Scheduler scheduler) {
        this.scheduler = scheduler;

        // Add observable list data to the tables
        user = scheduler.getUser();
        appointmentObservableList = user.getAppointments();
        userAppointments.setItems(appointmentObservableList);
        customerTableView.setItems(scheduler.getCustomers());

    }

    public void setAllAppointments(){
        userAppointments.setItems(appointmentObservableList);
    }

    public void setMonthAppointments(){

        ObservableList<Scheduler.Appointment> temp = FXCollections.observableArrayList();
        for(Scheduler.Appointment a : appointmentObservableList){
            if(a.getDate().getMonth() == LocalDate.now().getMonth()
            && a.getDate().getYear() == LocalDate.now().getYear()){
                temp.add(a);
            }
        }

        userAppointments.setItems(temp);

    }

    public void setWeekAppointments(){

        LocalDate monday = LocalDate.now();
        LocalDate sunday = LocalDate.now();
        ObservableList<Scheduler.Appointment> temp = FXCollections.observableArrayList();

        while(monday.getDayOfWeek() != DayOfWeek.MONDAY){
           monday = monday.minusDays(1);
            System.out.println(monday);
        }
        while(sunday.getDayOfWeek() != DayOfWeek.SUNDAY){
            sunday = sunday.plusDays(1);
        }

        for(Scheduler.Appointment a : appointmentObservableList){
            if(a.getDate().isAfter(monday.minusDays(1))
                    && a.getDate().isBefore(sunday.plusDays(1))){
                temp.add(a);
            }
        }

        userAppointments.setItems(temp);



    }

    public void onAppointmentDeleteClicked() {

        if (selectedAppointment != null) {
            selectedAppointment.deleteFromDB();
            selectedAppointment.deleteAppointment();
            appointmentObservableList.remove(selectedAppointment);
        } else {
            selectAppointmentPopup();
        }
    }
    public void onAppointmentModifyClicked() {

        if (selectedAppointment != null) {
            selectedAppointment.deleteAppointment();
            appointmentObservableList.remove(selectedAppointment);
        } else {
            selectAppointmentPopup();
        }
    }
    public void onAppointmentAddClicked() {

        showAddAppointmentWindow();

    }





    public void selectAppointmentPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Please Select an Appointment.");

        alert.showAndWait();
        }


    public void showAddAppointmentWindow() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        stage.setTitle(user.getUsername() + "'s Appointments" );
        stage.setScene(new Scene(root));
        stage.show();




    }

}
