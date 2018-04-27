package main.java.server;

import main.java.app.Message;
import main.java.controllers.Transfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Transfer users;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientThread(Socket socket, Transfer users) throws IOException {
        this.socket = socket;
        this.users = users;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    private void sendAll(Message msg) {
        for (ClientThread x : this.users.getList()) {
            x.send(msg);
        }
    }

    private void send(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                Message msg = (Message) ois.readObject();
                sendAll(msg);
            } catch (IOException | ClassNotFoundException e) {
                exit = true;
            }
        }
    }
}
