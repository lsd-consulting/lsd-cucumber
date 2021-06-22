package lsd.cucumber;

import com.lsd.LsdContext;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

import java.util.ArrayList;
import java.util.List;

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
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);

        publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedEvents::add);

        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String currentFeatureName = retrieveFeatureName(testCase);
        String currentScenarioName = testCase.getName();
        if (isVeryFirstRun()) {
            prepareFirstScenario(currentFeatureName, currentScenarioName);
        } else if (!continuationOfExistingScenario(currentScenarioName)) {
            finishProcessingCompletedScenario();
            finishProcessingCompletedFeature(currentFeatureName);
            prepareForNewScenario(currentScenarioName, currentFeatureName);
        } else {
            finishProcessingCompletedScenario();
            prepareForNewScenario(currentScenarioName, currentFeatureName);
        }
    }

    private String retrieveFeatureName(TestCase testCase) {
        return testCase.getUri().toString().replaceAll(".*/(.*?).feature$", "$1");
    }

    private void prepareFirstScenario(String currentFeatureName, String currentScenarioName) {
        scenarioName = currentScenarioName;
        featureName = currentFeatureName;
    }

    private void prepareForNewScenario(String currentScenarioName, String currentFeatureName) {
        testCaseFinishedEvents.clear();
        scenarioName = currentScenarioName;
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

    private void finishProcessingCompletedFeature(String currentFeatureName) {
        if (featureName != null && !currentFeatureName.equalsIgnoreCase(featureName)) {
            lsdContext.completeReport(featureName);
        }
    }

    private boolean continuationOfExistingScenario(String testScenarioName) {
        return testScenarioName.equals(scenarioName);
    }

    private boolean isVeryFirstRun() {
        return null == scenarioName;
    }

    private void handleTestRunFinished(TestRunFinished event) {
        finishProcessingCompletedScenario();
        lsdContext.completeReport(featureName);
        lsdContext.createIndex();
    }
}
