package ru.netology.diploma.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreditTest extends BaseTest {

    private final MainPage mainPage = new MainPage();

    @Test
    @DisplayName("13. Успешная покупка Credit (APPROVED)")
    void shouldSuccessCreditWithApprovedCard() {
        var card = DataHelper.getApprovedCard();
        var form = mainPage.selectCredit();
        form.fillForm(card);
        form.submit();
        form.waitForSuccess();

        assertEquals("APPROVED", DataHelper.getLastCreditStatus());
    }

    @Test
    @DisplayName("14. Отказ в кредите (DECLINED)")
    void shouldFailCreditWithDeclinedCard() {
        var card = DataHelper.getDeclinedCard();
        var form = mainPage.selectCredit();
        form.fillForm(card);
        form.submit();
        form.waitForError();

        assertEquals("DECLINED", DataHelper.getLastCreditStatus());
    }

    @Test
    @DisplayName("15. Credit: пустой номер карты")
    void shouldShowErrorForEmptyCardNumber() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo("", card.getMonth(), card.getYear(),
                card.getHolder(), card.getCvc());

        var form = mainPage.selectCredit();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("number");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("16. Credit: пустой месяц")
    void shouldShowErrorForEmptyMonth() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), "", card.getYear(),
                card.getHolder(), card.getCvc());

        var form = mainPage.selectCredit();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("month");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("17. Credit: пустой год")
    void shouldShowErrorForEmptyYear() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(), "",
                card.getHolder(), card.getCvc());

        var form = mainPage.selectCredit();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("18. Credit: пустое поле Владелец")
    void shouldShowErrorForEmptyHolder() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), "", card.getCvc());

        var form = mainPage.selectCredit();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("holder");
        assertEquals("Поле обязательно для заполнения", error);
    }

    @Test
    @DisplayName("19. Credit: пустое поле CVC")
    void shouldShowErrorForEmptyCvc() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), card.getHolder(), "");

        var form = mainPage.selectCredit();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("cvc");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("20. Credit: истекший срок (некорректный год)")
    void shouldShowErrorForExpiredYear() {
        var expiredCard = DataHelper.getExpiredYearCard();
        var form = mainPage.selectCredit();
        form.fillForm(expiredCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Истёк срок действия карты", error);
    }

    @Test
    @DisplayName("21. Credit: истекший срок (некорректный месяц) - ошибка у поля Год")
    void shouldShowErrorForExpiredMonth() {
        var expiredCard = DataHelper.getExpiredMonthCard();
        var form = mainPage.selectCredit();
        form.fillForm(expiredCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Истёк срок действия карты", error);
    }

    @Test
    @DisplayName("22. Credit: короткий номер карты")
    void shouldShowErrorForShortCardNumber() {
        var card = DataHelper.getApprovedCard();
        var shortCard = new DataHelper.CardInfo("4444 4444 4444 443",
                card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());

        var form = mainPage.selectCredit();
        form.fillForm(shortCard);
        form.submit();

        String error = form.getFieldError("number");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("23. Credit: короткий CVC")
    void shouldShowErrorForShortCvc() {
        var card = DataHelper.getApprovedCard();
        var shortCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), card.getHolder(), "12");

        var form = mainPage.selectCredit();
        form.fillForm(shortCard);
        form.submit();

        String error = form.getFieldError("cvc");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("24. Credit: пустая форма")
    void shouldShowErrorsForEmptyForm() {
        var form = mainPage.selectCredit();
        form.submit();

        String[] expectedErrors = {
                form.getFieldError("number"),
                form.getFieldError("month"),
                form.getFieldError("year"),
                form.getFieldError("holder"),
                form.getFieldError("cvc")
        };

        assertEquals("Неверный формат", expectedErrors[0]);
        assertEquals("Неверный формат", expectedErrors[1]);
        assertEquals("Неверный формат", expectedErrors[2]);
        assertEquals("Поле обязательно для заполнения", expectedErrors[3]);
        assertEquals("Неверный формат", expectedErrors[4]);
    }

    // ===== НОВЫЕ ТЕСТЫ =====

    @Test
    @DisplayName("31. Credit: CVC = 000 - ошибка у поля Месяц (БАГ)")
    void shouldShowErrorForCvc000() {
        var card = DataHelper.getApprovedCard();
        var testCard = new DataHelper.CardInfo(
                card.getNumber(),
                "08",
                "26",
                card.getHolder(),
                "000"
        );

        var form = mainPage.selectCredit();
        form.fillForm(testCard);
        form.submit();

        String cvcError = form.getFieldError("cvc");
        String monthError = form.getFieldError("month");

        assertNotEquals("Неверный формат", cvcError, "CVC должен показывать ошибку");
        assertEquals("Неверно указан срок действия карты", monthError, "БАГ: ошибка у поля Месяц");
    }

    @Test
    @DisplayName("32. Credit: Владелец с цифрами - ошибка у поля Месяц (БАГ)")
    void shouldShowErrorForHolderWithDigits() {
        var card = DataHelper.getApprovedCard();
        var testCard = new DataHelper.CardInfo(
                card.getNumber(),
                "08",
                "26",
                "Ivan123",
                "123"
        );

        var form = mainPage.selectCredit();
        form.fillForm(testCard);
        form.submit();

        String holderError = form.getFieldError("holder");
        String monthError = form.getFieldError("month");

        assertNotEquals("Поле обязательно для заполнения", holderError, "Владелец должен показывать ошибку");
        assertEquals("Неверно указан срок действия карты", monthError, "БАГ: ошибка у поля Месяц");
    }

    @Test
    @DisplayName("33. Credit: Владелец со спецсимволами - ошибка у поля Месяц (БАГ)")
    void shouldShowErrorForHolderWithSpecialChars() {
        var card = DataHelper.getApprovedCard();
        var testCard = new DataHelper.CardInfo(
                card.getNumber(),
                "08",
                "26",
                "Ivan@#$",
                "123"
        );

        var form = mainPage.selectCredit();
        form.fillForm(testCard);
        form.submit();

        String holderError = form.getFieldError("holder");
        String monthError = form.getFieldError("month");

        assertNotEquals("Поле обязательно для заполнения", holderError, "Владелец должен показывать ошибку");
        assertEquals("Неверно указан срок действия карты", monthError, "БАГ: ошибка у поля Месяц");
    }

    @Test
    @DisplayName("34. Credit: Владелец с SQL-инъекцией - ошибка у поля Месяц (БАГ)")
    void shouldShowErrorForHolderWithSqlInjection() {
        var card = DataHelper.getApprovedCard();
        var testCard = new DataHelper.CardInfo(
                card.getNumber(),
                "08",
                "26",
                "Ivan' OR '1'='1",
                "123"
        );

        var form = mainPage.selectCredit();
        form.fillForm(testCard);
        form.submit();

        String holderError = form.getFieldError("holder");
        String monthError = form.getFieldError("month");

        assertNotEquals("Поле обязательно для заполнения", holderError, "Владелец должен показывать ошибку");
        assertEquals("Неверно указан срок действия карты", monthError, "БАГ: ошибка у поля Месяц");
    }
}