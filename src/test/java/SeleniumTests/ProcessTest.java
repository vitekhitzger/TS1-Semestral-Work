package SeleniumTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void userInteractingWithPosts(){
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        By logoutSelector = By.xpath("//input[@value='Odhlásit se']");

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutSelector));

        assertTrue(logoutButton.isDisplayed(), "Tlačítko pro odhlášení nebylo nalezeno!");

        driver.get("https://zwa.toad.cz/~jiranst1/01/post?postId=63");

        WebElement commentInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#content")));
        commentInput.sendKeys("Tvůj komentář");

        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();

        WebElement delete = driver.findElement(By.name("delete_action"));

        assertTrue(delete.isDisplayed());

        delete.click();

        WebElement likeButton = driver.findElement(By.className("heart-label"));
        likeButton.click();

        WebElement likeCount = driver.findElement(By.className("like_count"));

        Assertions.assertEquals("2", likeCount.getText());
        likeButton.click();

        driver.get("https://zwa.toad.cz/~jiranst1/01/");

        logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutSelector));

        logoutButton.click();

        By loginSelector = By.xpath("//a[@href='https://zwa.toad.cz/~jiranst1/01/loginForm']");

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(loginSelector));

        assertTrue(loginButton.isDisplayed(), "Tlačítko pro přihlášení nebylo nalezeno!");

    }

    @Test
    public void creatingUserPostingPostAndThenDeletingIt(){
        driver.get("https://zwa.toad.cz/~jiranst1/01/loginForm");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@test.cz");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Felcvut26");

        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        driver.get("https://zwa.toad.cz/~jiranst1/01/createPostForm");

        WebElement titleField = driver.findElement(By.id("title"));
        titleField.sendKeys("test pro TS1");

        WebElement uploadElement = driver.findElement(By.id("postImage"));

        File file = new File("src/main/resources/controller.png");
        String absolutePath = file.getAbsolutePath();

        uploadElement.sendKeys(absolutePath);

        WebElement image = driver.findElement(By.id("image"));
        image.click();

        WebElement post = driver.findElement(By.className("post"));

        List<WebElement> deleteButtons = post.findElements(
                By.cssSelector("button[type='submit'][name='delete_action']")
        );

        Assertions.assertFalse(deleteButtons.isEmpty(), "Chybí tlačítko pro smazání.");
        deleteButtons.getFirst().click();

         post = driver.findElement(By.className("post"));

        deleteButtons = post.findElements(
                By.cssSelector("button[type='submit'][name='delete_action']")
        );

        Assertions.assertTrue( deleteButtons.isEmpty(), "Chybí tlačítko pro smazání.");
    }
}
