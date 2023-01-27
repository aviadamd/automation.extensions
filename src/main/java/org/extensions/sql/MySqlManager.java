package org.extensions.sql;

import org.mySql.MySqlSharedConnector;

import java.util.Map;

public class MySqlManager {
    private static final ThreadLocal<Map<Integer, MySqlSharedConnector>> mySqlRepo = new ThreadLocal<>();
    public MySqlManager(Map<Integer, MySqlSharedConnector> sqlRepo) { mySqlRepo.set(sqlRepo); }
    public Map<Integer, MySqlSharedConnector> getMySqlRepo() { return mySqlRepo.get(); }

    public void remove() { mySqlRepo.remove(); }

}
