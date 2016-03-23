package main_package;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;
import javax.swing.JFileChooser;


import java.io.File;
import java.util.List;

public class Scene2 {

    VBox layout2 = new VBox();

    Scene scene2 = new Scene(layout2, 540, 360);

    Button play_btn = new Button();
    Button pause_btn = new Button();
    Button load_btn = new Button("Load");


    Duration duration;

    Slider timeSlider = new Slider(0.0,100.0,0.0);

    static double current_value = 0.0;


    /*slider for curent track*/


    String path=null;

    public String get_path() {

        return path;
    }


    public String set_path(String path){
        this.path = path;
        return path;
    }


    public void setPlay_btn(Button play_btn) {
        play_btn.setText("PLAY");
    }

    public void setPause_btn(Button pause_btn) {pause_btn.setText("PAUSE");}

    public void setLoad_btn(Button load_btn) {
        load_btn.setText("LOAD MP3's");
    }

    /*main playlist area*/
    ObservableList<String> data = FXCollections.observableArrayList();
    ListView<String> listView = new ListView<String>(data);


    public void set_layout_main(VBox layout2) {

        listView.setPrefSize(200, 250);

        //data.addAll("Billy Joel - Piano Man", "[ELECTRO] Vexare - Ripened Pears", "Shakira - Hips Don't Lie", "Daft Punk - Aerodynamic", "Paperhouse","08-Sprawl");

       // data.add(String.valueOf(get_files()));



        /***********************/

        layout2.setSpacing(10);
        layout2.setPadding(new Insets(20, 10, 10, 10));
        layout2.getChildren().addAll(play_btn,pause_btn,timeSlider,listView,load_btn);

    }


    public Scene return_scene2(Stage primaryStage){

/*CSS*/
        scene2.getStylesheets().add(Main.class.getResource("Scene2UI.css").toExternalForm());

        setPause_btn(pause_btn);
        setPlay_btn(play_btn);
        set_layout_main(layout2);
        setLoad_btn(load_btn);

        // String path = "C:/Users/Geoff/Music/02-TinSoldier.mp3s";

        //player will bring up directory window upon login

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        //File file = fileChooser.showOpenDialog(primaryStage);
        List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);

        System.out.println(file);
        set_path(String.valueOf(file.get(0)));//set file path for JavaFX library media functions
        data.add(String.valueOf(file));



        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();//
        //change the timesliders length
        /*
        timeSlider.setMin(0.0);
        timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
*/

        mediaPlayer.pause(); //pause on load
        mediaPlayer.setAutoPlay(false);//toggle whether song play when file chosen


        //////////

        listView.setItems(data);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    System.out.println(new_val);

                    mediaPlayer.play();

                });


        play_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("prompt: PLAY");
                mediaPlayer.play();

                System.out.println("On ready "+mediaPlayer.getOnReady());
                System.out.println("Status:" +mediaPlayer.getStatus());
                System.out.println("Duration: "+ duration);

            }

        });

        pause_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("prompt: PAUSED");
                mediaPlayer.pause();

                System.out.println("On ready: "+mediaPlayer.getOnReady());
                System.out.println("Status:" +mediaPlayer.getStatus());
                System.out.println("Duration: "+ duration);
            }
        });

        load_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
                fileChooser.getExtensionFilters().add(extFilter);
                //File file = fileChooser.showOpenDialog(primaryStage);
                List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);
                System.out.println(file);
                set_path(String.valueOf(file));//set file path for JavaFX library media functions
                data.add(String.valueOf(file));

                //path = file.getAbsolutePath();
                path = file.get(0).getAbsolutePath();// ONLY RETURNS FIRST INDEX NEEDS EVERY SELECTED TO POPULATE LIST
                path = path.replace("\\", "/");

                mediaPlayer.pause();
                mediaPlayer.setAutoPlay(false);

                MediaView mediaView = new MediaView(mediaPlayer);

                mediaView.setMediaPlayer(mediaPlayer);
                
            }

        });


        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    mediaPlayer.pause();

                    // multiply duration by percentage calculated by slider position
                    System.out.println("TIMESLIDER MARK: "+timeSlider.getValue());
                   // mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));

                    mediaPlayer.seek(Duration.millis(timeSlider.getValue()*1000));

                    System.out.println("DURATION: "+Duration.millis(timeSlider.getValue()));

                    mediaPlayer.play();
                }
            }
        });

        return scene2;
    }



}

