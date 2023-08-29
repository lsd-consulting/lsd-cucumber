package lsd.cucumber

import com.lsd.core.LsdContext
import com.lsd.core.ReportOptions
import com.lsd.core.domain.Newpage
import com.lsd.core.domain.PageTitle
import com.lsd.core.domain.Status.ERROR
import com.lsd.core.domain.Status.SUCCESS
import com.lsd.core.properties.LsdProperties.getBoolean
import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.*
import io.cucumber.plugin.event.Status.FAILED
import java.util.concurrent.ConcurrentHashMap

class LsdCucumberPlugin : ConcurrentEventListener {
    private val lsdContext: LsdContext = LsdContext.instance
    private val testCaseFinishedEvents: MutableList<TestCaseFinished> = ArrayList()
    private val options = ReportOptions()
    private val outlineScenarioNames: MutableMap<String, Int> = ConcurrentHashMap()

    private var scenarioName: String? = null
    private var featureName: String? = null
    private var splitByStep: Boolean = false

    override fun setEventPublisher(publisher: EventPublisher) {
        splitByStep = getBoolean("lsd.cucumber.splitBySteps")
        publisher.registerHandlerFor(TestCaseStarted::class.java, ::handleTestCaseStarted)
        publisher.registerHandlerFor(TestStepStarted::class.java, ::handleTestStepStarted)
        publisher.registerHandlerFor(TestCaseFinished::class.java, testCaseFinishedEvents::add)
        publisher.registerHandlerFor(TestRunFinished::class.java, ::handleTestRunFinished)
    }

    private fun handleTestStepStarted(testStepStarted: TestStepStarted) {
        val testStep = testStepStarted.testStep
        if (splitByStep && testStep is PickleStepTestStep) {
            val step = testStep.step
            lsdContext.capture(Newpage(PageTitle(step.keyword + step.text)))
        }
    }

    private fun handleTestCaseStarted(event: TestCaseStarted) {
        val testCase = event.testCase
        val currentFeatureName = testCase.featureName()
        val currentScenarioName = testCase.scenarioName()
        if (isVeryFirstRun) {
            prepareFirstScenario(currentFeatureName, currentScenarioName)
        } else if (!continuationOfExistingScenario(currentScenarioName)) {
            finishProcessingCompletedScenario()
            finishProcessingCompletedFeature(currentFeatureName)
            prepareForNewScenario(currentScenarioName, currentFeatureName)
        } else {
            finishProcessingCompletedScenario()
            prepareForNewScenario(currentScenarioName, currentFeatureName)
        }
    }

    private fun TestCase.scenarioName(): String {
        if (keyword.equals("Scenario Outline", ignoreCase = true)) {
            var index = outlineScenarioNames[name] ?: 0
            outlineScenarioNames[name] = ++index
            return "$name #$index"
        }
        return name
    }

    private fun TestCase.featureName(): String =
        uri.toString().replace(".*/(.*?).feature$".toRegex(), "$1")

    private fun prepareFirstScenario(currentFeatureName: String, currentScenarioName: String) {
        scenarioName = currentScenarioName
        featureName = currentFeatureName
    }

    private fun prepareForNewScenario(currentScenarioName: String, currentFeatureName: String) {
        testCaseFinishedEvents.clear()
        scenarioName = currentScenarioName
        featureName = currentFeatureName
    }

    private fun finishProcessingCompletedScenario() {
        val description = testCaseFinishedEvents
            .flatMap { it.testCase.testSteps }
            .filterIsInstance<PickleStepTestStep>()
            .joinToString(separator = "</br>") { "${it.step.keyword} ${it.step.text}" }

        val overallStatus =
            if (testCaseFinishedEvents.any { it.result.status == FAILED }) ERROR else SUCCESS

        lsdContext.completeScenario(scenarioName!!, description, overallStatus)
    }

    private fun finishProcessingCompletedFeature(currentFeatureName: String) {
        if (featureName != null && !currentFeatureName.equals(featureName, ignoreCase = true)) {
            lsdContext.completeReport(featureName!!)
        }
    }

    private fun continuationOfExistingScenario(testScenarioName: String): Boolean =
        testScenarioName == scenarioName

    private val isVeryFirstRun: Boolean
        get() = scenarioName == null

    private fun handleTestRunFinished(event: TestRunFinished) {
        featureName?.let { name ->
            if (name.isNotBlank()) {
                finishProcessingCompletedScenario()
                lsdContext.completeReport(name)
                lsdContext.createIndex(options.devMode)
                lsdContext.completeComponentsReport("Combined Component Diagram")
            }
        }
    }
}
