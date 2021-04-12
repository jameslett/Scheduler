package Scheduler;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.ResourceBundle;

/**
 * The type Login controller.
 */
public class LoginController {

    @FXML
    private Button login = new Button();
    @FXML
    private TextField userName = new TextField();
    @FXML
    private PasswordField passwordField = new PasswordField();
    @FXML
    private Label zoneID = new Label();

    private static ResourceBundle resourceBundle;
    private Scheduler mainApp;
    private Stage loginStage;

    @FXML
    private void initialize(){



    }

    /**
     * Set main app.
     *
     * @param scheduler the scheduler
     */
    public void SetMainApp(Scheduler scheduler){

        mainApp = scheduler;
    }

    /**
     * On cancel clicked. Closes the login Window
     */
    public void onCancelClicked(){

        System.exit(0);
    }

    /**
     * On login clicked.
     * Checks username and password and logs to file
     */
    public void onLoginClicked(){
        Scheduler.User u = userNameMatch(userName.getText());

        String status = "Login failed";

        if(u == null){

            //insert username error

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("warning"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("incUN"));

            alert.showAndWait();

        }
        else if(!passwordMatch(passwordField.getText(),u)){
            //insert password error
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("warning"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("incPW"));

            alert.showAndWait();
        }
        else{
            mainApp.setUser(u);
            loginStage = (Stage) login.getScene().getWindow();
            loginStage.close();
            System.out.println("UN and PW CORRECT");
            status = "Login Succeeded";
        }
        try(FileWriter fw = new FileWriter(" login_activity.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {

            out.println("\nLogin Attempt:\n");
            out.println("Username:" +userName.getText());
            ZonedDateTime date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
            out.println("Date and Time: " + date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
            out.println(status);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * User name match user.
     *
     * @param username the username
     * @return the user
     */
    Scheduler.User userNameMatch(String username){


        Scheduler.User temp = null;
        for(Scheduler.User u : mainApp.getUsers()){
            if(u.getUsername().equals(username)){
                temp = u;
                break;
            }
        }
        return temp;
    }


    /**
     * Password match boolean.
     *
     * @param password the password
     * @param u        the u
     * @return the boolean
     */
    boolean passwordMatch(String password, Scheduler.User u){
        System.out.println("PASSWORD  FIRING");
        if(password.equals(u.getPassword())){
            return true;
        }
        else {
            return false;
        }


    }


    /**
     * Get login stage stage.
     *
     * @return the stage
     */
    public Stage getLoginStage(){
        return loginStage;
    }

    /**
     * Gets resource bundle.
     *
     * @return the resource bundle
     */
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Sets resource bundle.
     *
     * @param resourceBundle the resource bundle
     */
    public static void setResourceBundle(ResourceBundle resourceBundle) {
        LoginController.resourceBundle = resourceBundle;
    }

    /**
     * Gets zone id.
     *
     * @return the zone id
     */
    public Label getZoneID() {
        return zoneID;
    }

    /**
     * Sets zone id.
     *
     * @param zoneID the zone id
     */
    public void setZoneID(Label zoneID) {
        this.zoneID = zoneID;
    }
}
