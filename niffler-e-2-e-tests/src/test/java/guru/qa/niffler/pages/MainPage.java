package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.sizeLessThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement spendingsTable = $x("//table[@class='table spendings-table']/tbody");
    private final By SpendingTableRow = By.xpath("./tr");
    private final By spendingTableCell = By.xpath("./td");
    private final SelenideElement deleteButton = $x("//section[@class='spendings__bulk-actions']/button");

    @Step("Выбираем чекбокс по описанию")
    public MainPage selectCheckboxByDescription(String description) {
        findSpendingByDescription(description)
                .$(spendingTableCell).scrollIntoView(true).click();
        return this;
    }

    @Step("Нажимаем кнопку 'Delete selected'")
    public MainPage clickDeleteButton() {
        deleteButton.click();
        return this;
    }

    @Step("Проверяем количество трат после удаления")
    public void checkDeletedSpendings(int initialSize) {
        getAllSpendings().shouldHave(sizeLessThan(initialSize).because("Количество трат после удаления не соответствует ожидаемому"));
    }

    public int getSpendingsCount() {
        return getAllSpendings().size();
    }

    private ElementsCollection getAllSpendings() {
        return spendingsTable.scrollIntoView("{block: \"center\"}")
                .$$(SpendingTableRow);
    }

    private SelenideElement findSpendingByDescription(String description) {
        return getAllSpendings()
                .find(text(description));
    }
}