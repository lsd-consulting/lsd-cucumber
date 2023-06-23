package lsd.cucumber.example.steps;

import com.lsd.core.LsdContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import static com.lsd.core.builders.MessageBuilder.messageBuilder;

public class ReportGeneratorSteps implements En {

    private final LsdContext lsdContext = LsdContext.getInstance();

    public ReportGeneratorSteps() {

        Given("^given1$", () -> {
            lsdContext.capture(messageBuilder().from("source").to("destination").id("id1").label("label1").build());
            lsdContext.capture(messageBuilder().from("destination").to("source").id("id2").label("label2").build());
        });
        When("^when1$", () -> {
        });
        Then("^then1$", () -> {
        });

        Given("^given2$", (DataTable param) -> {
            lsdContext.capture(messageBuilder().from("source").to("destination").id("id1").label("with params").data(param).build());
            lsdContext.capture(messageBuilder().from("destination").to("source").id("id2").label("ok").build());
        });
        When("^when2$", () -> {
            lsdContext.capture(messageBuilder().from("source").to("destination").id("id3").label("label3").build());
            lsdContext.capture(messageBuilder().from("destination").to("source").id("id4").label("label4").build());
        });
        Then("^then2$", () -> {
        });

        Given("^the following values for (.*?) and (.*?)$", (String column1, String column2) -> {
            lsdContext.capture(messageBuilder().from("source").to("destination").id("id1").label("label1").build());
            lsdContext.capture(messageBuilder().from("destination").to("source").id("id2").label("label2").build());
        });
        When("^when3$", () -> {
        });
        Then("^then3$", () -> {
        });
    }
}
