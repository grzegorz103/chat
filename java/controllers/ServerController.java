package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.server.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController implements Transfer {

    @FXML
    private Button startButton, stopButton;

    private ServerSocket ss;
    private boolean exit = false;
    private List<ClientThread> activePlayers = new ArrayList<>();
    private Thread th;

    @FXML
    public void initialize() {
        this.stopButton.setDisable(true);
    }

    @FXML
    public void start(ActionEvent actionEvent) {
        this.stopButton.setDisable(false);
        this.startButton.setDisable(true);
        exit = false;
        th = new Thread(() -> {
            try {
                ss = new ServerSocket(5000);
                while (!exit) {
                    Socket socket = ss.accept();
                    ClientThread clientThread = new ClientThread(socket, this);
                    this.activePlayers.add(clientThread);
                    Thread thread = new Thread(clientThread);
                    thread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        th.start();
    }

    @FXML
    public void stop(ActionEvent actionEvent) {
        this.th.interrupt();
        this.exit = true;
        this.activePlayers = new ArrayList<>();
        try {
            this.ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.startButton.setDisable(false);
        this.stopButton.setDisable(true);
    }

    @Override
    public List<ClientThread> getList() {
        return this.activePlayers;
    }
}

