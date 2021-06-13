package DataBase;

import BaseClass.City;
import CollectionUtils.CollectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public interface DataBaseManager {
   //void connectToTables();
   Integer addCity(City city, String userName) throws DataBaseException;
   <T> T processQuery(SqlFunction<Connection, T> query) throws DataBaseException;
   void processQuery(SqlConsumer<Connection> query) throws DataBaseException;
   boolean remove_by_id(int id, String userName) throws DataBaseException;
   boolean update_by_id(City city, int id, String userName) throws DataBaseException;
   void cityFromDB(CollectionManager collectionManagerImpl) throws DataBaseException;
   String getPassword(String user) throws DataBaseException;
   boolean CheckUserExist(String userName) throws DataBaseException;
   void addUser(String userName, String password) throws DataBaseException;
   ArrayList<Integer> clearUserDB(String userName) throws DataBaseException;
   List<Integer> getUserElementsId(String userName) throws DataBaseException;
   boolean validateData(String login, String password) throws DataBaseException;
   List<List<Integer>> getUserElementsId() throws DataBaseException;
}
