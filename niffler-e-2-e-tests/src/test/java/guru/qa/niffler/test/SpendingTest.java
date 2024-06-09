package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@ExtendWith({CategoryExtension.class, SpendExtension.class})
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Test
    void anotherTest() {
        open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").should(visible);
    }

    @GenerateCategory(
            category = "Обучение",
            username = "zakharenkoo"
    )

    @GenerateSpend(
            category = "Обучение",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            username = "zakharenkoo"
    )

    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin("zakharenkoo", "ZaharenkoOP");

        int initialSize = mainPage.getSpendingsCount();
        mainPage = mainPage.selectCheckboxByDescription(spendJson.description())
                .clickDeleteButton();

        mainPage.checkDeletedSpendings(initialSize);
    }
}