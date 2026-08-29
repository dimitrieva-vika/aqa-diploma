package ru.netology.diploma.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.diploma.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentFormPage {

    private final SelenideElement cardNumber = $$(".input__top").filterBy(text("Номер карты")).first().parent().$("input");
    private final SelenideElement month = $$(".input__top").filterBy(text("Месяц")).first().parent().$("input");
    private final SelenideElement year = $$(".input__top").filterBy(text("Год")).first().parent().$("input");
    private final SelenideElement holder = $$(".input__top").filterBy(text("Владелец")).first().parent().$("input");
    private final SelenideElement cvc = $$(".input__top").filterBy(text("CVC/CVV")).first().parent().$("input");
    private final SelenideElement continueButton = $$(".button__text").filterBy(text("Продолжить")).first();

    public void fillForm(DataHelper.CardInfo card) {
        cardNumber.setValue(card.getNumber());
        month.setValue(card.getMonth());
        year.setValue(card.getYear());
        holder.setValue(card.getHolder());
        cvc.setValue(card.getCvc());
    }

    public void submit() {
        continueButton.click();
    }

    public void waitForSuccess() {
        $(".notification_status_ok .notification__content").shouldHave(text("Операция одобрена Банком"));
    }

    public void waitForError() {
        $(".notification_status_error .notification__content").shouldHave(text("Ошибка! Банк отказал в проведении операции"));
    }

    public String getFieldError(String field) {
        SelenideElement element;
        switch (field) {
            case "number":
                element = $$(".input__top").filterBy(text("Номер карты")).first()
                        .closest(".input").$(".input__sub");
                break;
            case "month":
                element = $$(".input__top").filterBy(text("Месяц")).first()
                        .closest(".input").$(".input__sub");
                break;
            case "year":
                element = $$(".input__top").filterBy(text("Год")).first()
                        .closest(".input").$(".input__sub");
                break;
            case "holder":
                element = $$(".input__top").filterBy(text("Владелец")).first()
                        .closest(".input").$(".input__sub");
                break;
            case "cvc":
                element = $$(".input__top").filterBy(text("CVC/CVV")).first()
                        .closest(".input").$(".input__sub");
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
        return element.getText();
    }
}