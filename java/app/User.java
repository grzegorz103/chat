package main.java.app;

import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {

    private String nick;
    private int red, green, blue;

    public User(String nick) {
        this.nick = nick;
        this.red = new Random().nextInt(200);
        this.green = new Random().nextInt(200);
        this.blue = new Random().nextInt(200);
    }

    public String getNick() {
        return nick;
    }


    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
