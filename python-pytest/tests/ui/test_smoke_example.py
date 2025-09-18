import pytest
from src.pages.example_page import ExamplePage

@pytest.mark.ui
def test_example_title(driver, base_url):
    page = ExamplePage(driver, base_url).open()
    assert "Example Domain" in driver.title

@pytest.mark.ui
def test_example_heading(driver, base_url):
    page = ExamplePage(driver, base_url).open()
    assert "Example" in page.heading_text()
