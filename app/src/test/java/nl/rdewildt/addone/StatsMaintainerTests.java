package nl.rdewildt.addone;

import android.content.Context;

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
 * Created by roydewildt on 16/09/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatsMaintainerTests {
    @Mock
    private Context mMockContext;

    @Test
    public void ReadWriteStats() throws IOException, JSONException {
        File temp = File.createTempFile("stats", ".json");
        when(mMockContext.openFileOutput("stats.json", Context.MODE_PRIVATE))
                .thenReturn(new FileOutputStream(temp));

        StatsMaintainer statsMaintainer = new StatsMaintainer();
        statsMaintainer.increaseCounter(10);
        statsMaintainer.writeStats(mMockContext);

        StatsMaintainer statsMaintainerFromFile = new StatsMaintainer(temp);

        assertThat(statsMaintainer.getStats(), is(statsMaintainerFromFile.getStats()));

    }
}
