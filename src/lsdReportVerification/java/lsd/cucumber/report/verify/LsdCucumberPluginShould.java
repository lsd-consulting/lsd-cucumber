package lsd.cucumber.report.verify;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class LsdCucumberPluginShould {

    @Test
    void generateMenu() throws IOException {
        Approvals.verify(copyOfFile("build/reports/lsd/lsd-index.html"));
    }

    @Test
    void generateReportForBasicScenario() throws IOException {
        Approvals.verify(copyOfFile("build/reports/lsd/Scenario.html"));
    }

    @Test
    void generateReportForScenarioWithDataTable() throws IOException {
        Approvals.verify(copyOfFile("build/reports/lsd/Data_table.html"));
    }

    @Test
    void generateForScenarioOutline() throws IOException {
        Approvals.verify(copyOfFile("build/reports/lsd/Scenario_outline.html"));
    }

    private File copyOfFile(String path) throws IOException {
        Path original = get(path);
        Path copy = Path.of(original.getParent().toString(), "copy_" + original.getFileName());
        Files.copy(original, copy, REPLACE_EXISTING);
        return copy.toFile();
    }
}
