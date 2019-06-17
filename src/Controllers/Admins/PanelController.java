package Controllers.Admins;

import Controllers.Super;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Controllers.settings.appName;
import static Controllers.settings.siteHelp;


public class PanelController extends Super implements Initializable {

    public WebView webview;
    public Button adduser;
    public TextArea userdescription;
    public TextField useridentifier;
    public TextField useremail;
    public TextField username;
    public AnchorPane panel;
    public ChoiceBox role;
    public TextField location;
    public Button logout;
    public Button addcertfile;
    public Tab regstaff;
    public Tab patientinfo;
    public Tab staffinfo;
    public Tab news;
    public TextField searchPatientID;
    public Button searchpatientbutton;
    public Label clock;
    public TabPane tabpane;
    public Label title;
    double tabWidth = 200.0;
    private File file;
    private FileInputStream fileInputStream;
    private int length;

    //Administrator panel controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        title.setText(appName + " Admin Panel ");
    }

    private void init() {
//        if(changepassword.containsKey("change")){
//            if(changepassword.get("change")){
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setHeaderText(null);
//                alert.setTitle("Change your password");
//                alert.setContentText("Please change your default password for your own account privacy");
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.get() == ButtonType.OK){
//                    PasswordDialog pd = new PasswordDialog();
//                    Optional<String> res = pd.showAndWait();
//                    res.ifPresent(password ->{
//                        out.println(password);} );
//
//
//                }
//            }
//            changepassword.remove("change");
//        }
        time(clock);
        buttonListeners();
        enterPressed();
        WebEngine engine = webview.getEngine();//help web page
        engine.load(siteHelp);
        configureView();
    }

    private void configureView() {
        tabpane.setTabMinWidth(tabWidth);
        tabpane.setTabMaxWidth(tabWidth);
        tabpane.setTabMinHeight(tabWidth - 140.0);
        tabpane.setTabMaxHeight(tabWidth - 140.0);
        tabpane.setRotateGraphic(true);


//        configureTab(tabexisting, "EXISTING PATIENTS", "resources/images/22-Cardi-B-Money.png");
//        configureTab(tabnew, "NEW PATIENTS", "resources/images/22-Cardi-B-Money.png");
    }

    private void configureTab(Tab tab, String title, String iconPath) {
        double imageWidth = 40.0;

        ImageView imageView = new ImageView(new Image(iconPath));
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
//        tabPane.setRotate(90.0);

        tabPane.setMaxWidth(tabWidth);
        tabPane.setCenter(imageView);
        tabPane.setBottom(label);

        tab.setText("");
        tab.setGraphic(tabPane);
    }

    private void enterPressed() {

        useridentifier.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        useremail.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        userdescription.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        username.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });

    }

    private void buttonListeners() {
        addcertfile.setOnMouseClicked(event -> {

            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extensionFilterPdf = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().addAll(extensionFilterPdf);
            fileChooser.setTitle("SELECT PDF FILE OF CERTIFICATION");
            //Show open file dialog
            file = fileChooser.showOpenDialog(null);
            length = (int) file.length();

            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        adduser.setOnMouseClicked(event -> {
//            add new user
            addNewUser();
        });
        logout.setOnMouseClicked(event -> {
            logOut(panel);

        });
    }

    private void addNewUser() {
        try {
            validation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void validation() throws SQLException {
//check if all fields are filled
        if (username.getText().isEmpty() || useremail.getText().isEmpty() || userdescription.getText().isEmpty() || useridentifier.getText().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {
            String roleval = role.getValue().toString();
            String locationString = location.getText();
            String name = username.getText();
            String description = userdescription.getText();
            String identification = useridentifier.getText();
            String email = useremail.getText();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "User exists", "The user email is already used");

            } else {
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    //adding user into database
                    PreparedStatement insertStaff = connection.prepareStatement("INSERT INTO users(name, email, password, hospital, status, userclearancelevel,certification,identification,description) VALUES (?,?,?,?,?,?,?,?,?)");
                    insertStaff.setString(1, name);
                    insertStaff.setString(2, email);
                    insertStaff.setString(3, email);
                    insertStaff.setString(4, locationString.toUpperCase());
                    insertStaff.setString(5, "active");
                    insertStaff.setString(6, roleval);
                    try {
                        insertStaff.setBinaryStream(7, FileUtils.openInputStream(file), length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    insertStaff.setString(8, identification);
                    insertStaff.setString(9, description);
                    insertStaff.executeUpdate();
                } else {
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                            "INVALID EMAIL", "PLEASE ENTER A VALID EMAIL");
                }
            }


        }
    }

}
