package com.yourteam.englishapp.data.local;

import com.yourteam.englishapp.BuildConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Quản lý kết nối JDBC tới SQL Server (jTDS). Singleton dạng lớp tiện ích tĩnh. */
public final class DatabaseClient {
    private static final String URL = "jdbc:jtds:sqlserver://" + BuildConfig.DB_HOST
            + ":" + BuildConfig.DB_PORT + "/" + BuildConfig.DB_NAME
            + ";loginTimeout=5;socketTimeout=20";

    private DatabaseClient() {}

    /** Phải gọi từ luồng nền, KHÔNG gọi trên main thread. */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy driver jTDS", e);
        }
        DriverManager.setLoginTimeout(5);   // giây, tránh treo khi sai IP
        return DriverManager.getConnection(URL, BuildConfig.DB_USER, BuildConfig.DB_PASS);
    }
}
