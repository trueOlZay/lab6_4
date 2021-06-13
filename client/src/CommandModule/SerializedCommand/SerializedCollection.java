package CommandModule.SerializedCommand;

import BaseClass.City;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SerializedCollection implements Serializable {
    private static final long serialVersionUID = 32L;
    private LinkedList<City> linkedList;
    private List<List<Integer>> idElements;
    private String type;

    public SerializedCollection(LinkedList<City> linkedList, List<List<Integer>> idElements, String type) {
        this.linkedList = linkedList;
        this.idElements = idElements;
        this.type = type;
    }

    public LinkedList<City> getLinkedList() {
        return linkedList;
    }

    public List<List<Integer>> getIdElements() {
        return idElements;
    }

    public String getType() {
        return type;
    }
}
