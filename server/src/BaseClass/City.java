package BaseClass;

import java.io.Serializable;
import java.time.LocalDate;

public class City implements Comparable<City>, Serializable {
    private static final long serialVersionUID = 32L;
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long area; //Значение поля должно быть больше 0
    private int population; //Значение поля должно быть больше 0
    private float metersAboveSeaLevel;
    private float agglomeration;
    private Climate climate; //Поле не может быть null
    private Government government; //Поле может быть null
    private Human governor; //Поле не может быть null

    public City(String name, Coordinates coordinates, long area, int population, float metersAboveSeaLevel, float agglomeration, Climate climate, Government government, Human governor) {
        this.id = 0;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.agglomeration = agglomeration;
        this.climate = climate;
        this.government = government;
        this.governor = governor;
    }


    public Climate getClimate() {
        return climate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Double getCoordinatesX() {
        return coordinates.getX();
    }

    public Float getCoordinatesY() {
        return coordinates.getY();
    }

    public float getAgglomeration() {
        return agglomeration;
    }

    public float getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public Government getGovernment() {
        return government;
    }

    public Human getGovernor() {
        return governor;
    }

    public int getPopulation() {
        return population;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getArea() {
        return area;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setAgglomeration(float agglomeration) {
        this.agglomeration = agglomeration;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setGovernment(Government government) {
        this.government = government;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    public LocalDate getGovernorBirthday() {
        return governor.getBirthday();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMetersAboveSeaLevel(float metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Integer toCompare(City city) {
        return (this.id - city.getId());
    }

    @Override
    public int compareTo(City o) {
        return getName().compareTo(o.getName());
    }
}
