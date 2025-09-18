package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ExamplePage {
    private final WebDriver driver;
    private final By HEADING = By.cssSelector("h1");

    public ExamplePage(WebDriver driver) { this.driver = driver; }

    public String headingText() { return driver.findElement(HEADING).getText(); }
}
