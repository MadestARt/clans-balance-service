package org.asvladimirov.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@UtilityClass
public class ConnectionManager {
    private static BlockingQueue<Connection> connections;
    private static final String URL_KEY = "datasource.url";
    private static final String USER_KEY = "datasource.user";
    private static final String PASSWORD_KEY = "datasource.password";

    static {
        loadDriver();
        loadConnections();
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName("org.postgresql.Driver");
    }

    public static Connection get() {
        try {
            return connections.take();
        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_KEY), PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void loadConnections() {
        connections = new ArrayBlockingQueue<>(5);
        for (int i = 0; i < 5; i++) {
            Connection connection = connect();
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                    ((proxy, method, args) -> method.getName().equals("close") ?
                            connections.add((Connection) proxy) :
                            method.invoke(connection, args)));
            connections.add(proxyConnection);

        }
    }
}
