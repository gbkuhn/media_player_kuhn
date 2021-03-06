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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;


import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.geometry.Orientation.VERTICAL;

public class Scene2 {

    MediaPlayer mediaPlayer;

    VBox layout2 = new VBox();

    Scene scene2 = new Scene(layout2, 460, 440);

    Button play_btn = new Button();
    Button pause_btn = new Button();
    Slider volume = new Slider(0.0, 100.0,80.0);
    Button load_btn = new Button("Load");
    static Text track_title = new Text("Track title");//blank upon initial load
    static Text track_time = new Text("0/0s");
    static Text volume_text = new Text("Volume");

    static boolean timer_running = false;

    static double current_time = 0.0;

    Timer timer = new Timer();

    Duration duration;

    Slider timeSlider = new Slider(0.0, 100.0, 0.0);

    static double current_value = 0.0;

    /*slider for curent track*/


    String path = null;

    public String get_path() {

        return path;
    }


    public String set_path(String path) {
        this.path = path;
        return path;
    }

    public void setPlay_btn(Button play_btn) {
        play_btn.setText("PLAY");
    }

    public void setPause_btn(Button pause_btn) {
        pause_btn.setText("PAUSE");
    }

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
        layout2.setPadding(new Insets(20, 20, 20, 20));
        layout2.getChildren().addAll(play_btn, pause_btn, track_time, track_title, timeSlider, listView,volume_text, volume, load_btn);
        volume.setOrientation(VERTICAL);

    }

    public void set_file(String _path) {
        Media media = new Media(new File(_path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        // mediaPlayer.play();//
    }

    public MediaPlayer get_mediaPlayer_obj() {

        return mediaPlayer;
    }


     String trim_directory(String _directory) {
        _directory = path.substring(path.lastIndexOf("\\") + 1, path.length());

        return _directory;
    }

    public void set_current_time(double _current_time) {
        this.current_time = _current_time;
    }


    // creating timer task, timer
    TimerTask track_timer = new TimerTask() {
        @Override
        public void run() {
            System.out.println("timer working " + timeSlider.getValue());
            timeSlider.setBlockIncrement(1.0);
            timeSlider.increment();
            track_time.setText(String.valueOf(Math.round(timeSlider.getValue()) + "/" + String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds()))) + "s");
        }
    };



    public Scene return_scene2(Stage primaryStage) {


        /*CSS*/
        scene2.getStylesheets().add(Main.class.getResource("Scene2UI.css").toExternalForm());
        track_title.setId("track-text");//CSS for track title

        track_time.setId("track-time");
        load_btn.setId("load-btn");
        volume_text.setId("volume-txt");

        setPause_btn(pause_btn);
        setPlay_btn(play_btn);
        set_layout_main(layout2);
        setLoad_btn(load_btn);

        //player will bring up directory window upon login

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);


        System.out.println(file); //prints the list
        set_path(String.valueOf(file.get(0)));//set file path for JavaFX library media functions


        //populates list after initial load

        for (int i = 0; i < file.size(); i++) {
            data.add(String.valueOf(file.get(i)));
        }

        set_file(path);

        get_mediaPlayer_obj().pause(); //pause on load
        get_mediaPlayer_obj().setAutoPlay(false);//toggle whether song play when file chosen


        listView.setItems(data);//populates playlist
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    System.out.println(new_val);


                    String path = new_val;
                    File f = new File(path);
                    System.out.println(f.getName());

                    track_title.setText(f.getName());


                    System.out.println("TRACK TITLE DISPLAYED "+track_title.getText());

                    get_mediaPlayer_obj().stop();

                    set_file(new_val);
                    //media = new Media(new File(new_val).toURI().toString());
                    //mediaPlayer = new MediaPlayer(media);
                    //set on ready is needed to return the duration of the song

                    mediaPlayer.setOnReady(new Runnable() {

                        @Override
                        public void run() {

                            timeSlider.setMax(get_mediaPlayer_obj().getTotalDuration().toSeconds());

                            get_mediaPlayer_obj().play();//plays song once selected

                            //track_title.setText(trim_directory(new_val));//trim directory method will get rid of prefix filepath
                            System.out.println("SONG: "+trim_directory(new_val));
                        }
                    });

                    if(timer_running==false) {
                        timer = new Timer();
                        timer.schedule(track_timer, 1000, 1000);
                        timer_running = true;
                    }

                    /*FOR THE mediaPlayer object ot return certain values needs STATUS.READY
                    the following code will handle it*/

                            timeSlider.setValue(0.0);

                            System.out.println("Duration: " + mediaPlayer.getTotalDuration().toSeconds());

                            //timeSlider.setMax(get_mediaPlayer_obj().getTotalDuration().toSeconds());

                            System.out.println("slider max "+get_mediaPlayer_obj().getTotalDuration().toSeconds());
