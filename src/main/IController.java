package main;

import dominio.Message;

public interface IController {

    void handleMessage(Message message);
}
