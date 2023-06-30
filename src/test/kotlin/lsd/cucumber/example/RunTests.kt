package lsd.cucumber.example

import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages(
    "lsd.cucumber.example",
)
class RunTests
