package lsd.cucumber.example.steps;

import com.lsd.LsdContext;
import com.lsd.events.Message;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

public class ReportGeneratorSteps implements En {

    private final LsdContext lsdContext = LsdContext.getInstance();

    public ReportGeneratorSteps() {

        Given("^given1$", () -> {
            lsdContext.capture(Message.builder().from("source").to("destination").id("id1").label("label1").build());
            lsdContext.capture(Message.builder().from("destination").to("source").id("id2").label("label2").build());
        });
        When("^when1$", () -> {
        });
        Then("^then1$", () -> {
        });

        Given("^given2$", (DataTable param1) -> {
            lsdContext.capture(Message.builder().from("source").to("destination").id("id1").label("label1").build());
            lsdContext.capture(Message.builder().from("destination").to("source").id("id2").label("label2").build());
        });
        When("^when2$", () -> {
        });
        Then("^then2$", () -> {
        });

        Given("^the following values for (.*?) and (.*?)$", (String column1, String column2) -> {
            lsdContext.capture(Message.builder().from("source").to("destination").id("id1").label("label1").build());
            lsdContext.capture(Message.builder().from("destination").to("source").id("id2").label("label2").build());
        });
        When("^when3$", () -> {
        });
        Then("^then3$", () -> {
        });
    }
}
