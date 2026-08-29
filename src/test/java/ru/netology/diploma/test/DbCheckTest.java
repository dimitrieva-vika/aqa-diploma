package ru.netology.diploma.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DbCheckTest extends BaseTest {

    private final MainPage mainPage = new MainPage();

    @Test
    @DisplayName("25. Проверка записи в БД после успешного платежа")
    void shouldCheckPaymentRecordInDb() {
        var card = DataHelper.getApprovedCard();
        var form = mainPage.selectBuy();

        form.fillForm(card);
        form.submit();
        form.waitForSuccess();

        assertEquals("APPROVED", DataHelper.getLastPaymentStatus());
        assertEquals(4500000, DataHelper.getLastPaymentAmount());
        assertNotNull(DataHelper.getLastPaymentId());
    }

    @Test
    @DisplayName("26. Проверка записи в БД после успешного кредита")
    void shouldCheckCreditRecordInDb() {
        var card = DataHelper.getApprovedCard();
        var form = mainPage.selectCredit();

        form.fillForm(card);
        form.submit();
        form.waitForSuccess();

        assertEquals("APPROVED", DataHelper.getLastCreditStatus());
        assertNotNull(DataHelper.getLastCreditId());
    }
}