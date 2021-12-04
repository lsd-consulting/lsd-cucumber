package lsd.cucumber;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class LsdReportsShould {

    private static final String REPORT_DIRECTORY = System.getProperty("user.dir") + "/build/reports/lsd";
    private static final Playwright PLAYWRIGHT = Playwright.create();
    private static final Browser BROWSER = PLAYWRIGHT.chromium().launch();

    // New instance for each test method.
    private BrowserContext context = BROWSER.newContext();
    private Page page = context.newPage();

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        PLAYWRIGHT.close();
    }

    @Test
    public void generateScenarioPage() {
        page.navigate("file:///" + REPORT_DIRECTORY + "/Scenario.html");
        captureScreenshot("scenario.png");

        assertThat(page.title()).isEqualTo("scenario");
    }

    @Test
    public void generateScenarioOutlinePage() {
        page.navigate("file:///" + REPORT_DIRECTORY + "/Scenario_outline.html");
        captureScreenshot("scenario_outline.png");

        assertThat(page.title()).isEqualTo("scenario_outline");
    }

    @Test
    public void generateDataTablePage() {
        page.navigate("file:///" + REPORT_DIRECTORY + "/Data_table.html");
        captureScreenshot("data_table.png");

        assertThat(page.title()).isEqualTo("data_table");
    }

    private void captureScreenshot(String fileName) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(REPORT_DIRECTORY + "/screenshots", fileName)));
    }
}
