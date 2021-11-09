 package selenium;
 
 import io.github.bonigarcia.wdm.WebDriverManager;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.TimeUnit;
 import java.util.function.Function;
 import org.openqa.selenium.By;
 import org.openqa.selenium.JavascriptExecutor;
 import org.openqa.selenium.WebDriver;
 import org.openqa.selenium.WebElement;
 import org.openqa.selenium.chrome.ChromeDriver;
 import org.openqa.selenium.chrome.ChromeOptions;
 import org.openqa.selenium.firefox.FirefoxDriver;
 import org.openqa.selenium.firefox.FirefoxOptions;
 import org.openqa.selenium.support.ui.WebDriverWait;
 
 
 public class DriverExtrator
 {
   private WebDriver driver;
   
   public DriverExtrator(boolean headless, boolean disableImages, boolean ehFireFox) {
     List<String> args = new ArrayList<>();
     
     if (headless) {
       args.add("--headless");
     } else {
       args.add("--disable-notifications");
     } 
     if (disableImages) {
       args.add("--disable-gpu");
       args.add("--blink-settings=imagesEnabled=false");
     } 
 
     
     if (ehFireFox) {
       WebDriverManager.firefoxdriver().setup();
       FirefoxOptions options = new FirefoxOptions();
       options.addArguments(args);
       this.driver = (WebDriver)new FirefoxDriver(options);
     } else {
       WebDriverManager.chromedriver().setup();
       ChromeOptions options = new ChromeOptions();
       options.addArguments(args);
       this.driver = (WebDriver)new ChromeDriver(options);
     } 
     this.driver.manage().timeouts().pageLoadTimeout(5L, TimeUnit.MINUTES);
     this.driver.manage().window().maximize();
   }
   
   public void waitForLoad() throws InterruptedException {
     (new WebDriverWait(this.driver, 60L)).until((Function)(wd -> Boolean.valueOf(((JavascriptExecutor)wd).executeScript("return document.readyState", new Object[0]).equals("complete"))));
   }
 
   
   public void hoverMouseJavaScript(WebElement webElement) {
     String javaScript = "var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);arguments[0].dispatchEvent(evObj);";
 
 
     
     ((JavascriptExecutor)this.driver).executeScript(javaScript, new Object[] { webElement });
   }
   
   public WebDriver getDriver() {
     return this.driver;
   }
   
   public void sendKeysSlowly(WebElement element, String mensagem) throws InterruptedException {
     for (int i = 0; i < (mensagem.toCharArray()).length; i++) {
       element.sendKeys(new CharSequence[] { (new StringBuilder(String.valueOf(mensagem.charAt(i)))).toString() });
       TimeUnit.MILLISECONDS.sleep(15L);
     } 
   }
   
   public boolean existeElemento(String path) {
     return (this.driver.findElements(By.xpath(path)).size() > 0);
   }
 }


