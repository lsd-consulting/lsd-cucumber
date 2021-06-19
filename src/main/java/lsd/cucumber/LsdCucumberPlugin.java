package lsd.cucumber;

import com.lsd.LsdContext;
import com.lsd.OutcomeStatus;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lsd.OutcomeStatus.ERROR;
import static com.lsd.OutcomeStatus.SUCCESS;
import static io.cucumber.plugin.event.Status.FAILED;
import static java.util.stream.Collectors.joining;

public class LsdCucumberPlugin implements EventListener {

    private final LsdContext lsdContext = LsdContext.getInstance();

    private final List<TestCaseFinished> testCaseFinishedEvents = new ArrayList<>();
    private String scenarioName;
    private String featureName;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class,
                this::handleTestCaseStarted);

        publisher.registerHandlerFor(TestCaseFinished.class, e -> {
            System.out.println("Finished featureName=" + featureName + ", currentTestScenarioName=" + scenarioName);
            testCaseFinishedEvents.add(e);
        });

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            finishProcessingCompletedScenario();
            lsdContext.completeReport(featureName);
            lsdContext.createIndex();
        });
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String currentFeatureName = testCase.getUri().toString().replaceAll(".*/(.*?).feature$", "$1");
        String currentScenarioName = testCase.getName();
        if (isVeryFirstRun()) {
            scenarioName = currentScenarioName;
            featureName = currentFeatureName;
        } else if (!continuationOfExistingScenario(currentScenarioName)) {
            finishProcessingCompletedScenario();
            if (featureName != null && !currentFeatureName.equalsIgnoreCase(featureName)) {
                lsdContext.completeReport(featureName);
            }
            prepareForNewScenario(currentScenarioName, currentFeatureName);
        } else {
            // TODO This doesn't seem to ever happen
        }
    }

    private void prepareForNewScenario(String testScenarioName, String currentFeatureName) {
        testCaseFinishedEvents.clear();
        scenarioName = testScenarioName;
        featureName = currentFeatureName;
    }

    private void finishProcessingCompletedScenario() {
        var description = testCaseFinishedEvents.stream()
                .flatMap(event -> event.getTestCase().getTestSteps().stream())
                .filter(PickleStepTestStep.class::isInstance)
                .map(PickleStepTestStep.class::cast)
                .map(v -> v.getStep().getKeyword() + " " + v.getStep().getText())
                .collect(joining("</br>"));


        var result = testCaseFinishedEvents.stream()
                .map(TestCaseFinished::getResult)
                .filter(testResult -> testResult.getStatus().is(FAILED))
                .findFirst()
                .map(x -> ERROR)
                .orElse(SUCCESS);

        lsdContext.completeScenario(scenarioName, description, result);
    }

    private boolean continuationOfExistingScenario(String testScenarioName) {
        return testScenarioName.equals(scenarioName);
    }

    private boolean isVeryFirstRun() {
        return null == scenarioName;
    }
}
