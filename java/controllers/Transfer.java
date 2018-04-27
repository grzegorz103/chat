package main.java.controllers;

import main.java.server.ClientThread;

import java.util.List;

public interface Transfer {
    List<ClientThread> getList();
}