package Scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.*;
import java.util.Optional;


public class MainController {

    private Scheduler scheduler;
    private Scheduler.Appointment selectedAppointment;
    private Scheduler.Appointment tempAppointment;
    private Scheduler.Customer selectedCustomer;
    private Scheduler.Customer tempCustomer;
    private Stage appointmentStage;
    private Stage customerStage;
    private  AddAppointmentController appointmentController;
    private AddCustomerController customerController;


    private Scheduler.User user;
    private ObservableList<Scheduler.Appointment> appointmentObservableList = FXCollections.observableArrayList();

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
    private Button addCustomerButton;
    @FXML TextArea reportArea = new TextArea();







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
            selectedAppointment = null;
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information Dialog");
            alert2.setHeaderText(null);
            alert2.setContentText("Appointment Deleted");

            alert2.showAndWait();


        } else {
            selectAppointmentPopup();
        }
    }

    public void onCustomerDeleteClicked() {



        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING,"", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Warning!");
            alert.setContentText("Are you sure you want to delete " +selectedCustomer.getName() +
                    "?\nDeleting this customer will delete all of their appointments!");


            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES){

                selectedCustomer.deleteAllAppointments();

                selectedCustomer.deleteFromDB();
                scheduler.getCustomers().remove(selectedCustomer);
                selectedCustomer = null;
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Information Dialog");
                alert2.setHeaderText(null);
                alert2.setContentText("Customer Deleted");

                alert2.showAndWait();

            } else {
                alert.close();
            }
        } else {
            selectCustomerPopup();
        }
    }

    public void onAppointmentModifyClicked() {

        if (selectedAppointment != null) {
            showAddAppointmentWindow();
            appointmentController.getAppointmentIDTextField().setText(Integer.toString(selectedAppointment.getAppointmentID()));
            appointmentController.getAppointmentTitleTextField().setText(selectedAppointment.getTitle());
            appointmentController.getAppointmentDescriptionTextField().setText(selectedAppointment.getDescription());
            appointmentController.getAppointmentLocationTextField().setText(selectedAppointment.getLocation());
            appointmentController.getAppointmentTypeTextField().setText(selectedAppointment.getType());
            appointmentController.getAppointmentUserIDComboBox().getSelectionModel().select(selectedAppointment.getUser());
            appointmentController.getAppointmentContactComboBox().getSelectionModel().select(selectedAppointment.getContact());
            appointmentController.getAppointmentCustomerIDComboBox().getSelectionModel().select(selectedAppointment.getCustomer());

            appointmentController.getAppointmentDatePicker().setValue(selectedAppointment.getDate());

            appointmentController.getAppointmentStartTimeComboBox().getSelectionModel().select(selectedAppointment.getStartTime());
            appointmentController.getAppointmentEndTimeComboBox().getSelectionModel().select(selectedAppointment.getEndTime());




        } else {
            selectAppointmentPopup();
        }
    }

    public void onCustomerModifyClicked() {

        if (selectedCustomer != null) {
            showAddCustomerWindow();
            customerController.getCustomerIDTextField().setText(Integer.toString(selectedCustomer.getID()));
            customerController.getCustomerNameTextField().setText(selectedCustomer.getName());
            customerController.getCustomerAddressTextField().setText(selectedCustomer.getAddress());
            customerController.getCustomerPostalTextField().setText(selectedCustomer.getPostalCode());
            customerController.getCustomerPhoneTextField().setText(selectedCustomer.getPhone());
            customerController.getCustomerCountryComboBox().getSelectionModel().select(selectedCustomer.getCountryString());
            customerController.getCustomerFirstLevelComboBox().getSelectionModel().select(scheduler.getDivision(selectedCustomer.getDivisionID()));



        } else {
            selectCustomerPopup();
        }
    }
    public void onAppointmentAddClicked() {

        showAddAppointmentWindow();
        selectedAppointment = null;

    }





    public void selectAppointmentPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Please Select an Appointment.");

        alert.showAndWait();
        }

    public void selectCustomerPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Please Select a Customer.");

        alert.showAndWait();
    }



    public void showAddAppointmentWindow() {

        System.out.println(appointmentStage);
        createAppointmentStage();
        appointmentStage.show();

        appointmentController.getAppointmentIDTextField().setText(generateAppointmentID());
        appointmentController.getAppointmentContactComboBox().setItems(scheduler.getContacts());
        appointmentController.getAppointmentCustomerIDComboBox().setItems(scheduler.getCustomers());
        appointmentController.getAppointmentUserIDComboBox().setItems(scheduler.getUsers());








    }

    public void showAddCustomerWindow(){
        createCustomerStage();
        customerController.getCustomerCountryComboBox().setItems(scheduler.getCountryNames());
        customerController.getCustomerCountryComboBox().getSelectionModel().select(0);
        customerController.getCustomerFirstLevelComboBox().setItems(scheduler.getDivisionsByCountry(customerController.getCustomerCountryComboBox().getSelectionModel().getSelectedItem()));
        customerController.getCustomerFirstLevelComboBox().getSelectionModel().select(0);
        customerController.getCustomerIDTextField().setText(generateCustomerID());
        customerStage.show();
    }


    public void createAppointmentStage(){
        if(appointmentStage == null) {
            appointmentStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(root);

            appointmentController = loader.getController();
            appointmentController.setController(this);
            appointmentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                appointmentController.clearFields();
                selectedAppointment = null;

                }
            });
            appointmentStage.initModality(Modality.APPLICATION_MODAL);
            appointmentStage.setTitle(user.getUsername() + "'s Appointments");
            appointmentStage.setScene(new Scene(root));
        }
    }
    public void createCustomerStage(){
        if(customerStage == null) {
            customerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(getClass().getResource("addCustomer.fxml"));

            customerController = loader.getController();
            customerController.setController(this);
            customerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    customerController.clearFields();
                    selectedCustomer = null;

                }
            });
            customerStage.initModality(Modality.APPLICATION_MODAL);
            customerStage.setTitle(user.getUsername() + "'s Appointments");
        customerStage.setScene(new Scene(root));
        }
    }

    public String generateAppointmentID(){

        System.out.println(appointmentObservableList.size());
        if(appointmentObservableList.size()>0){
        return Integer.toString( appointmentObservableList.get(appointmentObservableList.size()-1).getAppointmentID()+1);}
        else{
            return "1";
        }
    }

    public String generateCustomerID(){


        if(scheduler.getCustomers().size()>0){
            return Integer.toString( scheduler.getCustomers().get(scheduler.getCustomers().size()-1).getID()+1);}
        else{
            return "1";
        }
    }


    public void addAppointment(Scheduler.Appointment appointment){
        appointmentObservableList.add(appointment);

    }


    public void saveAppointment(){
        LocalDateTime start = LocalDateTime.of(appointmentController.getSelectedDate(), appointmentController.getStartTime());
        LocalDateTime end = LocalDateTime.of(appointmentController.getSelectedDate(), appointmentController.getEndTime());
        if(selectedAppointment != null) {

            selectedAppointment.deleteAppointment();
            selectedAppointment.deleteFromDB();
            appointmentObservableList.remove(selectedAppointment);
        }

        tempAppointment = new Scheduler.Appointment(appointmentController.getAppointmentID(), start, end, appointmentController.getSelectedCustomer(), appointmentController.getSelectedContact(), appointmentController.getSelectedUser(), appointmentController.getType(), appointmentController.getTitle(), appointmentController.getDescription(), appointmentController.getLocation());
        tempAppointment.addToDB();
        appointmentController.clearFields();
        selectedAppointment = null;
        appointmentStage.close();


    }



    public void saveCustomer(){


        if(selectedCustomer != null) {


            selectedCustomer.deleteFromDB();
            scheduler.getCustomers().remove(selectedCustomer);

        }


        AddCustomerController cc = customerController;



        tempCustomer = scheduler.createCustomer(cc.getCustomerID(),cc.getName(),cc.getAddress(),cc.getFirstLevel().getCountryID(),cc.getFirstLevel().getFirstLevelID(),cc.getPhone(),cc.getPostal());
        tempCustomer.addToDB();
        scheduler.getCustomers().add(tempCustomer);

        customerController.clearFields();
        selectedCustomer = null;
        customerStage.close();

    }

    public Scheduler getMainApp() {
        return scheduler;
    }

    public Scheduler.Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Scheduler.Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public void onAddCustomerClicked(){
        showAddCustomerWindow();
    }

    public ObservableList<Scheduler.Appointment> getAppointmentObservableList() {
        return appointmentObservableList;
    }

    public void setAppointmentObservableList(ObservableList<Scheduler.Appointment> appointmentObservableList) {
        this.appointmentObservableList = appointmentObservableList;
    }
    public void onReportTab(){
        reportArea.clear();
        reportArea.appendText("Appointments By month and type :\n");
        for(Month m : getMonths()){
            reportArea.appendText(m.toString()+"\nBy Type:\n");
            for(String s : getTypes()){
                reportArea.appendText(s + " :" +getNumberOfAppointmentsByMonthAndType(m,s)+ "\n");
            }
        }
        reportArea.appendText("\nUser Schedules :\n");
        for(Scheduler.User u : scheduler.getUsers()){
            reportArea.appendText("\n"+u.getUsername() + "s appointments:\n\n");
            //appointment ID, title, type and description, start date and time, end date and time, and customer ID
            for(Scheduler.Appointment a : u.getAppointments()){
                reportArea.appendText("\nAppointment ID: " + a.getAppointmentID());
                reportArea.appendText("\nTitle: " + a.getTitle());
                reportArea.appendText("\nDate: " + a.getDate());
                reportArea.appendText("\nStart Time:  " + a.getStartTime());
                reportArea.appendText("\nEnd Time: " + a.getEndTime());
                reportArea.appendText("\nCustomerID: " + a.getCustomer().getID() + "\n");
            }
        }
        reportArea.appendText("Report 3 Total Number of appointments :\n" + getAppointmentObservableList().size());

    }
    public ObservableList<String> getTypes(){
        ObservableList<String> temp = FXCollections.observableArrayList();

        for(Scheduler.Appointment a : appointmentObservableList ){
            if(!temp.contains(a.getType())){
                temp.add(a.getType());
            }
        }
        return temp;
    }

    public ObservableList<Month> getMonths() {
        ObservableList<Month> temp = FXCollections.observableArrayList();

        for(Scheduler.Appointment a :appointmentObservableList ){
            if(!temp.contains(a.getDate().getMonth())){
                temp.add(a.getDate().getMonth());
            }
        }

        return temp;
    }

    public int getNumberOfAppointmentsByMonthAndType(Month month,String type) {

        int temp = 0;
        for(Scheduler.Appointment a :appointmentObservableList ){
            if(a.getDate().getMonth().equals(month) && a.getType().equals(type)){
                temp++;
            }
        }

        return temp;
    }
}
