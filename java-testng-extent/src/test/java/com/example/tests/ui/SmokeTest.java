package com.example.tests.ui;

import com.example.base.BaseTest;
import com.example.pages.ExamplePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTest extends BaseTest {
    @Test
    public void exampleTitle() {
        Assert.assertTrue(driver.getTitle().contains("Example Domain"));
    }

    @Test
    public void exampleHeading() {
        ExamplePage page = new ExamplePage(driver);
        Assert.assertTrue(page.headingText().contains("Example"));
    }
}
