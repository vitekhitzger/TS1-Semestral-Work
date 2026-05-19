package SeleniumTests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/registration_data.csv", numLinesToSkip = 1)
    void testRegistrationScenarios(String username, String email, String password, String passwordAgain, String expectedResult) {
        driver.get("https://zwa.toad.cz/~jiranst1/01/registerForm");

        WebElement userField = driver.findElement(By.id("username"));
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passField = driver.findElement(By.id("password"));
        WebElement passFieldAgain = driver.findElement(By.id("password2"));
        WebElement submitBtn = driver.findElement(By.className("submit-button"));

        userField.sendKeys(username == null ? "" : username);
        emailField.sendKeys(email == null ? "" : email);
        passField.sendKeys(password == null ? "" : password);
        passFieldAgain.sendKeys(password == null ? "" : passwordAgain);

        if ("success".equalsIgnoreCase(expectedResult.trim())) {
            submitBtn.click();
            String currentUrl = driver.getCurrentUrl();
            Assertions.assertTrue(currentUrl.contains("~jiranst1/01/"), "Registrace pro " + username + " měla být úspěšná.");
        } else {
            WebElement errorMsg = driver.findElement(By.id(expectedResult));
            Assertions.assertTrue(errorMsg.isDisplayed(), "Měla se zobrazit chyba pro scénář: " + expectedResult);
        }
    }

    @Test
    public void testSuccessfulLogin() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        By logoutSelector = By.xpath("//input[@value='Odhlásit se']");

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutSelector));

        Assertions.assertTrue(logoutButton.isDisplayed(), "Tlačítko pro odhlášení nebylo nalezeno!");
    }

    @Test
    public void testSuccessfulAdminLogin() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("jiranst1@cvut.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tereza86");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        String url = driver.getCurrentUrl();

        Assertions.assertEquals("https://zwa.toad.cz/~jiranst1/01/dashboard",url);
    }

    @Test
    public void testUnsuccessfulLogin() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("wrong");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        By errorMessageLocator = By.className("error");

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));

        Assertions.assertTrue(errorMessage.isDisplayed(), "Chybová hláška nebyla nalezena!");
    }

    @Test
    public void testSuccessfulLogout() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        By logoutSelector = By.xpath("//input[@value='Odhlásit se']");

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutSelector));
        logoutButton.click();

        By loginSelector = By.xpath("//a[@href='https://zwa.toad.cz/~jiranst1/01/loginForm']");

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(loginSelector));

        Assertions.assertTrue(loginButton.isDisplayed(), "Tlačítko pro přihlášení nebylo nalezeno!");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/login-names.csv", numLinesToSkip = 1)
    public void testChangeDisplayName(String username, String type) {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        driver.get("https://zwa.toad.cz/~jiranst1/01/profile");

        WebElement nameField = driver.findElement(By.id("username"));

        nameField.clear();
        nameField.sendKeys(username);
        driver.findElement(By.className("submit-button")).click();

        // 3. Kontrola, že v hlavičce nebo na profilu svítí nové jméno
        if ("success".equalsIgnoreCase(type.trim())) {
            WebElement successMessage = driver.findElement(By.className("success-message"));
            Assertions.assertTrue(successMessage.isDisplayed(), "potvrzující správa se neukázala");
        } else {
            WebElement errorMessage = driver.findElement(By.className("error"));
            Assertions.assertTrue(errorMessage.isDisplayed(), "Chyba se neukázala");
        }
    }

    @Test
    public void testUnauthorizedAccessRedirect() {
        // Odhlásíme se, pokud jsme náhodou přihlášeni
        driver.manage().deleteAllCookies();

        // Pokus o přístup na chráněnou stránku
        driver.get("https://zwa.toad.cz/~jiranst1/01/dashboard");

        // Očekáváme přesměrování na login nebo chybovou hlášku
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals("https://zwa.toad.cz/~jiranst1/01/", currentUrl);
    }

    @Test
    public void likePost() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        WebElement likeButton = driver.findElement(By.className("heart-label"));
        likeButton.click();

        WebElement likeCount = driver.findElement(By.className("like_count"));

        Assertions.assertEquals("2", likeCount.getText());
        likeButton.click();
    }

    @Test
    public void commentPost() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        driver.get("https://zwa.toad.cz/~jiranst1/01/post?postId=63");

        WebElement commentInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#content")));
        commentInput.sendKeys("Tvůj komentář");

        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();

        WebElement delete = driver.findElement(By.name("delete_action"));

        Assertions.assertTrue(delete.isDisplayed());

        delete.click();
    }

    @Test
    public void invalidCommentPost() {
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        driver.get("https://zwa.toad.cz/~jiranst1/01/post?postId=63");

        WebElement commentInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#content")));
        commentInput.sendKeys("aha");

        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();

        WebElement errorMessage = driver.findElement(By.cssSelector(".comment-zone p"));
        Assertions.assertTrue(errorMessage.isDisplayed());

    }

    @Test
    public void createAdmin(){
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("jiranst1@cvut.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tereza86");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        WebElement makeAdminButton = driver.findElement(By.className("promote-button"));
        makeAdminButton.click();

        WebElement role = driver.findElement(By.className("role-badge"));
        Assertions.assertEquals("Administrator", role.getText());
    }

    @Test
    public void deleteUser(){
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("jiranst1@cvut.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("tereza86");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        WebElement deleteUserButton = driver.findElement(By.cssSelector("table tr:last-child .delete-button"));
        deleteUserButton.click();

        WebElement role = driver.findElement(By.className("role-badge"));
        Assertions.assertEquals("Administrator", role.getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}