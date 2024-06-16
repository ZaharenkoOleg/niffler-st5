package guru.qa.niffler.test;


import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AllPeoplePage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

@WebTest
public class FriendshipStatusTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Test
    public void userWithSentInvite1(@User(value = INVITE_SENT) UserJson userForTest,
                                    @User(value = INVITE_RECEIVED) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInviteSent(anotherUserForTest.username());
    }

    @Test
    public void userWithSentInvite2(@User(value = INVITE_SENT) UserJson userForTest,
                                    @User(value = INVITE_RECEIVED) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInviteSent(anotherUserForTest.username());
    }

    @Test
    public void userWithSentInvite3(@User(value = INVITE_SENT) UserJson userForTest,
                                    @User(value = INVITE_RECEIVED) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInviteSent(anotherUserForTest.username());
    }

    @Test
    public void userWithReceivedInvite1(@User(value = INVITE_RECEIVED) UserJson userForTest,
                                        @User(value = INVITE_SENT) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInvitationReceived(anotherUserForTest.username());
    }

    @Test
    public void userWithReceivedInvite2(@User(value = INVITE_RECEIVED) UserJson userForTest,
                                        @User(value = INVITE_SENT) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInvitationReceived(anotherUserForTest.username());
    }

    @Test
    public void userWithReceivedInvite3(@User(value = INVITE_RECEIVED) UserJson userForTest,
                                        @User(value = INVITE_SENT) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isInvitationReceived(anotherUserForTest.username());
    }

    @Test
    void userWithFriend1(@User(value = WITH_FRIEND) UserJson userForTest,
                         @User(value = WITH_FRIEND) UserJson anotherUserForTest) {
        MainPage mainPage = open("http://127.0.0.1:3000/", WelcomePage.class)
                .clickLogInButton()
                .doLogin(userForTest.username(), userForTest.testData().password());
        AllPeoplePage allPeoplePage = mainPage.openPeoplePage();
        allPeoplePage.isFriend(anotherUserForTest.username());
    }
}
