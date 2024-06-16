package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class AllPeoplePage {

    private final SelenideElement allPeoplesTable = $x("//table[@class='table abstract-table']/tbody");
    private final String tableRow = "./tr";
    private final String FriendshipStatus = ".//div[@class='abstract-table__buttons']/div";

    private ElementsCollection getAllPeoples() {
        return allPeoplesTable.$$x(tableRow);
    }

    public SelenideElement findUserByName(String userName) {
        return getAllPeoples().find(text(userName));
    }

    public void isFriend(String username) {
        findUserByName(username).$x(FriendshipStatus)
                .shouldHave(text("You are friends"));
    }

    public void isInviteSent(String username) {
        findUserByName(username).$x(FriendshipStatus)
                .shouldHave(text("Pending invitation"));
    }

    public void isInvitationReceived(String username) {
        findUserByName(username).$x(FriendshipStatus)
                .shouldHave(attribute("data-tooltip-id", "submit-invitation"));
    }
}