package nl.rdewildt.addone;

import android.content.Context;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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
        assertThat(counter.getCounter(), is(0));
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
        b2.setCounter(1);

        assertThat(a1, is(a2));
        assertThat(a2, is(a1));
        assertThat(a1, not(b1));
        assertThat(a1, not(b2));

    }

    @Test
    public void readWriteCounter() throws IOException, JSONException {
        File temp = File.createTempFile("counter", ".json");
        when(mMockContext.openFileOutput("counter.json", Context.MODE_PRIVATE))
                .thenReturn(new FileOutputStream(temp));

        CounterMaintainer counterMaintainer = new CounterMaintainer(temp);
        counterMaintainer.increaseCounter(10);
        Counter.writeCounter(counterMaintainer.getCounter(), temp);

        CounterMaintainer counterMaintainerFromFile = new CounterMaintainer(temp);

        assertThat(counterMaintainer.getCounter(), is(counterMaintainerFromFile.getCounter()));

    }

    @Test
    public void validateCounter() throws IOException, JSONException {
        // valid counter file
        File temp = File.createTempFile("counter", ".json");
        Counter.writeCounter(new Counter(),temp);
        assertThat(Counter.IsValidCounterFile(temp), is(true));

        // Invalid counter file
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(temp))){
            fileWriter.write("{\"counter\":0}");
        }
        assertThat(Counter.IsValidCounterFile(temp), is(false));
    }
}