/*
                            current_time = timeSlider.getValue();

                            set_current_time(timeSlider.getValue());
*/
                            track_time.setText(String.valueOf(Math.round(timeSlider.getValue()) + "/" + String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds()))) + "s");

                });


        play_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(timer_running==false){ //if the track is already paying it will not do anything, if its not the timer_running boolean will be false

                System.out.println("prompt: PLAY");

                get_mediaPlayer_obj().play();

                System.out.println("On ready " + mediaPlayer.getOnReady());
                System.out.println("Status:" + mediaPlayer.getStatus());
                System.out.println("Duration: " + duration);

                //track_time.setText(String.valueOf(Math.round(timeSlider.getValue())+"/"+String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds())))+"s");

                TimerTask track_timer = new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("timer working " + timeSlider.getValue());
                        timeSlider.increment();
                        track_time.setText(String.valueOf(Math.round(timeSlider.getValue()) + "/" + String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds()))) + "s");
                    }
                };

                    timer = new Timer();
                timer.schedule(track_timer, 1000, 1000);


                //flag so if a new song is selected the timer wont run again
            }

        }});

        pause_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("prompt: PAUSED");
                get_mediaPlayer_obj().pause();

                System.out.println("On ready: " + mediaPlayer.getOnReady());
                System.out.println("Status: " + mediaPlayer.getStatus());
                System.out.println("Duration: " + duration);

                //track_time.setText(String.valueOf(Math.round(timeSlider.getValue())+"/"+String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds())))+"s");

                timer.cancel();
                timer.purge();

                timer_running = false;

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

                System.out.println(file); //prints the list
                set_path(String.valueOf(file.get(0)));//set file path for JavaFX library media functions
                // data.add(String.valueOf(file));

                //populates list after initial load

                for (int i = 0; i < file.size(); i++) {
                    data.add(String.valueOf(file.get(i)));
                }

                set_file(path);

                get_mediaPlayer_obj().pause();

            }

        });


        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    get_mediaPlayer_obj().pause();

                    // multiply duration by percentage calculated by slider position
                    System.out.println("TIMESLIDER MARK: " + timeSlider.getValue());
                    // mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));

                    get_mediaPlayer_obj().seek(Duration.millis(timeSlider.getValue() * 1000));

                    System.out.println("DURATION: " + Duration.millis(timeSlider.getValue()));

                    get_mediaPlayer_obj().play();

                    track_time.setText(String.valueOf(Math.round(timeSlider.getValue()) + "/" + String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds()))) + "s");

                }
            }
        });

        //this allows the point in the song to change if the sliding bar is clicked as opposed to dragging the bubble
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isPressed()) {
                    get_mediaPlayer_obj().pause();

                    // multiply duration by percentage calculated by slider position
                    System.out.println("TIMESLIDER MARK: " + timeSlider.getValue());
                    // mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));

                    get_mediaPlayer_obj().seek(Duration.millis(timeSlider.getValue() * 1000));

                    System.out.println("DURATION: " + Duration.millis(timeSlider.getValue()));

                    get_mediaPlayer_obj().play();

                    track_time.setText(String.valueOf(Math.round(timeSlider.getValue()) + "/" + String.valueOf(Math.round(get_mediaPlayer_obj().getTotalDuration().toSeconds()))) + "s");


                }
            }
        });

        volume.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volume.isValueChanging()) {

                    mediaPlayer.setVolume(volume.getValue());

                    System.out.println("volume "+volume.getValue());


                }
            }
        });

        //this allows the point in the song to change if the sliding bar is clicked as opposed to dragging the bubble
        volume.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volume.isPressed()) {

                    System.out.println("volume "+volume.getValue());

                    mediaPlayer.setVolume(volume.getValue());


                }
            }
        });

        return scene2;
    }

}

