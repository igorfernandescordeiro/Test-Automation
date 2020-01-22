package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testAutomation_step {
    WebDriver driver;
    String nameToCompere;

    @Given("^I navigate to \"([^\"]*)\"$")
    public void iNavigateTo(String website) throws Throwable {
        System.setProperty("webdriver.chrome.driver","/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(website);
    }

    @When("^I select the option “Books” in the dropdown next to the search text input criteria$")
    public void iSelectTheOptionBooksInTheDropdownNextToTheSearchTextInputCriteria() {
        WebElement dropdown = driver.findElement(By.id("searchDropdownBox"));
        new Select(dropdown).selectByVisibleText("Books");
    }
    @Then("^I search for \"([^\"]*)\"$")
    public void iSearchFor(String itemtosearch) throws Throwable {
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(itemtosearch);
        driver.findElement(By.xpath("//input[@value='Go']")).click();
    }

    @And("^I select the cheapest book of the page without using any sorting method available$")
    public void iSelectTheCheapestBookOfThePageWithoutUsingAnySortingMethodAvailable() throws InterruptedException {
        List<WebElement> listOfElements = driver.findElements(By.cssSelector("div[data-cel-widget^='search_result_']"));
//        System.out.println("Number of elements: " + listOfElements.size() );
        Double minorPrice = null;
        WebElement elementToClick = null;

        for (int i = 0; i<listOfElements.size(); i++) {
            WebElement element = listOfElements.get(i);
//            System.out.println("Index: " + i);
//            System.out.println(element.findElement(By.cssSelector("h2 span")).getText());
//            System.out.print(element.findElement(By.cssSelector(".a-price .a-price-whole")).getText());
//            System.out.print(".");
//            System.out.println(element.findElement(By.cssSelector(".a-price .a-price-fraction")).getText());
//            System.out.println(" -------- ");
            String whole = element.findElement(By.cssSelector(".a-price .a-price-whole")).getText();
            String fraction = element.findElement(By.cssSelector(".a-price .a-price-fraction")).getText();
            String completePrice = whole+"."+fraction;
            Double price = Double.parseDouble(completePrice);

            if (minorPrice == null){
                minorPrice = price;
                elementToClick = element;
                nameToCompere = elementToClick.findElement(By.cssSelector("h2 span")).getText();
            }else if (price < minorPrice) {
                elementToClick = element;
                minorPrice = price;
                nameToCompere = elementToClick.findElement(By.cssSelector("h2 span")).getText();
            }
        }
        elementToClick.findElement(By.cssSelector(".a-price .a-price-whole")).click();
//        System.out.println("Menor valor: "+ minorPrice);
    }

    @When("^I reach the detailed book page, I check if the name in the header is the same name of the book that I select previously$")
    public void iReachTheDetailedBookPageICheckIfTheNameInTheHeaderIsTheSameNameOfTheBookThatISelectPreviously() {
        Assert.assertEquals(nameToCompere, driver.findElement(By.id("ebooksProductTitle")).getText());
//        System.out.println("Before " + nameToCompere);
//        System.out.println("Title of the book founded: " + driver.findElement(By.id("ebooksProductTitle")).getText());
        driver.close();
    }
}
