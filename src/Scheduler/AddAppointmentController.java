package Scheduler;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class AddAppointmentController {

    @FXML
    private TextField appointmentIDTextField = new TextField();
    @FXML
    private TextField appointmentTitleTextField;
    @FXML
    private TextField appointmentDescriptionTextField;
    @FXML
    private TextField appointmentLocationTextField;
    @FXML
    private TextField appointmentCustomerNameTextField;
    @FXML
    private TextField appointmentUserNameTextField;
    @FXML
    private TextField appointmentTypeTextField;
    @FXML
    private ComboBox<Scheduler.Contact> appointmentContactComboBox = new ComboBox();
    @FXML
    private ComboBox<Scheduler.Customer> appointmentCustomerIDComboBox;
    @FXML
    private ComboBox<Scheduler.User> appointmentUserIDComboBox;
    @FXML
    private ComboBox<LocalTime> appointmentStartTimeComboBox;
    @FXML
    private ComboBox<LocalTime> appointmentEndTimeComboBox;
    @FXML
    private DatePicker appointmentDatePicker;


    Scheduler.User selectedUser = null;
    Scheduler.Customer selectedCustomer = null;
    Scheduler.Contact selectedContact = null;
    LocalTime startTime = null;
    LocalTime endTime = null;
    LocalDate selectedDate = null;
    String title;
    String description;
    String location;
    String type;
    Integer appointmentID;
    Stage stage;

            MainController controller;

    public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;

    }
    @FXML
    private void initialize(){

    appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean bool) {
            super.updateItem(date, bool);
            LocalDate today;
            if(LocalTime.now().isAfter(LocalTime.of(21,44))) {
                today  = LocalDate.now().plusDays(1);
            }
            else{
                today = LocalDate.now();
            }
            setDisable(bool || date.compareTo(today) < 0 );
        }




    });



    }


    public TextField getAppointmentIDTextField() {
        return appointmentIDTextField;
    }

    public void setAppointmentIDTextField(TextField appointmentIDTextField) {
        this.appointmentIDTextField = appointmentIDTextField;
    }

    public TextField getAppointmentTitleTextField() {
        return appointmentTitleTextField;
    }

    public void setAppointmentTitleTextField(TextField appointmentTitleTextField) {
        this.appointmentTitleTextField = appointmentTitleTextField;
    }

    public TextField getAppointmentDescriptionTextField() {
        return appointmentDescriptionTextField;
    }

    public void setAppointmentDescriptionTextField(TextField appointmentDescriptionTextField) {
        this.appointmentDescriptionTextField = appointmentDescriptionTextField;
    }

    public TextField getAppointmentLocationTextField() {
        return appointmentLocationTextField;
    }

    public void setAppointmentLocationTextField(TextField appointmentLocationTextField) {
        this.appointmentLocationTextField = appointmentLocationTextField;
    }

    public TextField getAppointmentCustomerNameTextField() {
        return appointmentCustomerNameTextField;
    }

    public void setAppointmentCustomerNameTextField(TextField appointmentCustomerNameTextField) {
        this.appointmentCustomerNameTextField = appointmentCustomerNameTextField;
    }

    public TextField getAppointmentUserNameTextField() {
        return appointmentUserNameTextField;
    }

    public void setAppointmentUserNameTextField(TextField appointmentUserNameTextField) {
        this.appointmentUserNameTextField = appointmentUserNameTextField;
    }

    public TextField getAppointmentTypeTextField() {
        return appointmentTypeTextField;
    }

    public void setAppointmentTypeTextField(TextField appointmentTypeTextField) {
        this.appointmentTypeTextField = appointmentTypeTextField;
    }

    public ComboBox getAppointmentContactComboBox() {
        return appointmentContactComboBox;
    }

    public void setAppointmentContactComboBox(ComboBox appointmentContactComboBox) {
        this.appointmentContactComboBox = appointmentContactComboBox;
    }

    public ComboBox getAppointmentCustomerIDComboBox() {
        return appointmentCustomerIDComboBox;
    }

    public void setAppointmentCustomerIDComboBox(ComboBox appointmentCustomerIDComboBox) {
        this.appointmentCustomerIDComboBox = appointmentCustomerIDComboBox;
    }

    public ComboBox getAppointmentUserIDComboBox() {
        return appointmentUserIDComboBox;
    }

    public void setAppointmentUserIDComboBox(ComboBox appointmentUserIDComboBox) {
        this.appointmentUserIDComboBox = appointmentUserIDComboBox;
    }

    public ComboBox getAppointmentStartTimeComboBox() {
        return appointmentStartTimeComboBox;
    }

    public void setAppointmentStartTimeComboBox(ComboBox appointmentStartTimeComboBox) {
        this.appointmentStartTimeComboBox = appointmentStartTimeComboBox;
    }

    public ComboBox getAppointmentEndTimeComboBox() {
        return appointmentEndTimeComboBox;
    }

    public void setAppointmentEndTimeComboBox(ComboBox appointmentEndTimeComboBox) {
        this.appointmentEndTimeComboBox = appointmentEndTimeComboBox;
    }

    public DatePicker getAppointmentDatePicker() {
        return appointmentDatePicker;
    }

    public void setAppointmentDatePicker(DatePicker appointmentDatePicker) {
        this.appointmentDatePicker = appointmentDatePicker;

    }

    public void onSaveClicked(){

        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        type = appointmentTypeTextField.getText();
        location = appointmentLocationTextField.getText();
        appointmentID = Integer.parseInt(appointmentIDTextField.getText());

        if(title.isEmpty() ||
           description.isEmpty()||
           type.isEmpty() ||
           location.isEmpty()||
           selectedContact == null||
           selectedUser == null||
           selectedCustomer == null ||
            selectedDate == null ||
            startTime == null ||
        endTime == null){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all fields before saving!");

            alert.showAndWait();


        }

        else if(startTime.isBefore(LocalTime.now()) && selectedDate.isEqual(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please select a time before the current time!");

            alert.showAndWait();
            appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(),selectedDate));
            appointmentStartTimeComboBox.getSelectionModel().select(0);
            startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
            appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate));
            appointmentEndTimeComboBox.getSelectionModel().select(0);
            endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();

        }



    else {
        startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
        endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();
        selectedUser = appointmentUserIDComboBox.getSelectionModel().getSelectedItem();
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        selectedCustomer = appointmentCustomerIDComboBox.getSelectionModel().getSelectedItem();
        selectedDate = appointmentDatePicker.getValue();
        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        location = appointmentLocationTextField.getText();
        type = appointmentTypeTextField.getText();
        appointmentID =Integer.parseInt(appointmentIDTextField.getText());




        controller.saveAppointment();}
    }

    public void onCustomerIDComboBoxAction(){

        if(appointmentCustomerIDComboBox.getSelectionModel().getSelectedItem() != null) {
            appointmentCustomerNameTextField.setText(appointmentCustomerIDComboBox.getSelectionModel().getSelectedItem().getName());
            selectedCustomer = appointmentCustomerIDComboBox.getSelectionModel().getSelectedItem();
            appointmentDatePicker.setDisable(!allSelected());
        }
        if(allSelected()) {
            appointmentDatePicker.setDisable(!allSelected());
            if(appointmentDatePicker.getValue() != null){

                updateDatesAndTimes();}
        }
    }





    public void onUserIDComboBoxAction(){

        if(appointmentUserIDComboBox.getSelectionModel().getSelectedItem() != null){
        selectedUser = appointmentUserIDComboBox.getSelectionModel().getSelectedItem();
        appointmentUserNameTextField.setText(appointmentUserIDComboBox.getSelectionModel().getSelectedItem().getUsername());
    }
        if(allSelected()) {
            appointmentDatePicker.setDisable(!allSelected());
            if(appointmentDatePicker.getValue() != null){

                updateDatesAndTimes();}
        }
    }


    public void onContactComboBoxAction(){
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        if(allSelected()) {
            appointmentDatePicker.setDisable(!allSelected());
           if(appointmentDatePicker.getValue() != null){

            updateDatesAndTimes();}
        }
    }

    public boolean allSelected(){

        return !appointmentUserIDComboBox.getSelectionModel().isEmpty() &&
                !appointmentContactComboBox.getSelectionModel().isEmpty() &&
                !appointmentCustomerIDComboBox.getSelectionModel().isEmpty();

        }


    public void datePickerOnMouseClicked(){

        if(!allSelected()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please select a contact,user, and customer before selecting a date.");

            alert.showAndWait();

            appointmentDatePicker.setDisable(true);
        }


    }


    public void onDateSelected(){





       if(appointmentDatePicker.getValue() != null) {
           selectedDate = appointmentDatePicker.getValue();


           System.out.println(selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate).size());
           if (selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate).size() < 1) {

               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Information Dialog");
               alert.setHeaderText(null);
               alert.setContentText("No appointments available on this date; please select another date.");
               appointmentStartTimeComboBox.getSelectionModel().clearSelection();
               appointmentStartTimeComboBox.setItems(null);
               startTime = null;
               appointmentEndTimeComboBox.getSelectionModel().clearSelection();
               appointmentStartTimeComboBox.setItems(null);
               endTime = null;
               appointmentDatePicker.setValue(null);
               alert.showAndWait();
           } else {

               appointmentStartTimeComboBox.setDisable(false);
               appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate));
               appointmentStartTimeComboBox.getSelectionModel().select(0);
               startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
               appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(), startTime, selectedDate));
               appointmentEndTimeComboBox.getSelectionModel().select(0);
               endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();




           }
       }
    }
    public void onstartTimeSelected(){
        startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();

        if(startTime != null){


        appointmentEndTimeComboBox.setDisable(false);
        System.out.println(startTime + "THIS ONE");
        appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate));
        appointmentEndTimeComboBox.getSelectionModel().select(0);}
    }

    public void onEndTimeSelected(){
        endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();
    }

    public void onTitleAction(){
        System.out.println("action!");
    }

    public ArrayList<Scheduler.Schedulable> getPartyArrayList(){
        ArrayList<Scheduler.Schedulable> temp = new ArrayList();
        temp.add(selectedContact);
        temp.add(selectedUser);
        temp.add(selectedCustomer);

        return temp;

    }


    public void updateDatesAndTimes(){
        if(allSelected()){


            appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(),selectedDate));

            if(!appointmentStartTimeComboBox.getItems().contains(startTime)){
                appointmentStartTimeComboBox.getSelectionModel().select(0);

            }
            startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
            appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate));
            appointmentEndTimeComboBox.getSelectionModel().select(0);
            endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();
        }
    }

    public Scheduler.User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Scheduler.User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Scheduler.Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Scheduler.Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public Scheduler.Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Scheduler.Contact selectedContact) {
        this.selectedContact = selectedContact;
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

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void clearFields(){
        getAppointmentDatePicker().setValue(null);
        getAppointmentCustomerNameTextField().clear();
        getAppointmentEndTimeComboBox().setItems(null);
        getAppointmentContactComboBox().setItems(null);
        getAppointmentLocationTextField().clear();
        getAppointmentDescriptionTextField().clear();
        getAppointmentStartTimeComboBox().setItems(null);
        getAppointmentTitleTextField().clear();
        getAppointmentTypeTextField().clear();
        getAppointmentCustomerNameTextField().clear();
        getAppointmentUserIDComboBox().setItems(null);
        getAppointmentCustomerIDComboBox().setItems(null);
        getAppointmentIDTextField().clear();

        getAppointmentUserNameTextField().clear();
        getAppointmentEndTimeComboBox();
    }


}
