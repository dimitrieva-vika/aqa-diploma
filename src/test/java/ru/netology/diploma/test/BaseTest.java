package ru.netology.diploma.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.diploma.data.DataHelper;

import static com.codeborne.selenide.Selenide.open;

public class BaseTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
        com.codeborne.selenide.Configuration.timeout = 15000;
        com.codeborne.selenide.Configuration.headless = false;
        com.codeborne.selenide.Configuration.browserSize = "1920x1080";
        com.codeborne.selenide.Configuration.pollingInterval = 200;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        DataHelper.clearDb();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }
}