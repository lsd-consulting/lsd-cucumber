[![semantic-release](https://img.shields.io/badge/semantic-release-e10079.svg?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

# lsd-cucumber

Cucumber plugin for [lsd-core](https://github.com/lsd-consulting/lsd-core) (living sequence diagrams) reports.

Example project:

* [lsd-kotlin-cucumber-example](https://github.com/lsd-consulting/lsd-kotlin-cucumber-example)

[![CI](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/ci.yml/badge.svg)](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/ci.yml)
[![Nightly Build](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/nightly.yml/badge.svg)](https://github.com/lsd-consulting/lsd-cucumber/actions/workflows/nightly.yml)
[![GitHub release](https://img.shields.io/github/release/lsd-consulting/lsd-cucumber)](https://github.com/lsd-consulting/lsd-cucumber/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.lsd-consulting/lsd-cucumber.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.lsd-consulting%22%20AND%20a:%22lsd-cucumber%22)

## Properties

The following properties can be overridden by adding a properties file called `lsd.properties` on the classpath of your
application or by setting a System property. Note that System properties override file properties.

| Property Name             | Default | Description                                   |
|---------------------------|---------|-----------------------------------------------|
| lsd.cucumber.splitBySteps | false   | If set, splits the diagram by scenario steps. |
