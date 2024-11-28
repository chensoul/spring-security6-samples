

package com.chensoul.security;

import com.chensoul.security.page.HomePage;
import com.chensoul.security.page.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Integration tests.
 *
 * @author Michael Simons
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainITests {

	private WebDriver driver;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		this.driver = new HtmlUnitDriver();
	}

	@AfterEach
	void tearDown() {
		this.driver.quit();
	}

	@Test
	void login() {
		final LoginPage loginPage = HomePage.to(this.driver, this.port);
		loginPage.assertAt();

		HomePage homePage = loginPage.loginForm().username("user").password("password").submit();
		homePage.assertAt();

		LoginPage logoutSuccess = homePage.logout();
		logoutSuccess.assertAt();
	}

}