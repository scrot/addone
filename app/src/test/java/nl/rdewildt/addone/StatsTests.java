package nl.rdewildt.addone;

import android.content.Context;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatsTests {
    @Mock
    private Context mMockContext;

    @Test
    public void statsInit() {
        Stats stats = new Stats();
        assertThat(stats.getCounter(), is(0));
        assertThat(stats.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear() - 1));
    }

    @Test
    public void statsEqual() {
        DateTime now = new DateTime();

        Stats a1 = new Stats();
        a1.setLastUpdated(now);
        Stats a2 = new Stats();
        a2.setLastUpdated(now);
        Stats b1 = new Stats();
        Stats b2 = new Stats();
        b2.setLastUpdated(now);
        b2.setCounter(1);

        assertThat(a1, is(a2));
        assertThat(a2, is(a1));
        assertThat(a1, not(b1));
        assertThat(a1, not(b2));

    }

    @Test
    public void ReadWriteStats() throws IOException, JSONException {
        File temp = File.createTempFile("stats", ".json");
        when(mMockContext.openFileOutput("stats.json", Context.MODE_PRIVATE))
                .thenReturn(new FileOutputStream(temp));

        StatsMaintainer statsMaintainer = new StatsMaintainer(temp);
        statsMaintainer.increaseCounter(10);
        Stats.writeStats(statsMaintainer.getStats(), temp);

        StatsMaintainer statsMaintainerFromFile = new StatsMaintainer(temp);

        assertThat(statsMaintainer.getStats(), is(statsMaintainerFromFile.getStats()));

    }
}