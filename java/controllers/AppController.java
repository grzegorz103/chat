package main.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.app.Message;
import main.java.app.User;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class AppController implements Externalizable {

    @FXML
    public TextField textBox;

    @FXML
    public VBox vbox;

    @FXML
    public Button sendButton;

    private String nick;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;

    private final Alert.AlertType TYPE1 = Alert.AlertType.INFORMATION;
    private final String HOSTNAME = "localhost";
    private final int PORT = 5000;

    public AppController() {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Chattie");
        input.setHeaderText("Please enter your nick");

        try {
            this.socket = new Socket(HOSTNAME, PORT);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            setAlert(TYPE1, "Connection error");
            System.exit(1);
        }

        Optional<String> result;
        do {
            result = input.showAndWait();
        } while (result.isPresent() && result.get().length() == 0);
        this.nick = "user";
        result.ifPresent(s -> nick = s);

        this.user = new User(this.nick);
    }

    @FXML
    public void initialize() {
        this.vbox.getChildren().add(new Label("Welcome in Chat"));
        try {
            readExternal(ois);
        } catch (IOException | ClassNotFoundException e) {
            setAlert(TYPE1, "Connection error");
            System.exit(1);
        }

        this.textBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                sendMessage();
        });
    }

    @FXML
    private void sendMessage() {
        if (this.textBox.getText().length() == 0) {
            setAlert(TYPE1, "You can't send an empty message");
            return;
        }
        try {
            writeExternal(this.oos);
        } catch (IOException e) {
            setAlert(TYPE1, "Connection error");
            System.exit(1);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        Message msg = new Message(this.user.getNick(), textBox.getText(), this.user.getRed(), this.user.getGreen(), this.user.getBlue());
        out.writeObject(msg);
        oos.flush();
        this.textBox.setText("");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        Thread thread = new Thread(() -> {
            while (true) {
                updateText(in);
            }
        });
        thread.start();
    }

    private void updateText(ObjectInput in) {

        Message msg = null;
        try {
            msg = (Message) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            setAlert(TYPE1, "Connection error");
            System.exit(1);
        }
        FlowPane fp = new FlowPane();
        Text nickText = new Text(msg.getNick() + ": ");
        nickText.setStyle("-fx-font-weight: bold");
        nickText.setFill(Color.rgb(msg.getRed(), msg.getGreen(), msg.getBlue()));
        Label message = new Label(msg.getText());
        fp.getChildren().addAll(nickText, message);
        Platform.runLater(() ->
                this.vbox.getChildren().add(fp));
    }

    private void setAlert(Alert.AlertType type, String text) {
        Alert alert = new Alert(type);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
