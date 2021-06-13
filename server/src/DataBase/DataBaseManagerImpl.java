package DataBase;

import BaseClass.*;
import CollectionUtils.CollectionManager;

import Utils.PasswordEncrypter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DataBaseManagerImpl implements DataBaseManager {
    private final String url = "jdbc:postgresql://localhost/ITMO";
    private final String user = "postgres";
    private final String password = "Pjkjnbyrf2002";
    private final PasswordEncrypter passwordEncrypter;

    @Inject
    public DataBaseManagerImpl(PasswordEncrypter passwordEncrypter) throws SQLException, ClassNotFoundException {
        this.passwordEncrypter = passwordEncrypter;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        connectToTables();
    }

    public synchronized void connectToTables() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized <T> T processQuery(SqlFunction<Connection, T> query) throws DataBaseException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            return query.apply(connection);
        } catch (SQLException e) {
            throw new DataBaseException("Что-то пошло не так при работе с базой данных.");
        }
    }

    public synchronized void processQuery(SqlConsumer<Connection> query) throws DataBaseException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            query.accept(connection);
        } catch (SQLException e) {
            throw new DataBaseException("Что-то пошло не так при работе с базой данных.");
        }
    }

    public synchronized Integer addCity(City city, String userName) throws DataBaseException{
        return this.<Integer>processQuery((Connection connection) -> {
            Human governor = city.getGovernor();
            String addGovernor = "INSERT INTO governor (governor_birthday)" + "VALUES (?)";
            PreparedStatement governorStatement = connection.prepareStatement(addGovernor, Statement.RETURN_GENERATED_KEYS);

            governorStatement.setString(1, governor.getBirthday().toString());
            governorStatement.executeUpdate();
            ResultSet resultSet = governorStatement.getGeneratedKeys();

            if (resultSet.next()) {
                String addCity = "INSERT INTO city (city_name, x, y, creation_date, area, " +
                        "population, meters_above_sea_level, agglomeration, climate, government, governor_id, user_id)" +
                        " SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?::climate, ?::government, ?, id" +
                        " FROM \"user\"" +
                        " WHERE \"user\".login = ?";

                PreparedStatement cityStatement = connection.prepareStatement(addCity, Statement.RETURN_GENERATED_KEYS);
                Coordinates coordinates = city.getCoordinates();
                cityStatement.setString(1, city.getName());
                cityStatement.setDouble(2, coordinates.getX());
                cityStatement.setFloat(3, coordinates.getY());
                cityStatement.setString(4, city.getCreationDate().toString());
                cityStatement.setFloat(5, city.getArea());
                cityStatement.setInt(6, city.getPopulation());
                cityStatement.setFloat(7, city.getMetersAboveSeaLevel());
                cityStatement.setFloat(8, city.getAgglomeration());
                cityStatement.setString(9, city.getClimate().toString());
                cityStatement.setString(10, city.getGovernment().toString());
                cityStatement.setInt(11, resultSet.getInt(1));
                cityStatement.setString(12, userName);

                cityStatement.executeUpdate();
                ResultSet result = cityStatement.getGeneratedKeys();
                result.next();
                return result.getInt(1);
            }
            else {
                throw new SQLException("Что-то пошло не так с базой данных.");
            }
        });
    }


    public synchronized boolean remove_by_id(int id, String userName) throws DataBaseException {
        return processQuery((Connection connection) -> {
            String query = "DELETE from governor " +
                    "USING \"user\", city " +
                    "WHERE governor.governor_id = city.governor_id AND city.city_id = ?" +
                    "AND city.user_id = \"user\".id AND \"user\".login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, userName);

            int deleted = preparedStatement.executeUpdate();
            return deleted > 0;
        });
    }

    public synchronized boolean update_by_id(City city, int id, String userName) throws DataBaseException {
        return processQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION");
            String query = "UPDATE city " +
                    "SET city_name = ?," +
                    "x = ?," +
                    "y = ?," +
                    "creation_date =?," +
                    "area = ?," +
                    "population = ?," +
                    "meters_above_sea_level = ?," +
                    "agglomeration = ?," +
                    "climate = ?::climate," +
                    "government = ?::government " +
                    "FROM \"user\"" +
                    "WHERE city.city_id = ? AND city.user_id = \"user\".id AND \"user\".login = ?;" +
                    "UPDATE governor " +
                    "SET governor_birthday = ?" +
                    "FROM city, \"user\"" +
                    "WHERE city.governor_id = governor.governor_id and city.city_id = ? AND city.user_id = \"user\".id AND \"user\".login = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setDouble(2, city.getCoordinates().getX());
            preparedStatement.setFloat(3, city.getCoordinates().getY());
            preparedStatement.setString(4, city.getCreationDate().toString());
            preparedStatement.setFloat(5, city.getArea());
            preparedStatement.setInt(6, city.getPopulation());
            preparedStatement.setFloat(7, city.getMetersAboveSeaLevel());
            preparedStatement.setFloat(8, city.getAgglomeration());
            preparedStatement.setString(9,city.getClimate().toString());
            preparedStatement.setString(10, city.getGovernment().toString());
            preparedStatement.setInt(11, id);
            preparedStatement.setString(12, userName);
            preparedStatement.setString(13,city.getGovernor().getBirthday().toString());

            preparedStatement.setInt(14, id);
            preparedStatement.setString(15, userName);

            int result = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT");

            return result > 0;
        });
    }

    public synchronized void cityFromDB(CollectionManager collectionManagerImpl) throws DataBaseException {
        processQuery((Connection connection) -> {
            collectionManagerImpl.initList();
            String query = "SELECT * FROM city " +
                    "INNER JOIN governor ON city.governor_id = governor.governor_id";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            City city;

            while(result.next()) {
                city = new City(
                        result.getString("city_name"),
                        new Coordinates(
                                result.getDouble("x"),
                                result.getFloat("y")
                        ),

                        result.getLong("area"),
                        result.getInt("population"),
                        result.getFloat("meters_above_sea_level"),
                        result.getFloat("agglomeration"),
                        Climate.valueOf(result.getString("climate")),
                        Government.valueOf(result.getString("government")),
                        new Human(
                                LocalDate.parse(result.getString("governor_birthday"))
                        )
                );

                city.setId(result.getInt("city_id"));
                collectionManagerImpl.add(city);
            }
        });
    }

    public synchronized String getPassword(String user) throws DataBaseException {
        return this.processQuery((Connection connection) -> {
            String query = "SELECT (\"password\")" +
                    " FROM \"user\"" +
                    " WHERE \"user\".login = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return result.getString("password");
            }
            return null;
        });
    }

    public synchronized boolean CheckUserExist(String userName) throws DataBaseException {
        return this.<Boolean>processQuery((Connection connection) -> {
            String query = "SELECT COUNT(*)" +
                    " FROM \"user\"" +
                    " WHERE \"user\".login = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            Boolean res = resultSet.getInt("COUNT") > 0;
            return res;
        });
    }

    public synchronized void addUser(String userName, String password) throws DataBaseException {
        processQuery((Connection connection) -> {
            String query = "INSERT INTO \"user\" (\"login\", \"password\")" +
                    "VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();
        });
    }

    public synchronized ArrayList<Integer> clearUserDB(String userName) throws DataBaseException {
        return processQuery((Connection connection) -> {
            String query = "DELETE FROM governor " +
                    "USING city, \"user\"" +
                    "WHERE governor.governor_id = city.city_id AND city.user_id = \"user\".id AND \"user\".login = ?" +
                    "RETURNING city.city_id;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> idList = new ArrayList<>();

            while (resultSet.next()) {
                idList.add(resultSet.getInt("city_id"));
            }

            return idList;
        });
    }

    public synchronized List<Integer> getUserElementsId(String userName) throws DataBaseException {
        return processQuery((Connection connection) -> {
            String query = "SELECT city_id FROM city, \"user\"" +
                    " WHERE city.user_id = \"user\".id AND \"user\".login = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Integer> idSet = new ArrayList<>();

            while (resultSet.next()) {
                idSet.add(resultSet.getInt("city_id"));
            }

            return idSet;
        });
    }

    public synchronized List<List<Integer>> getUserElementsId() throws DataBaseException {
        return processQuery((Connection connection) -> {
            String query = "SELECT login FROM \"user\"";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<List<Integer>> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(getUserElementsId(resultSet.getString("login")));
            }
            return result;
        });
    }

    public synchronized boolean validateData(String login, String password) throws DataBaseException {
        String truePassword = getPassword(login);
        return passwordEncrypter.encryptString(password).equals(truePassword);
    }
}
