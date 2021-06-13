package CollectionUtils;

import BaseClass.City;
import BaseClass.Government;

import java.util.LinkedList;

public interface CollectionManager {
    LinkedList<City> getCityLinkedList();
    void initList();
    void add(City city);
    String info();
    LinkedList<City> show();
    void remove_by_id(Integer cityID);
    void clear();
    String add_if_min(City city);
    String add_if_max(City city);
    String update(City city1, Integer ID);
    String count_greater_than_government(Government government);
    String filter_by_government(Government government);
    String print_field_ascending_agglomeration();
    void append_to_list(Object o);
    boolean check_min(City city);
    boolean check_max(City city);
    String remove_first();
}
