package ru.netology.diploma.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class DataHelper {

    private static final Faker faker = new Faker(new Locale("en"));

    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "pass";

    private DataHelper() {}

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo(
                "4444 4444 4444 4441",
                "08",
                "26",
                "Ivan Petrov",
                "123"
        );
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(
                "4444 4444 4444 4442",
                "08",
                "26",
                "Ivan Petrov",
                "123"
        );
    }

    public static CardInfo getRandomCard() {
        return new CardInfo(
                faker.finance().creditCard().replaceAll("-", " "),
                String.format("%02d", faker.number().numberBetween(1, 12)),
                String.valueOf(faker.number().numberBetween(25, 30)),
                faker.name().fullName().toUpperCase(),
                String.valueOf(faker.number().numberBetween(100, 999))
        );
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static String getLastPaymentStatus() {
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return executeQuery(query);
    }

    public static String getLastCreditStatus() {
        String query = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return executeQuery(query);
    }

    public static String getLastPaymentId() {
        String query = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1";
        return executeQuery(query);
    }

    public static String getLastCreditId() {
        String query = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return executeQuery(query);
    }

    public static int getLastPaymentAmount() {
        String query = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1";
        String result = executeQuery(query);
        return result != null ? Integer.parseInt(result) : 0;
    }

    private static String executeQuery(String query) {
        try (Connection conn = getConnection();
             var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("doesn't exist") || e.getMessage().contains("does not exist")) {
                return null;
            }
            throw new RuntimeException("Ошибка выполнения запроса: " + query, e);
        }
        return null;
    }

    public static void clearDb() {
        try (Connection conn = getConnection()) {
            var stmt = conn.createStatement();
            try {
                stmt.execute("DELETE FROM order_entity");
            } catch (SQLException e) {
                // Таблица не существует
            }
            try {
                stmt.execute("DELETE FROM payment_entity");
            } catch (SQLException e) {
                // Таблица не существует
            }
            try {
                stmt.execute("DELETE FROM credit_request_entity");
            } catch (SQLException e) {
                // Таблица не существует
            }
        } catch (SQLException e) {
            // База данных недоступна
        }
    }
}