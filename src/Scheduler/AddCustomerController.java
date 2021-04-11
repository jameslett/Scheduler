package Scheduler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class AddCustomerController {

    @FXML
    private TextField customerIDTextField = new TextField();
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerPostalTextField;
    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<String> customerCountryComboBox = new ComboBox();
    @FXML
    private ComboBox<Scheduler.FirstLevelDivision> customerFirstLevelComboBox = new ComboBox<>();




    String name;
    String address;
    String postal;
    String phone;
    Integer customerID;
    Stage stage;
    String country;
    Scheduler.FirstLevelDivision firstLevel;


            MainController controller;

    public MainController getController() {
        return controller;
    }

    public void setController(MainController controller) {
        this.controller = controller;

    }
    @FXML
    private void initialize(){






    }


    public TextField getCustomerIDTextField() {
        return customerIDTextField;
    }

    public void setCustomerIDTextField(TextField customerIDTextField) {
        this.customerIDTextField = customerIDTextField;
    }

    public TextField getCustomerNameTextField() {
        return customerNameTextField;
    }

    public void setCustomerNameTextField(TextField customerNameTextField) {
        this.customerNameTextField = customerNameTextField;
    }

    public TextField getCustomerAddressTextField() {
        return customerAddressTextField;
    }

    public void setCustomerAddressTextField(TextField customerAddressTextField) {
        this.customerAddressTextField = customerAddressTextField;
    }

    public TextField getCustomerPostalTextField() {
        return customerPostalTextField;
    }

    public void setCustomerPostalTextField(TextField customerPostalTextField) {
        this.customerPostalTextField = customerPostalTextField;
    }

    public TextField getCustomerPhoneTextField() {
        return customerPhoneTextField;
    }

    public void setCustomerPhoneTextField(TextField customerPhoneTextField) {
        this.customerPhoneTextField = customerPhoneTextField;
    }

    public ComboBox<String> getCustomerCountryComboBox() {
        return customerCountryComboBox;
    }

    public void setCustomerCountryComboBox(ComboBox<String> customerCountryComboBox) {
        this.customerCountryComboBox = customerCountryComboBox;
    }

    public ComboBox<Scheduler.FirstLevelDivision> getCustomerFirstLevelComboBox() {
        return customerFirstLevelComboBox;
    }

    public void setCustomerFirstLevelComboBox(ComboBox<Scheduler.FirstLevelDivision> customerFirstLevelComboBox) {
        this.customerFirstLevelComboBox = customerFirstLevelComboBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Scheduler.FirstLevelDivision getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(Scheduler.FirstLevelDivision firstLevel) {
        this.firstLevel = firstLevel;
    }



    public void onSaveClicked(){

        name = customerNameTextField.getText();
        address = customerAddressTextField.getText();
        postal = customerPostalTextField.getText();
        phone = customerPhoneTextField.getText();
        customerID = Integer.parseInt(customerIDTextField.getText());
        country = customerCountryComboBox.getSelectionModel().getSelectedItem();
        firstLevel = customerFirstLevelComboBox.getSelectionModel().getSelectedItem();

        phone = phone.replaceAll("[-()]","");

        int length = phone.length();

        if(name.isEmpty() ||
           address.isEmpty()||
           postal.isEmpty() ||
           phone.isEmpty()||
           customerID == null ||
           country.isEmpty() ||
           firstLevel == null
){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all fields before saving!");

            alert.showAndWait();


        }

        else if (!phone.matches("[0-9]+")){

            System.out.println("digits");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please use 1-123-456-7890 format for phone number");

            alert.showAndWait();

        }
        else if(length <11 || length > 12){
            System.out.println("length");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please use 1-123-456-7890 format for phone number");

            alert.showAndWait();
        }





    else {

            phone = phone.substring(0,phone.length()-10) + "-" + phone.substring(phone.length()-10 ,phone.length()-7)+ "-" + phone.substring(phone.length()-7,phone.length()-4) + "-" + phone.substring(phone.length()-4,phone.length());
            System.out.println(phone);
        controller.saveCustomer();}
    }








    public void clearFields(){

            customerIDTextField.clear();
            customerNameTextField.clear();
            customerAddressTextField.clear();
            customerPostalTextField.clear();
            customerPhoneTextField.clear();

          customerCountryComboBox.getSelectionModel().clearSelection();
          customerFirstLevelComboBox.getSelectionModel().clearSelection();


    }

    public void onCountryClicked()
    {
        getCustomerFirstLevelComboBox().setItems(controller.getMainApp().getDivisionsByCountry(getCustomerCountryComboBox().getSelectionModel().getSelectedItem()));
    }
}
