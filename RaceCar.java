package banggood;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RaceCar {

	public static void main(String[] args) throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
//		options.addArguments("--headless");
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.get(
				"https://www.banggood.in/Wholesale-RC-Car-ca-7008.html?cat_id=7008&page=1&direct=0&rec_uid=0&bid=81131&sort=1&sortType=desc");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

		WebElement page = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='page-total']")));
		String pageno = page.getText();
		int pages = Integer.parseInt(pageno);
		int p = 2;
		int i = 1;
		String name = null, title = null, ship = null, price = null;
		System.out.println("Scrapping page : 1");
		do {
			

			try {
				WebElement sort = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Newest']")));
				sort.click();
			} catch (Exception e) {
				// Re-locate the sort button and click again
				WebElement sort = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Newest']")));
				sort.click();
			}

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//			Thread.sleep(3000);

			List<WebElement> products = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='product-list']/ul/li/div/span/a")));
			int num = products.size();
//			System.out.println(num);

			for (int j = 1; j <= Math.min(num, 2); j++) {


				try {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

					WebElement productList = wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("(//div[@class='product-list']/ul/li/div/span/a)[" + j + "]")));
					productList.sendKeys(Keys.ENTER);
					name= driver.findElement(By.xpath("(//span[@property='name'])[last()]")).getText();
					title = driver.findElement(By.xpath("//span[@class='product-title-text']")).getText();
					price = driver.findElement(By.xpath("//span[@class='main-price']")).getText();
					ship = driver.findElement(By.xpath("//div[@class='shipping-price']/em")).getText();
				} catch (Exception e) {
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
					js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

					WebElement productList = wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("(//div[@class='product-list']/ul/li/div/span/a)[" + j + "]")));
					productList.sendKeys(Keys.ENTER);
					name= driver.findElement(By.xpath("(//span[@property='name'])[last()]")).getText();
					title = driver.findElement(By.xpath("//span[@class='product-title-text']")).getText();
					price = driver.findElement(By.xpath("//span[@class='main-price']")).getText();
					ship = driver.findElement(By.xpath("//div[@class='shipping-price']/em")).getText();
					System.out.println("Error Encounter at number "+j+"in page number +"+p);;
					continue;
				}

				System.out.println("Sr. No : " + i++);
				System.out.println("Item Name: " + name);
				System.out.println("Item Title: " + title);
				System.out.println("Item Price : " + price);
				System.out.print("Shipping Cost : ");
				if (ship == "Free shipping")
					System.out.println(0);
				else
					System.out.println(ship);
				System.out.println("Images");
				List<WebElement> img = driver.findElements(By.xpath("//li[@class='product']/a/img"));
				for (WebElement pic : img)
					System.out.println(pic.getAttribute("src"));

				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

				try {
					do {
						wait = new WebDriverWait(driver, Duration.ofSeconds(60));

						WebElement showMore = driver.findElement(By.xpath("(//div[text()='SHOW MORE'])[last()]"));

						if (showMore.isDisplayed()) { // Check if the element is displayed
							System.out.println("found it");
							showMore.click(); // Click if it exists
//							Thread.sleep(1000); // Wait for the content to load
						}

					} while (driver.findElement(By.xpath("(//div[text()='SHOW MORE'])[last()]")).isDisplayed());
					System.out.print("Description : ");

				} catch (Exception e) {

					System.out.print("Description : ");
				}

				try {
					WebElement table = wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//div[@class='product-oa-desc']/table")));

					List<WebElement> rows = table.findElements(By.tagName("tr"));
					for (WebElement row : rows) {
						List<WebElement> cells = row.findElements(By.tagName("td"));
						for (WebElement cell : cells) {
							System.out.print(cell.getText() + "\t");
						}
						System.out.println();

					}
				} catch (Exception e) {
					WebElement desc = wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//div[@class='product-oa-desc']")));
					
					System.out.println(desc.getText());
				}
				driver.navigate().back();
			}

			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

			WebElement tel = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='num']")));
			tel.clear();
			tel.sendKeys(String.valueOf(p));
			driver.findElement(By.xpath("//a[text()='Go']")).sendKeys(Keys.ENTER);

			WebElement check = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='num']/span")));
			if(p!=9)
			System.out.println("Scrapping page : "+ p);
			
			p++;
			

		} while (p <= pages+1);

		driver.quit();
	}

}
