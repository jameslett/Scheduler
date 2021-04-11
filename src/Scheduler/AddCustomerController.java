package Scheduler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * The type Add customer controller.
 */
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



  private  String name;

    private String address;

    private String postal;

    private String phone;

    private Integer customerID;
    private Stage stage;

    private String country;

    private Scheduler.FirstLevelDivision firstLevel;



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






    }


    /**
     * Gets customer id text field.
     *
     * @return the customer id text field
     */
    public TextField getCustomerIDTextField() {
        return customerIDTextField;
    }

    /**
     * Sets customer id text field.
     *
     * @param customerIDTextField the customer id text field
     */
    public void setCustomerIDTextField(TextField customerIDTextField) {
        this.customerIDTextField = customerIDTextField;
    }

    /**
     * Gets customer name text field.
     *
     * @return the customer name text field
     */
    public TextField getCustomerNameTextField() {
        return customerNameTextField;
    }

    /**
     * Sets customer name text field.
     *
     * @param customerNameTextField the customer name text field
     */
    public void setCustomerNameTextField(TextField customerNameTextField) {
        this.customerNameTextField = customerNameTextField;
    }

    /**
     * Gets customer address text field.
     *
     * @return the customer address text field
     */
    public TextField getCustomerAddressTextField() {
        return customerAddressTextField;
    }

    /**
     * Sets customer address text field.
     *
     * @param customerAddressTextField the customer address text field
     */
    public void setCustomerAddressTextField(TextField customerAddressTextField) {
        this.customerAddressTextField = customerAddressTextField;
    }

    /**
     * Gets customer postal text field.
     *
     * @return the customer postal text field
     */
    public TextField getCustomerPostalTextField() {
        return customerPostalTextField;
    }

    /**
     * Sets customer postal text field.
     *
     * @param customerPostalTextField the customer postal text field
     */
    public void setCustomerPostalTextField(TextField customerPostalTextField) {
        this.customerPostalTextField = customerPostalTextField;
    }

    /**
     * Gets customer phone text field.
     *
     * @return the customer phone text field
     */
    public TextField getCustomerPhoneTextField() {
        return customerPhoneTextField;
    }

    /**
     * Sets customer phone text field.
     *
     * @param customerPhoneTextField the customer phone text field
     */
    public void setCustomerPhoneTextField(TextField customerPhoneTextField) {
        this.customerPhoneTextField = customerPhoneTextField;
    }

    /**
     * Gets customer country combo box.
     *
     * @return the customer country combo box
     */
    public ComboBox<String> getCustomerCountryComboBox() {
        return customerCountryComboBox;
    }

    /**
     * Sets customer country combo box.
     *
     * @param customerCountryComboBox the customer country combo box
     */
    public void setCustomerCountryComboBox(ComboBox<String> customerCountryComboBox) {
        this.customerCountryComboBox = customerCountryComboBox;
    }

    /**
     * Gets customer first level combo box.
     *
     * @return the customer first level combo box
     */
    public ComboBox<Scheduler.FirstLevelDivision> getCustomerFirstLevelComboBox() {
        return customerFirstLevelComboBox;
    }

    /**
     * Sets customer first level combo box.
     *
     * @param customerFirstLevelComboBox the customer first level combo box
     */
    public void setCustomerFirstLevelComboBox(ComboBox<Scheduler.FirstLevelDivision> customerFirstLevelComboBox) {
        this.customerFirstLevelComboBox = customerFirstLevelComboBox;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets postal.
     *
     * @return the postal
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Sets postal.
     *
     * @param postal the postal
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Sets customer id.
     *
     * @param customerID the customer id
     */
    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
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
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets first level.
     *
     * @return the first level
     */
    public Scheduler.FirstLevelDivision getFirstLevel() {
        return firstLevel;
    }

    /**
     * Sets first level.
     *
     * @param firstLevel the first level
     */
    public void setFirstLevel(Scheduler.FirstLevelDivision firstLevel) {
        this.firstLevel = firstLevel;
    }


    /**
     * On save clicked. Handles the save button click
     */
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


    /**
     * Clear fields.
     */
    public void clearFields(){

            customerIDTextField.clear();
            customerNameTextField.clear();
            customerAddressTextField.clear();
            customerPostalTextField.clear();
            customerPhoneTextField.clear();

          customerCountryComboBox.getSelectionModel().clearSelection();
          customerFirstLevelComboBox.getSelectionModel().clearSelection();


    }

    /**
     * On country clicked. Handles sorting first level divisions by country.
     */
    public void onCountryClicked()
    {
        getCustomerFirstLevelComboBox().setItems(controller.getMainApp().getDivisionsByCountry(getCustomerCountryComboBox().getSelectionModel().getSelectedItem()));
    }
}
