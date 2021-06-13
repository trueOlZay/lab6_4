package CommandModule.SerializedCommand;

import BaseClass.City;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SerializedCollection implements Serializable {
    private static final long serialVersionUID = 32L;
    private LinkedList<City> linkedList;
    private List<List<Integer>> idOfUserEl;
    private String type;

    public SerializedCollection(LinkedList<City> linkedList, List<List<Integer>> idOfUserEl, String type) {
        this.linkedList = linkedList;
        this.idOfUserEl = idOfUserEl;
        this.type =type;
    }

    public List<List<Integer>> getIdElementsAllUsers() {
        return idOfUserEl;
    }
    public LinkedList<City> getLinkedList() {
        return linkedList;
    }
    public String getType() {
        return type;
    }
}
