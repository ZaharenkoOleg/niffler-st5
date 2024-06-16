package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class WelcomePage {

    private final SelenideElement loginButton = $x("//a[@href='/redirect']");

    @Step("Нажимаем кнопку 'Sign In'")
    public AuthorizationPage clickLogInButton() {
        loginButton.click();
        return new AuthorizationPage();
    }
}