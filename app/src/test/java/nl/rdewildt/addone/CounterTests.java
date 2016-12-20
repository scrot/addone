package nl.rdewildt.addone;

import android.content.Context;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class CounterTests {
    @Mock
    private Context mMockContext;

    @Test
    public void counterInit() {
        Counter counter = new Counter();
        assertThat(counter.getValue(), is(0));
        assertThat(counter.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear() - 1));
    }

    @Test
    public void counterEqual() {
        DateTime now = new DateTime();

        Counter a1 = new Counter();
        a1.setLastUpdated(now);
        Counter a2 = new Counter();
        a2.setLastUpdated(now);
        Counter b1 = new Counter();
        Counter b2 = new Counter();
        b2.setLastUpdated(now);
        b2.setValue(1);

        assertThat(a1, is(a2));
        assertThat(a2, is(a1));
        assertThat(a1, not(b1));
        assertThat(a1, not(b2));

    }

    @Test
    public void readWriteCounter() throws IOException, JSONException {
        File temp = new File(System.getProperty("java.io.tmpdir"));

        StatsController statsController = new StatsController(temp);
        statsController.getCounter().setValue(10);
        Counter.writeCounter(statsController.getCounter(), temp);

        StatsController statsControllerFromFile = new StatsController(temp);

        assertThat(statsController.getCounter(), is(statsControllerFromFile.getCounter()));

    }

    /*
    @Test
    public void validateCounter() throws IOException, JSONException {
        // valid counter file
        File temp = new File(System.getProperty("java.io.tmpdir"));
        Counter.writeStats(new Counter(), temp);
        assertThat(Counter.IsValidCounterFile(temp), is(true));

        // Invalid counter file
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(temp))){
            fileWriter.write("{\"counter\":0}");
        }
        assertThat(Counter.IsValidCounterFile(temp), is(false));
    }
    */
}