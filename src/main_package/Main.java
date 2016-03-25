package main_package;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Main extends Application {

    public static Text set_title(){
        Text scenetitle = new Text("Login");
        scenetitle.setId("login-text");//CSS

       // scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        return scenetitle;
    }

    @Override
    public void start(Stage primaryStage) {

        Button settings_btn = new Button();
        Button login_btn = new Button();

        settings_btn.setText("Settings action");
        login_btn.setText("Login action");

        TextField userTextField = new TextField();
        PasswordField pwBox = new PasswordField();

        final Text actiontarget = new Text();

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(20, 20, 10, 20));
        layout.getChildren().addAll(set_title(),userTextField,pwBox,login_btn,settings_btn,actiontarget);


        //VBox layout2 = new VBox();

        /*
        StackPane root = new StackPane();
        root.getChildren().add(settings_btn);
        root.getChildren().add(remove_btn);
        root.setAlignment(settings_btn,0);
*/

        Scene scene = new Scene(layout, 300, 250);


        Scene2 scene2 = new Scene2();

        primaryStage.setTitle("MP3 Player - Kuhn");
        primaryStage.setScene(scene);

        //CSS INITIALIZE
        scene.getStylesheets().add(Main.class.getResource("Login.css").toExternalForm());


        primaryStage.show();


        settings_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Settings");
            }
        });


        login_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // remove_btn.setText("Increment " +count);
                System.out.println("Login");
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in button pressed");

                // actiontarget.setId("actiontarget");//CSS

                //NEED CREDENTIAL HANDLING

                primaryStage.setScene(scene2.return_scene2(primaryStage));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}