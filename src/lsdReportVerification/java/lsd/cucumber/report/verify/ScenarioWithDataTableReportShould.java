package lsd.cucumber.report.verify;

import lsd.cucumber.report.LsdReportPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenarioWithDataTableReportShould {
    private static final LsdReportPage LSD_REPORT_PAGE = new LsdReportPage("Data_table.html");

    @AfterAll
    static void cleanup() {
        LSD_REPORT_PAGE.close();
    }

    @Test
    public void containAPageTitle() {
        assertThat(LSD_REPORT_PAGE.title()).isEqualTo("data_table");
    }

    @Test
    public void containAScenarioTitle() {
        assertThat(LSD_REPORT_PAGE.articleTitles())
                .hasSize(1)
                .containsExactly(
                        "Scenario with a data table"
                );
    }
}
