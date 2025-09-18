package com.example.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite
    public void setupReport() throws IOException {
        Files.createDirectories(Path.of("reports"));
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Parameters({"browser", "headless", "baseUrl"})
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser,
                      @Optional("true") String headless,
                      @Optional("https://example.com") String baseUrl,
                      Method method) {
        boolean isHeadless = Boolean.parseBoolean(headless);
        if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions fo = new FirefoxOptions();
            if (isHeadless) fo.addArguments("-headless");
            driver = new FirefoxDriver(fo);
        } else {
            ChromeOptions co = new ChromeOptions();
            if (isHeadless) co.addArguments("--headless=new");
            co.addArguments("--window-size=1366,900", "--no-sandbox", "--disable-dev-shm-usage");
            driver = new ChromeDriver(co);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.get(baseUrl);

        ExtentTest t = extent.createTest(method.getName());
        test.set(t);
        t.info("Starting test on " + browser + (isHeadless ? " (headless)" : ""));
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = takeScreenshot(result.getName());
            test.get().fail("Failed").addScreenCaptureFromPath(path);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.get().pass("Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.get().skip("Skipped");
        }
        if (driver != null) driver.quit();
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) extent.flush();
    }

    protected String takeScreenshot(String name) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = "reports/" + name + "_" + System.currentTimeMillis() + ".png";
        FileUtils.copyFile(src, new File(filePath));
        return filePath;
    }
}
