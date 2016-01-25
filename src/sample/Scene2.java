package sample;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;

import java.io.File;

public class Scene2 {

    VBox layout2 = new VBox();

    Scene scene2 = new Scene(layout2, 1024, 720);

    Button play_btn = new Button();
    Button pause_btn = new Button();
    Button load_btn = new Button("Load");


    public void setPlay_btn(Button play_btn) {
        play_btn.setText("PLAY action");
    }

    public void setPause_btn(Button pause_btn) {
        pause_btn.setText("PAUSE action");
    }

    public void setLoad_btn(Button load_btn) {
        load_btn.setText("LOAD FILES action");
    }


    /*main playlist area*/
    ObservableList<String> data = FXCollections.observableArrayList();
    ListView<String> listView = new ListView<String>(data);


    public void set_layout_main(VBox layout2) {

        listView.setPrefSize(200, 250);

        data.addAll("Track1", "Track2", "Track3", "Track4", "Track4","Track5");

        listView.setItems(data);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    System.out.println(new_val);

                });

        /***********************/

        layout2.setSpacing(10);
        layout2.setPadding(new Insets(20, 20, 10, 20));
        layout2.getChildren().addAll(play_btn,pause_btn,listView);

        /*file chooser*/


    }

    public Scene return_scene2(Stage primaryStage){
/*CSS*/
        scene2.getStylesheets().add(Main.class.getResource("Scene2UI.css").toExternalForm());

        setPause_btn(pause_btn);
        setPlay_btn(play_btn);
        set_layout_main(layout2);


        play_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("PLAY");
            }
        });

        load_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                System.out.println(file);
            }

        });
        return scene2;
    }

}

