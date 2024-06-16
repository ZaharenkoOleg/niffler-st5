package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement allPeopleButton = $x("//a[@href='/people']");

    public AllPeoplePage openPeoplePage() {
        allPeopleButton.click();
        return new AllPeoplePage();
    }
}