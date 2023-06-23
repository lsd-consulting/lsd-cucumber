package lsd.cucumber;

import com.lsd.core.LsdContext;
import com.lsd.core.domain.Newpage;
import com.lsd.core.domain.PageTitle;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static io.cucumber.plugin.event.Status.FAILED;
import static java.util.stream.Collectors.joining;

public class LsdCucumberPlugin implements ConcurrentEventListener {

    private final LsdContext lsdContext = LsdContext.getInstance();

    private final List<TestCaseFinished> testCaseFinishedEvents = new ArrayList<>();
    private String scenarioName;
    private String featureName;

    private final Map<String, Integer> outlineScenarioNames = new ConcurrentHashMap<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedEvents::add);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestStepStarted(TestStepStarted testStepStarted) {
        var testStep = testStepStarted.getTestStep();
        if (testStep instanceof PickleStepTestStep) {
            var pickleTestStep = (PickleStepTestStep) testStep;
            var step = pickleTestStep.getStep();
            var stepText = step.getText();
            var stepKeyword = step.getKeyword();
            lsdContext.capture(new Newpage(new PageTitle(stepKeyword + stepText)));
        }
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String currentFeatureName = retrieveFeatureName(testCase);
        String currentScenarioName = getScenarioName(testCase);
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

    private String getScenarioName(TestCase testCase) {
        if (testCase.getKeyword().equalsIgnoreCase("Scenario Outline")) {
            Integer index = Optional.ofNullable(outlineScenarioNames.get(testCase.getName())).orElse(0);
            outlineScenarioNames.put(testCase.getName(), ++index);
            return testCase.getName() + " #" + index;
        }
        return testCase.getName();
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
                .map(x -> com.lsd.core.domain.Status.ERROR)
                .orElse(com.lsd.core.domain.Status.SUCCESS);

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
        if (!StringUtils.isBlank(featureName)) {
            finishProcessingCompletedScenario();
            lsdContext.completeReport(featureName);
            lsdContext.createIndex();
            lsdContext.completeComponentsReport("Combined Component Diagram");
        }
    }
}
