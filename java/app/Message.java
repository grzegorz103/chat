package main.java.app;

import java.io.Serializable;

public class Message implements Serializable {

    private String nick, text;
    private int red, green, blue;

    public Message(String nick, String text, int red, int green, int blue) {
        this.nick = nick;
        this.text = text;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getNick() {
        return nick;
    }

    public String getText() {
        return text;
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
