package Scheduler;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * The type Add appointment controller.
 */
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



    private Scheduler.User selectedUser = null;

    private Scheduler.Customer selectedCustomer = null;

    private Scheduler.Contact selectedContact = null;

    private LocalTime startTime = null;

    private LocalTime endTime = null;

    private LocalDate selectedDate = null;

    private String title;

    private String description;

    private String location;

    private String type;

    private Integer appointmentID;

    private Stage stage;


    MainController controller;

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public MainController getController() {
        return controller;
    }

    /**
     * Sets controller.
     *
     * @param controller the controller
     */
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
            setDisable(bool || (date.compareTo(today) < 0  || date.getDayOfWeek() == DayOfWeek.SATURDAY ) );


        }




    });



    }


    /**
     * Gets appointment id text field.
     *
     * @return the appointment id text field
     */
    public TextField getAppointmentIDTextField() {
        return appointmentIDTextField;
    }


    /**
     * Gets appointment title text field.
     *
     * @return the appointment title text field
     */
    public TextField getAppointmentTitleTextField() {
        return appointmentTitleTextField;
    }


    /**
     * Gets appointment description text field.
     *
     * @return the appointment description text field
     */
    public TextField getAppointmentDescriptionTextField() {
        return appointmentDescriptionTextField;
    }


    /**
     * Gets appointment location text field.
     *
     * @return the appointment location text field
     */
    public TextField getAppointmentLocationTextField() {
        return appointmentLocationTextField;
    }


    /**
     * Gets appointment customer name text field.
     *
     * @return the appointment customer name text field
     */
    public TextField getAppointmentCustomerNameTextField() {
        return appointmentCustomerNameTextField;
    }


    /**
     * Gets appointment user name text field.
     *
     * @return the appointment user name text field
     */
    public TextField getAppointmentUserNameTextField() {
        return appointmentUserNameTextField;
    }


    /**
     * Gets appointment type text field.
     *
     * @return the appointment type text field
     */
    public TextField getAppointmentTypeTextField() {
        return appointmentTypeTextField;
    }


    /**
     * Gets appointment contact combo box.
     *
     * @return the appointment contact combo box
     */
    public ComboBox getAppointmentContactComboBox() {
        return appointmentContactComboBox;
    }


    /**
     * Gets appointment customer id combo box.
     *
     * @return the appointment customer id combo box
     */
    public ComboBox getAppointmentCustomerIDComboBox() {
        return appointmentCustomerIDComboBox;
    }


    /**
     * Gets appointment user id combo box.
     *
     * @return the appointment user id combo box
     */
    public ComboBox getAppointmentUserIDComboBox() {
        return appointmentUserIDComboBox;
    }


    /**
     * Gets appointment start time combo box.
     *
     * @return the appointment start time combo box
     */
    public ComboBox getAppointmentStartTimeComboBox() {
        return appointmentStartTimeComboBox;
    }


    /**
     * Gets appointment end time combo box.
     *
     * @return the appointment end time combo box
     */
    public ComboBox getAppointmentEndTimeComboBox() {
        return appointmentEndTimeComboBox;
    }


    /**
     * Gets appointment date picker.
     *
     * @return the appointment date picker
     */
    public DatePicker getAppointmentDatePicker() {
        return appointmentDatePicker;
    }


    /**
     * On save clicked.Handles the save button action checks for empty fields
     */
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
            appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(),selectedDate, controller.getSelectedAppointment()));
            appointmentStartTimeComboBox.getSelectionModel().select(0);
            startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
            appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate, controller.getSelectedAppointment()));
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

    /**
     * On customer id combo box action. Checks to see if user is ready to select a date
     */
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


    /**
     * On user id combo box action. Checks to see if user is ready to select a date
     */
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


    /**
     * On contact combo box action. Checks to see if user is ready to select a date
     */
    public void onContactComboBoxAction(){
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        if(allSelected()) {
            appointmentDatePicker.setDisable(!allSelected());
           if(appointmentDatePicker.getValue() != null){

            updateDatesAndTimes();}
        }
    }

    /**
     * All selected boolean. Checks if all needed fields are selected for date selection.
     *
     * @return the boolean
     */
    public boolean allSelected(){

        return !appointmentUserIDComboBox.getSelectionModel().isEmpty() &&
                !appointmentContactComboBox.getSelectionModel().isEmpty() &&
                !appointmentCustomerIDComboBox.getSelectionModel().isEmpty();

        }


    /**
     * Date picker on mouse clicked. Makes sure user has selected correct fields before date
     */
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


    /**
     * On date selected. Generates start times based on the selected date
     */
    public void onDateSelected(){





       if(appointmentDatePicker.getValue() != null) {
           selectedDate = appointmentDatePicker.getValue();


           System.out.println(selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate, controller.getSelectedAppointment()).size());
           if (selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate, controller.getSelectedAppointment()).size() < 1) {

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
               appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(), selectedDate, controller.getSelectedAppointment()));
               appointmentStartTimeComboBox.getSelectionModel().select(0);
               startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
               appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(), startTime, selectedDate, controller.getSelectedAppointment()));
               appointmentEndTimeComboBox.getSelectionModel().select(0);
               endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();




           }
       }
    }

    /**
     * Onstart time selected.Generates end times based on start time
     */
    public void onstartTimeSelected(){
        startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();

        if(startTime != null){


        appointmentEndTimeComboBox.setDisable(false);
        System.out.println(startTime + "THIS ONE");
        appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate, controller.getSelectedAppointment()));
        appointmentEndTimeComboBox.getSelectionModel().select(0);}
    }

    /**
     * On end time selected.
     */
    public void onEndTimeSelected(){
        endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * On title action.
     */
    public void onTitleAction(){
        System.out.println("action!");
    }

    /**
     * Get party array list array list.
     *
     * @return the array list
     */
    public ArrayList<Scheduler.Schedulable> getPartyArrayList(){
        ArrayList<Scheduler.Schedulable> temp = new ArrayList();
        temp.add(selectedContact);
        temp.add(selectedUser);
        temp.add(selectedCustomer);

        return temp;

    }


    /**
     * Update dates and times.Updates The times and dates incase of change of customer/user/contact
     */
    public void updateDatesAndTimes(){
        if(allSelected()){


            appointmentStartTimeComboBox.setItems(selectedUser.GetAvailableStartTimes(getPartyArrayList(),selectedDate,controller.getSelectedAppointment()));

            if(!appointmentStartTimeComboBox.getItems().contains(startTime)){
                appointmentStartTimeComboBox.getSelectionModel().select(0);

            }
            startTime = appointmentStartTimeComboBox.getSelectionModel().getSelectedItem();
            appointmentEndTimeComboBox.setItems(selectedUser.GetAvailableEndTimes(getPartyArrayList(),startTime,selectedDate, controller.getSelectedAppointment()));
            appointmentEndTimeComboBox.getSelectionModel().select(0);
            endTime = appointmentEndTimeComboBox.getSelectionModel().getSelectedItem();
        }
    }

    /**
     * Gets selected user.
     *
     * @return the selected user
     */
    public Scheduler.User getSelectedUser() {
        return selectedUser;
    }

    /**
     * Sets selected user.
     *
     * @param selectedUser the selected user
     */
    public void setSelectedUser(Scheduler.User selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     * Gets selected customer.
     *
     * @return the selected customer
     */
    public Scheduler.Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Sets selected customer.
     *
     * @param selectedCustomer the selected customer
     */
    public void setSelectedCustomer(Scheduler.Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * Gets selected contact.
     *
     * @return the selected contact
     */
    public Scheduler.Contact getSelectedContact() {
        return selectedContact;
    }

    /**
     * Sets selected contact.
     *
     * @param selectedContact the selected contact
     */
    public void setSelectedContact(Scheduler.Contact selectedContact) {
        this.selectedContact = selectedContact;
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
     * Gets selected date.
     *
     * @return the selected date
     */
    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    /**
     * Sets selected date.
     *
     * @param selectedDate the selected date
     */
    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets appointment id.
     *
     * @return the appointment id
     */
    public Integer getAppointmentID() {
        return appointmentID;
    }

    /**
     * Sets appointment id.
     *
     * @param appointmentID the appointment id
     */
    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Gets stage.
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Clear fields.
     */
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
