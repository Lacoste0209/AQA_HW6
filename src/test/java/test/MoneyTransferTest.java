package test;

import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Transfer from the first card to the second")
    void shouldTransferMoneyFromFirst() {
        DashboardPage dashboardPage = new DashboardPage();
        int sum = 1000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        var refillCardBalancePage = dashboardPage.cardRelifPage(1);
        var cardNum = DataHelper.getFirstCard();
        var dashboardPage2 = refillCardBalancePage.transferValue(cardNum, sum);
        int endBalance1 = dashboardPage2.getCardBalance(0);
        int endBalance2 = dashboardPage2.getCardBalance(1);
        assertEquals(initBalance1 - sum, endBalance1);
        assertEquals(initBalance2 + sum, endBalance2);
    }

    @Test
    @DisplayName("Transfer from the second card to the first")
    void shouldTransferMoneyFromSecond() {
        DashboardPage dashboardPage = new DashboardPage();
        int sum = 1000;
        int initBalance1 = dashboardPage.getCardBalance(0);
        int initBalance2 = dashboardPage.getCardBalance(1);
        var refillCardBalancePage = dashboardPage.cardRelifPage(0);
        var cardNum = DataHelper.getSecondCard();
        var dashboardPage2 = refillCardBalancePage.transferValue(cardNum, sum);
        int endBalance1 = dashboardPage2.getCardBalance(0);
        int endBalance2 = dashboardPage2.getCardBalance(1);
        assertEquals(initBalance1 + sum, endBalance1);
        assertEquals(initBalance2 - sum, endBalance2);
    }

    @Test
    @DisplayName("Should not transfer when there is insufficient balance")
    void insufficientFunds() {
        DashboardPage dashboardPage = new DashboardPage();
        int initBalance1 = dashboardPage.getCardBalance(0);
        int sum = initBalance1 + 1000;
        var refillCardBalancePage = dashboardPage.cardRelifPage(1);
        var cardNum = DataHelper.getFirstCard();
        refillCardBalancePage.errorTransfer(cardNum, sum);
    }
}
