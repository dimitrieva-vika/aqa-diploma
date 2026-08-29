package ru.netology.diploma.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest extends BaseTest {

    private final MainPage mainPage = new MainPage();

    @Test
    @DisplayName("01. Успешная покупка Debit (APPROVED)")
    void shouldSuccessPaymentWithApprovedCard() {
        var card = DataHelper.getApprovedCard();
        var form = mainPage.selectBuy();
        form.fillForm(card);
        form.submit();
        form.waitForSuccess();

        assertEquals("APPROVED", DataHelper.getLastPaymentStatus());
        assertEquals(4500000, DataHelper.getLastPaymentAmount());
    }

    @Test
    @DisplayName("02. Отказ в покупке Debit (DECLINED)")
    void shouldFailPaymentWithDeclinedCard() {
        var card = DataHelper.getDeclinedCard();
        var form = mainPage.selectBuy();
        form.fillForm(card);
        form.submit();
        form.waitForError();

        assertEquals("DECLINED", DataHelper.getLastPaymentStatus());
    }

    @Test
    @DisplayName("03. Пустой номер карты")
    void shouldShowErrorForEmptyCardNumber() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo("", card.getMonth(), card.getYear(),
                card.getHolder(), card.getCvc());

        var form = mainPage.selectBuy();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("number");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("04. Пустой месяц")
    void shouldShowErrorForEmptyMonth() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), "", card.getYear(),
                card.getHolder(), card.getCvc());

        var form = mainPage.selectBuy();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("month");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("05. Пустой год")
    void shouldShowErrorForEmptyYear() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(), "",
                card.getHolder(), card.getCvc());

        var form = mainPage.selectBuy();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("06. Пустое поле Владелец")
    void shouldShowErrorForEmptyHolder() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), "", card.getCvc());

        var form = mainPage.selectBuy();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("holder");
        assertEquals("Поле обязательно для заполнения", error);
    }

    @Test
    @DisplayName("07. Пустое поле CVC")
    void shouldShowErrorForEmptyCvc() {
        var card = DataHelper.getApprovedCard();
        var emptyCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), card.getHolder(), "");

        var form = mainPage.selectBuy();
        form.fillForm(emptyCard);
        form.submit();

        String error = form.getFieldError("cvc");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("08. Истекший срок (некорректный год)")
    void shouldShowErrorForExpiredYear() {
        var card = DataHelper.getApprovedCard();
        var expiredCard = new DataHelper.CardInfo(card.getNumber(), "08", "20",
                card.getHolder(), "123");

        var form = mainPage.selectBuy();
        form.fillForm(expiredCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Истёк срок действия карты", error);
    }

    @Test
    @DisplayName("09. Истекший срок (некорректный месяц) - ошибка у поля Год")
    void shouldShowErrorForExpiredMonth() {
        var card = DataHelper.getApprovedCard();
        var expiredCard = new DataHelper.CardInfo(card.getNumber(), "01", "25",
                card.getHolder(), "123");

        var form = mainPage.selectBuy();
        form.fillForm(expiredCard);
        form.submit();

        String error = form.getFieldError("year");
        assertEquals("Истёк срок действия карты", error);
    }

    @Test
    @DisplayName("10. Короткий номер карты")
    void shouldShowErrorForShortCardNumber() {
        var card = DataHelper.getApprovedCard();
        var shortCard = new DataHelper.CardInfo("4444 4444 4444 443",
                card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());

        var form = mainPage.selectBuy();
        form.fillForm(shortCard);
        form.submit();

        String error = form.getFieldError("number");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("11. Короткий CVC")
    void shouldShowErrorForShortCvc() {
        var card = DataHelper.getApprovedCard();
        var shortCard = new DataHelper.CardInfo(card.getNumber(), card.getMonth(),
                card.getYear(), card.getHolder(), "12");

        var form = mainPage.selectBuy();
        form.fillForm(shortCard);
        form.submit();

        String error = form.getFieldError("cvc");
        assertEquals("Неверный формат", error);
    }

    @Test
    @DisplayName("12. Пустая форма")
    void shouldShowErrorsForEmptyForm() {
        var form = mainPage.selectBuy();
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
}