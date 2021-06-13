package CommandModule.SerializedCommand;

import BaseClass.City;

import java.io.Serializable;
import java.util.LinkedList;

public class SerializedMessage implements Serializable {
    private static final long serialVersionUID = 32L;
    private String message;
    private LinkedList<City> linkedList;

    public SerializedMessage(LinkedList<City> linkedList) {
        this.message = null;
        this.linkedList = linkedList;
    }

    public SerializedMessage(String message) {
        this.message = message;
        this.linkedList = null;
    }

    public String getMessage() {
        return message;
    }

    public LinkedList<City> getLinkedList() {
        return linkedList;
    }
}
