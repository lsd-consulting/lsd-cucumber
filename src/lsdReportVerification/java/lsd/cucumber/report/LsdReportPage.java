package lsd.cucumber.report;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.nio.file.Paths;
import java.util.List;

/**
 * Loads an LSD report from the lsd reports directory for use in a test class.
 * <p>
 * Hides away the Playwright details and make it easier to reuse common page actions.
 * <p>
 */
public class LsdReportPage {
    private final String lsdReportsDir = "file:///" + System.getProperty("user.dir") + "/build/reports/lsd/";
    private final Playwright playwright = Playwright.create();
    private final BrowserContext browserContext = playwright.chromium().launch().newContext();
    private final Page page = browserContext.newPage();

    public LsdReportPage(String reportName) {
        page.navigate(lsdReportsDir + reportName);
        captureScreenshot(reportName.replaceAll(".html", ".png"));
    }

    public String title() {
        return page.title();
    }

    public List<String> articleTitles() {
        return page.locator("main > article > h2").allTextContents();
    }

    private void captureScreenshot(String fileName) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(lsdReportsDir + "screenshots", fileName)));
    }

    public void close() {
        playwright.close();
    }
}
