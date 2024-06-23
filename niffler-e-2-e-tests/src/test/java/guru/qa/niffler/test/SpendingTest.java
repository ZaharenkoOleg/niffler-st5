package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.JdbcCategoryExtension;
import guru.qa.niffler.jupiter.extension.JdbcSpendExtension;
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

@WebTest
@ExtendWith({JdbcCategoryExtension.class, JdbcSpendExtension.class})
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Test
    void anotherTest() {
        open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").should(visible);
    }

    @Category(
            category = "Обучение27",
            username = "zakharenkoo"
    )

    @Spend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB
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