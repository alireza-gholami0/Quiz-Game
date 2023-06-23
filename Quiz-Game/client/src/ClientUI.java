import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.*;
import javafx.util.Duration;

import javax.swing.event.ChangeListener;

public class ClientUI extends Application{
    Client client;
    Label lbQuestion;
    ToggleGroup options;
    RadioButton button1;
    RadioButton button2;
    RadioButton button3;
    RadioButton button4;
    Timeline timeline;
    ProgressIndicator timer;
    TextArea messages;
    TextField singleChtTo;
    TextField singleChtMes;
    TextField groupChtMes;
    TextArea scoreboard;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        String name = list.get(0);
        int cPort = Integer.parseInt(list.get(1));
        int sPort = Integer.parseInt(list.get(2));
        client = new Client(name,cPort,sPort,this);
        client.run();
        VBox  vbox = new VBox();
        Scene scene = new Scene(vbox, 500, 1000);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setAlignment(Pos.TOP_RIGHT);
        Label lblQTitle = new Label("سوال: ");
        lbQuestion = new Label("هنوز مسابقه شروع نشده");
        options = new ToggleGroup();
        button1 = new RadioButton();
        button2 = new RadioButton();
        button3 = new RadioButton();
        button4 = new RadioButton();
        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);
        button1.setToggleGroup(options);
        button2.setToggleGroup(options);
        button3.setToggleGroup(options);
        button4.setToggleGroup(options);
        timer = new ProgressBar();
        timer.setProgress(0.0f);

        Label lbMessages = new Label("All Messages");
        messages = new TextArea();

        Label lbSingleCht = new Label("Single Chat");
        Label lbSingleChtTo = new Label("To: ");
        singleChtTo = new TextField();
        HBox hbSingleChtTo = new HBox();
        hbSingleChtTo.getChildren().addAll(lbSingleChtTo, singleChtTo);

        Label lbSingleChtMes = new Label("Text: ");
        singleChtMes = new TextField();
        HBox hbSingleChtMes = new HBox();
        hbSingleChtMes.getChildren().addAll(lbSingleChtMes, singleChtMes);
        Button btnSingleCht = new Button("Send");

        Label lbGroupCht = new Label("Group Chat");
        Label lbGroupChtMes = new Label("Text: ");
        groupChtMes = new TextField();
        HBox hbGroupChtMes = new HBox();
        hbGroupChtMes.getChildren().addAll(lbGroupChtMes, groupChtMes);
        Button btnGroupCht = new Button("Send");

        Label lbBoard = new Label("Scoreboard");
        scoreboard = new TextArea();

        Button btnExit = new Button("Exit");

        vbox.getChildren().addAll(lblQTitle, lbQuestion, button1, button2, button3, button4, timer, new Separator(),
                lbMessages, messages, new Separator(),
                lbSingleCht, lbSingleChtTo , singleChtTo, lbSingleChtMes, singleChtMes, btnSingleCht, new Separator(),
                lbGroupCht, lbGroupChtMes, groupChtMes, btnGroupCht, new Separator(),
                lbBoard, scoreboard, btnExit);

        btnSingleCht.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                client.sendSingleCht(singleChtTo.getText(), singleChtMes.getText());
                singleChtMes.setText("");
                singleChtTo.setText("");
            }
        });
        singleChtMes.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    client.sendSingleCht(singleChtTo.getText(), singleChtMes.getText());
                    singleChtMes.setText("");
                    singleChtTo.setText("");
                }
            }
        });
        btnGroupCht.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                client.sendGroupCht(groupChtMes.getText());
                groupChtMes.setText("");
            }
        });
        groupChtMes.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    client.sendGroupCht(groupChtMes.getText());
                    groupChtMes.setText("");
                }
            }
        });
        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.exit();
                primaryStage.close();
                return;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("project");
        primaryStage.show();
    }
public void showMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messages.appendText(message + "\n");
            }
        });
}
    public void showQuestion(String question){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbQuestion.setText(question);
            }
        });

    }
    public void showOptions(String[] op){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!button1.isVisible()){
                    button1.setVisible(true);
                    button2.setVisible(true);
                    button3.setVisible(true);
                    button4.setVisible(true);
                }
                button1.setText(op[0]);
                button2.setText(op[1]);
                button3.setText(op[2]);
                button4.setText(op[3]);
                button1.setToggleGroup(options);
                button2.setToggleGroup(options);
                button3.setToggleGroup(options);
                button4.setToggleGroup(options);
            }
        });
    }
    public void setTimer(int time){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timer.setProgress((float) (time+1)/client.qTime);
                if (timer.getProgress() == 1.0){
                    client.writer.println("AN");
                    client.writer.println(getAnswer());
                }
            }
        });
    }
    private int getAnswer(){
        Toggle selectedToggle = options.getSelectedToggle();
        if (button1.equals(selectedToggle)) {
            return 1;
        } else if (button2.equals(selectedToggle)) {
            return 2;
        } else if (button3.equals(selectedToggle)) {
            return 3;
        } else if (button4.equals(selectedToggle)) {
            return 4;
        }
        return 0;
    }
    public void showBoard(String board) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                scoreboard.appendText(board + "\n");
            }
        });
    }
}
