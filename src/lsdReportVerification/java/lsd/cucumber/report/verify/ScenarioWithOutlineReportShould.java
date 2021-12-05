package lsd.cucumber.report.verify;

import lsd.cucumber.report.LsdReportPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenarioWithOutlineReportShould {
    private static final LsdReportPage LSD_REPORT_PAGE = new LsdReportPage("Scenario_outline.html");

    @AfterAll
    static void cleanup() {
        LSD_REPORT_PAGE.close();
    }

    @Test
    public void containAPageTitle() {
        assertThat(LSD_REPORT_PAGE.title()).isEqualTo("scenario_outline");
    }

    @Test
    public void containAScenarioTitleForEachExampleRow() {
        assertThat(LSD_REPORT_PAGE.articleTitles())
                .hasSize(2)
                .contains(
                        "Scenario with a data table #1",
                        "Scenario with a data table #2"
                );
    }
}
