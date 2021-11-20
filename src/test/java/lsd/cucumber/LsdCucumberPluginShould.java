package lsd.cucumber;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.readString;
import static java.nio.file.Paths.get;

class LsdCucumberPluginShould {

    @Test
    void generateMenu() throws IOException {
        Approvals.verifyHtml(readString(get("build/reports/lsd/lsd-index.html"), defaultCharset()));
    }

    @Test
    void generateReportForBasicScenario() throws IOException {
        Approvals.verifyHtml(readString(get("build/reports/lsd/Scenario.html"), defaultCharset()));
    }

    @Test
    void generateReportForScenarioWithDataTable() throws IOException {
        Approvals.verifyHtml(readString(get("build/reports/lsd/Data_table.html"), defaultCharset()));
    }

    @Test
    void generateForScenarioOutline() throws IOException {
        Approvals.verifyHtml(readString(get("build/reports/lsd/Scenario_outline.html"), defaultCharset()));
    }
}
