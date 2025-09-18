from selenium.webdriver.common.by import By
from .base_page import BasePage

class ExamplePage(BasePage):
    HEADING = (By.CSS_SELECTOR, "h1")

    def heading_text(self):
        return self.text(self.HEADING)
