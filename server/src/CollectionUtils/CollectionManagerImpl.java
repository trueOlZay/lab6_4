package CollectionUtils;

import BaseClass.City;
import BaseClass.Government;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class CollectionManagerImpl implements CollectionManager {
    private static LinkedList<City> cityLinkedList;
    private List r = new ArrayList();
    private static ZonedDateTime creationDate;
    private final SupportManager supportManager;

    @Inject
    private CollectionManagerImpl(SupportManager supportManager) {
        this.supportManager = supportManager;
    }

    public void initList() {
        if (cityLinkedList == null) {
            cityLinkedList = new LinkedList<>();
            creationDate = ZonedDateTime.now();
        }
        else {
            cityLinkedList.clear();
        }
    }

    public void add(City city) {
        cityLinkedList.add(city);
    }

    public String info() {
        String info = "";
        info += "%type " + cityLinkedList.getClass().getName() + "\n";
        info += "%date " + creationDate + "\n";
        info += "%size " + cityLinkedList.size() + "\n";

        return info;
    }

    public LinkedList<City> show() {
        return new LinkedList<>(cityLinkedList);
        //cityLinkedList.sort(Comparator.comparing(City::getName));

        //String res = cityLinkedList.stream().map(supportManager::display).collect(Collectors.joining("---------------------------------\n"));
        //if (res.equals("")) {
            //res =  "Коллекция пуста";
       // }
        //return res;
    }

    public void remove_by_id(Integer cityID) {
        cityLinkedList.forEach(city -> {
            if (city.getId().equals(cityID)) {
                cityLinkedList.remove(city);
            }
        });
    }

    public void clear() {
        cityLinkedList.clear();
    }

    public String remove_first() {
        String result;
        if (cityLinkedList.size() > 0) {
            cityLinkedList.remove(0);
            result = "Первый элемент коллекции успешно удален.";
        }
        else {
            result = "Данная коллекция пуста.";
        }
        return result;
    }

    public String add_if_min(City city) {
        String result;
        if (cityLinkedList.size() > 0) {
            City res = cityLinkedList.getFirst();
            for (City city1: cityLinkedList) {
                if (city1.compareTo(res) < 0) {
                    res = city1;
                }
            }
            if (city.compareTo(res) < 0) {
                cityLinkedList.add(city);
                result = "Элемент успешно добаавлен в коллекцию";
            }
            else {
                result = "Элемент не является минимальным. Добавления в коллекцию не произошло.";
            }
        }
        else {
            result = "Коллекция пуста";
        }
        return result;
    }

    public boolean check_min(City city) {
        Boolean res = false;
        if (cityLinkedList.size() > 0) {
            City r = cityLinkedList.getFirst();
            for (City city1: cityLinkedList) {
                if (city1.compareTo(r) < 0) {
                    r = city1;
                    res = true;
                }
            }
        }
        return res;
    }

    public boolean check_max(City city) {
        Boolean res = false;
        if (cityLinkedList.size() > 0) {
            City r = cityLinkedList.getFirst();
            for (City city1: cityLinkedList) {
                if (city1.compareTo(r) > 0) {
                    r = city1;
                    res = true;
                }
            }
        }
        return res;
    }

    public String add_if_max(City city) {
        String result;
        if (cityLinkedList.size() > 0) {
            City res = cityLinkedList.getFirst();
            for (City city1: cityLinkedList) {
                if (city1.compareTo(res) > 0) {
                    res = city1;
                }
            }
            if (city.compareTo(res) > 0) {
                cityLinkedList.add(city);
                result = "Элемент успешно добаавлен в коллекцию";
            }
            else {
                result = "Элемент не является максимальным. Добавления в коллекцию не произошло.";
            }
        }
        else {
            result = "Коллекция пуста.";
        }
        return result;
    }

    public String update(City city1, Integer ID) {
        String res;
        final String[] result = new String[1];
        cityLinkedList.forEach(city -> {
            if (city.getId() == (ID)) {
                city.setName(city1.getName());
                city.setCoordinates(city1.getCoordinates());
                city.setArea(city1.getArea());
                city.setPopulation(city1.getPopulation());
                city.setMetersAboveSeaLevel(city1.getMetersAboveSeaLevel());
                city.setAgglomeration(city1.getAgglomeration());
                city.setClimate(city1.getClimate());
                city.setGovernment(city1.getGovernment());
                city.setGovernor(city1.getGovernor());
                result[0] = "sss";
            }
        });
        if (result[0].equals("sss")) {
            res = "Элемент успешно добавлен в коллекцию";
        }
        else {
            res = "Элемента с указанным ID не существует.";
        }
        return res;
    }

    public String count_greater_than_government(Government government) {
        String result;
        if (cityLinkedList.size() > 0) {
            int kol = 0;
            for (City city: cityLinkedList) {
                if (city.getGovernment() != null && city.getGovernment().compareTo(government) > 0) {
                    kol ++;
                }
            }
            result = "Результат: " + kol;
        }
        else {
            result = "Данная коллекция пуста.";
        }
        return result;
    }

    public String filter_by_government(Government government) {
        String result = "";
        if (cityLinkedList.size() > 0) {
            int kol = 0;
            for (City city: cityLinkedList) {
                if (city.getGovernment().equals(government)) {
                    result += supportManager.display(city);
                    kol ++;
                }
            }
            if (kol == 0) {
                result = "Городов с данной формой правления в коллекции нет. ";
            }
        }
        else {
            result = "Данная коллекция пуста. ";
        }
        return result;
    }

    public String print_field_ascending_agglomeration() {
        String result = "";
        if (cityLinkedList.size() > 0) {
            for (City city: cityLinkedList) {
                result += "Город " + city.getName() + ": \n" + city.getAgglomeration() + "\n";
            }
        }
        else {
            result = "Данная коллекция пуста";
        }
        return result;
    }

    public LinkedList<City> getCityLinkedList() {
        return cityLinkedList;
    }

    public void append_to_list(Object o) {
        r.add(o);
    }
}
