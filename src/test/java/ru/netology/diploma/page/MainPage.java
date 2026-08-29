package ru.netology.diploma.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement buyButton = $$(".button__text").filterBy(text("Купить")).first();
    private final SelenideElement creditButton = $$(".button__text").filterBy(text("Купить в кредит")).first();

    public PaymentFormPage selectBuy() {
        buyButton.click();
        return new PaymentFormPage();
    }

    public CreditFormPage selectCredit() {
        creditButton.click();
        return new CreditFormPage();
    }
}