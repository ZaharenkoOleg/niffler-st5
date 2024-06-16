package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class AuthorizationPage {

    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement usernameInput = $x("//input[@name='username']");
    private final SelenideElement passwordInput = $x("//input[@name='password']");

    @Step("Нажимаем кнопку 'Sign In'")
    public MainPage clickLogInButton() {
        submitButton.click();
        return new MainPage();
    }

    @Step("Вводим имя пользователя {username}")
    public AuthorizationPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    @Step("Вводим пароль {password}")
    public AuthorizationPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Авторизуемся с {username}, {password}")
    public MainPage doLogin(String username, String password) {
        return setUsername(username)
                .setPassword(password)
                .clickLogInButton();
    }
}