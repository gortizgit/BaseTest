import os
import pathlib
import time
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options as ChromeOptions
from selenium.webdriver.firefox.options import Options as FirefoxOptions


def pytest_addoption(parser):
    parser.addoption("--browser", action="store", default="chrome", help="chrome|firefox")
    parser.addoption("--headless", action="store_true", help="run headless")
    parser.addoption("--base-url", action="store", default=os.getenv("BASE_URL", "https://example.com"))


@pytest.fixture(scope="session")
def base_url(pytestconfig):
    return pytestconfig.getoption("--base-url")


@pytest.fixture(scope="session")
def driver(pytestconfig):
    browser = pytestconfig.getoption("--browser").lower()
    headless = pytestconfig.getoption("--headless")

    if browser == "firefox":
        opts = FirefoxOptions()
        if headless:
            opts.add_argument("-headless")
        drv = webdriver.Firefox(options=opts)
    else:
        opts = ChromeOptions()
        if headless:
            opts.add_argument("--headless=new")
        opts.add_argument("--window-size=1366,900")
        opts.add_argument("--no-sandbox")
        opts.add_argument("--disable-dev-shm-usage")
        drv = webdriver.Chrome(options=opts)

    yield drv
    drv.quit()


# Screenshot on failure + attach to pytest-html
@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    rep = outcome.get_result()
    if rep.when == "call" and rep.failed and "driver" in item.fixturenames:
        drv = item.funcargs["driver"]
        reports_dir = pathlib.Path("reports")
        reports_dir.mkdir(exist_ok=True, parents=True)
        png_path = reports_dir / f"{item.name}_{int(time.time())}.png"
        try:
            drv.save_screenshot(str(png_path))
            if hasattr(rep, "extra"):
                from pytest_html import extras
                rep.extra.append(extras.png(str(png_path), mime_type="image/png"))
        except Exception:
            pass
