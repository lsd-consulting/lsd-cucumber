# lsd-cucumber

[![CI](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/ci.yml/badge.svg)](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/ci.yml)
[![Nightly Build](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/nightly.yml/badge.svg)](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/nightly.yml)
[![GitHub release](https://img.shields.io/github/release/lsd-consulting/lsd-cucumber)](https://github.com/lsd-consulting/lsd-cucumber/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.lsd-consulting/lsd-cucumber.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.lsd-consulting%22%20AND%20a:%22lsd-cucumber%22)

Cucumber plugin for [lsd-core](https://github.com/lsd-consulting/lsd-core) - automatically generates living sequence diagrams from your Cucumber scenarios.

## Quick Start

### Installation

Add the dependency to your project:

<details>
  <summary>Maven</summary>

```xml
<dependency>
    <groupId>io.github.lsd-consulting</groupId>
    <artifactId>lsd-cucumber</artifactId>
    <version>X.X.X</version>
    <scope>test</scope>
</dependency>
```

</details>

<details>
  <summary>Gradle</summary>

```groovy
testImplementation 'io.github.lsd-consulting:lsd-cucumber:X.X.X'
```
</details>

### Usage

Add the `LsdCucumberPlugin` to your Cucumber configuration:

<details open>
  <summary>Kotlin Example</summary>

```kotlin
@CucumberOptions(
    plugin = ["io.lsdconsulting.lsd.cucumber.LsdCucumberPlugin"],
    features = ["classpath:features"],
    glue = ["your.step.definitions"]
)
class RunCucumberTest
```
</details>

<details>
  <summary>Java Example</summary>

```java
@CucumberOptions(
    plugin = {"io.lsdconsulting.lsd.cucumber.LsdCucumberPlugin"},
    features = "classpath:features",
    glue = "your.step.definitions"
)
public class RunCucumberTest {
}
```
</details>

The plugin:
- Hooks into Cucumber lifecycle to generate reports
- Creates a new scenario for each Cucumber scenario
- Generates sequence diagrams showing captured interactions
- Marks scenarios as passed/failed based on scenario results
- Outputs reports to `build/reports/lsd/` (configurable via `lsd.core.report.outputDir`)

### Working with lsd-core directly

You can capture events manually within step definitions:

<details open>
  <summary>Kotlin Example</summary>

```kotlin
class OrderSteps {
    private val lsd = LsdContext.instance
    
    @When("the customer places an order")
    fun customerPlacesOrder() {
        lsd.capture(
            ("Customer" messages "OrderService") { label("POST /orders") }
        )
        // Your step implementation
    }
    
    @Then("the order is confirmed")
    fun orderIsConfirmed() {
        // Your assertions
        lsd.capture(
            ("OrderService" respondsTo "Customer") { label("201 Created") }
        )
    }
}
```
</details>

<details>
  <summary>Java Example</summary>

```java
public class OrderSteps {
    private final LsdContext lsd = LsdContext.getInstance();
    
    @When("the customer places an order")
    public void customerPlacesOrder() {
        lsd.capture(
            messageBuilder()
                .from("Customer")
                .to("OrderService")
                .label("POST /orders")
                .build()
        );
        // Your step implementation
    }
    
    @Then("the order is confirmed")
    public void orderIsConfirmed() {
        // Your assertions
        lsd.capture(
            messageBuilder()
                .from("OrderService")
                .to("Customer")
                .label("201 Created")
                .type(SYNCHRONOUS_RESPONSE)
                .build()
        );
    }
}
```
</details>

See [lsd-core](https://github.com/lsd-consulting/lsd-core) documentation for full API details.

## Example Project

For a complete working example, see [lsd-kotlin-cucumber-example](https://github.com/lsd-consulting/lsd-kotlin-cucumber-example).

## Configuration

Configure via `lsd.properties` on your classpath. See [lsd-core configuration](https://github.com/lsd-consulting/lsd-core#configuration) for available properties.

## Ecosystem

- **[lsd-core](https://github.com/lsd-consulting/lsd-core)** - Core library for generating living sequence diagrams
- **[lsd-junit-jupiter](https://github.com/lsd-consulting/lsd-junit-jupiter)** - JUnit Jupiter extension for LSD reports
- **[lsd-interceptors](https://github.com/lsd-consulting/lsd-interceptors)** - HTTP/messaging interceptors for automatic capture
