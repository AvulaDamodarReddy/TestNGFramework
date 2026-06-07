package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;


import org.apache.logging.log4j.LogManager;  //Log4j
import org.apache.logging.log4j.Logger;  //Log4j


/*public class BaseClass {

//public static WebDriver driver; //for capture screenshot make it static other wise remove static
public WebDriver driver; 
public Logger logger;  //Log4j
public Properties p;
	
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setup(String os, String br) throws IOException
	{
		//Loading config.properties file
		FileReader file=new FileReader("./src//test//resources//config.properties");
		p=new Properties();
		p.load(file);
				
		logger=LogManager.getLogger(this.getClass());  //lOG4J2
				
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
			//os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WINDOWS);
			}
			else if(os.equalsIgnoreCase("linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
				
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
			
			//browser
			switch(br.toLowerCase())
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			case "firefox": capabilities.setBrowserName("firefox"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
		
				
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{

			switch(br.toLowerCase())
			{
			case "chrome" : driver=new ChromeDriver(); break;
			case "edge" : driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default : System.out.println("Invalid browser name.."); return;
			}
		}
		
			
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL")); // reading url from properties file.
		driver.manage().window().maximize();
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void tearDown()
	{
		driver.quit();
	}
	
	public String randomeString()
	{
		String generatedstring=RandomStringUtils.randomAlphabetic(5);
		return generatedstring;
	}
	
	public String randomeNumber()
	{
		String generatednumber=RandomStringUtils.randomNumeric(10);
		return generatednumber;
	}
	
	public String randomeAlphaNumberic()
	{
		String generatedstring=RandomStringUtils.randomAlphabetic(3);
		String generatednumber=RandomStringUtils.randomNumeric(3);
		return (generatedstring+"@"+generatednumber);
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
	
	
}*/
public class BaseClass {

    public WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = {"Sanity", "Regression", "Master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        // CI detection — also supports manual override via config
        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));
        boolean headless = isCI ||
                "true".equalsIgnoreCase(p.getProperty("headless", "false"));

        System.out.println(">>> OS: " + os);
        System.out.println(">>> Browser: " + br);
        System.out.println(">>> execution_env: " + p.getProperty("execution_env"));
        System.out.println(">>> isCI: " + isCI);
        System.out.println(">>> headless: " + headless);

        if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {

            DesiredCapabilities capabilities = new DesiredCapabilities();

            switch (os.toLowerCase()) {
                case "windows": capabilities.setPlatform(Platform.WINDOWS); break;
                case "linux":   capabilities.setPlatform(Platform.LINUX);   break;
                case "mac":     capabilities.setPlatform(Platform.MAC);     break;
                default:
                    System.out.println(">>> No matching OS: " + os); return;
            }

            switch (br.toLowerCase()) {
                case "chrome":  capabilities.setBrowserName("chrome");        break;
                case "edge":    capabilities.setBrowserName("MicrosoftEdge"); break;
                case "firefox": capabilities.setBrowserName("firefox");       break;
                default:
                    System.out.println(">>> No matching browser: " + br); return;
            }

            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);

        } else if (p.getProperty("execution_env").equalsIgnoreCase("local")) {

            switch (br.toLowerCase()) {

                case "chrome": {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                    if (headless) {
                        options.addArguments("--headless=new");
                        System.out.println(">>> Chrome running in HEADLESS mode");
                    }
                    driver = new ChromeDriver(options);
                    break;
                }

                case "edge": {
                    EdgeOptions options = new EdgeOptions();
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                    if (headless) {
                        options.addArguments("--headless=new");
                    }
                    driver = new EdgeDriver(options);
                    break;
                }

                case "firefox": {
                    FirefoxOptions options = new FirefoxOptions();
                    if (headless) {
                        options.addArguments("--headless");
                        options.addArguments("--width=1920");
                        options.addArguments("--height=1080");
                    }
                    driver = new FirefoxDriver(options);
                    break;
                }

                default:
                    System.out.println(">>> Invalid browser: " + br); return;
            }

        } else {
            throw new RuntimeException(
                ">>> Unknown execution_env: " + p.getProperty("execution_env")
                + " — must be 'local' or 'remote'"
            );
        }

        // Fail fast with clear message if driver not created
        if (driver == null) {
            throw new RuntimeException(
                ">>> Driver is NULL after setup! " +
                "execution_env=" + p.getProperty("execution_env") +
                ", browser=" + br
            );
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appURL"));

        if (!headless) {
            driver.manage().window().maximize();
        }

        System.out.println(">>> Driver ready: " + driver.getClass().getSimpleName());
    }

    @AfterClass(groups = {"Sanity", "Regression", "Master"})
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomeAlphaNumberic() {
        return RandomStringUtils.randomAlphabetic(3) + "@" + RandomStringUtils.randomNumeric(3);
    }
	public static String captureScreen(WebDriver driver, String testName) throws IOException {

    if (driver == null) {
        System.out.println(">>> captureScreen skipped — driver is null");
        return "";
    }

    String screenshotDir = System.getProperty(
            "screenshot.path",
            System.getProperty("user.dir") + "/screenshots");

    File dir = new File(screenshotDir);
    if (!dir.exists()) {
        dir.mkdirs();
    }

    TakesScreenshot ts = (TakesScreenshot) driver;
    File src = ts.getScreenshotAs(OutputType.FILE);

    String dest = screenshotDir + "/" + testName + "_"
            + System.currentTimeMillis() + ".png";

    FileUtils.copyFile(src, new File(dest));

    return dest;
}
}

 
