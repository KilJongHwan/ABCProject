package com.abc.jdbc.util;
import java.sql.*;

// 데이터베이스에 연결하려면 ABC 계정의 연결 정보를 사용해야 합니다.
// 아래와 같이 데이터베이스 연결 정보를 설정하는 코드를 작성합니다.

public class DatabaseConnection {
    private DatabaseConnection(){} // 기본생성자는 public으로 생성되기에, private으로 생성자 하나 생성
    // ABC@//localhost:1521/xe
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
    private static final String USER = "gilbert"; // ABC 계정의 사용자 이름
    private static final String PASSWORD = "1234"; // ABC 계정의 비밀번호

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("DatabaseConnection Error! : " + e);
        }
        return connection;
    }
    public static void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            System.out.println("MembersDAO close Error! : " + e);
        }
    }
    public static void close(Statement st) {
        try {
            if (st != null && !st.isClosed()) {
                st.close();
            }
        } catch (Exception e){
            System.out.println("MembersDAO close Error! : " + e);
        }
    }
    public static void close(PreparedStatement pst) {
        try {
            if (pst != null && !pst.isClosed()) {
                pst.close();
            }
        } catch (Exception e){
            System.out.println("MembersDAO close Error! : " + e);
        }
    }
    public static void close(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (Exception e){
            System.out.println("MembersDAO close Error! : " + e);
        }
    }
}
