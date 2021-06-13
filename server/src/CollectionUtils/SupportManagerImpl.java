package CollectionUtils;

import BaseClass.City;
import com.google.inject.Inject;
import com.google.inject.Singleton;

public class SupportManagerImpl implements SupportManager{
    private final CollectionManager collectionManager;

    @Inject
    public SupportManagerImpl(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public boolean checkExist(Integer ID) {
        for (City city: collectionManager.getCityLinkedList()) {
            if (city.getId().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public String display(City city) {
        String res;
        res = String.format("ID элемента коллекции: %s\n" + "Имя: %s\n" + "Координата X: %s\n" + "Координата Y: %s\n" + "Дата создания: %s\n" + "Площадь: %s\n" + "Численность населения: %s\n" + "Метры над уровнем моря: %s\n" + "Площадь аггломерации: %s\n" + "Климат: %s\n" + "Форма правления: %s\n" + "Дата рождения: %s\n", city.getId(), city.getName(), city.getCoordinatesX(), city.getCoordinatesY(), city.getCreationDate(), city.getArea(), city.getPopulation(), city.getMetersAboveSeaLevel(), city.getAgglomeration(), city.getClimate(), city.getGovernment(), city.getGovernorBirthday());
        return res;
    }
}
