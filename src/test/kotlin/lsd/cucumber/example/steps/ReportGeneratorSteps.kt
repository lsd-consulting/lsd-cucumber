package lsd.cucumber.example.steps

import com.lsd.core.LsdContext
import com.lsd.core.builders.MessageBuilder.Companion.messageBuilder
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En

@Suppress("unused")
class ReportGeneratorSteps : En {
    private val lsdContext: LsdContext = LsdContext.instance

    init {
        Given("^given1$") {
            lsdContext.capture(
                messageBuilder().from("source").to("destination").id("id1").label("label1").build(),
                messageBuilder().from("destination").to("source").id("id2").label("label2").build()
            )
        }
        When("^when1$") {}
        Then("^then1$") {}
        Given("^given2$") { param: DataTable ->
            lsdContext.capture(
                messageBuilder().from("source").to("destination").id("id1").label("with params").data(param).build(),
                messageBuilder().from("destination").to("source").id("id2").label("ok").build()
            )
        }
        When("^when2$") {
            lsdContext.capture(
                messageBuilder().from("source").to("destination").id("id3").label("label3").build(),
                messageBuilder().from("destination").to("source").id("id4").label("label4").build()
            )
        }
        Then("^then2$") {}
        Given("^the following values for (.*?) and (.*?)$") { _: String, _: String ->
            lsdContext.capture(
                messageBuilder().from("source").to("destination").id("id1").label("label1").build(),
                messageBuilder().from("destination").to("source").id("id2").label("label2").build()
            )
        }
        When("^when3$") {}
        Then("^then3$") {}
    }
}