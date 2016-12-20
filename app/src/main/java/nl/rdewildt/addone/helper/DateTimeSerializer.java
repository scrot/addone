package nl.rdewildt.addone.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Created by roydewildt on 18/09/16.
 */
public class DateTimeSerializer implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String jodaDateTime = json.getAsString();
        if(jodaDateTime.length() <= 0){
            return null;
        }
        else{
            return DateTime.parse(jodaDateTime);
        }
    }

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        String jodaDateTime;
        if(src == null){
            jodaDateTime = "";
        }
        else {
            jodaDateTime = src.toString();
        }
        return new JsonPrimitive(jodaDateTime);
    }
}
