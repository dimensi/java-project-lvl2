package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import hexlet.code.DiffEntry;
import hexlet.code.Formatter;

import java.io.IOException;
import java.util.List;

final class DiffEntrySerializer extends StdSerializer<DiffEntry> {
    DiffEntrySerializer() {
        this(null);
    }

    DiffEntrySerializer(final Class<DiffEntry> t) {
        super(t);
    }

    @Override
    public void serialize(final DiffEntry entry, final JsonGenerator gen,
                          final SerializerProvider provider)
        throws IOException {
        gen.writeStartObject();
        gen.writeStringField("key", entry.key());
        if (entry.isUpdate()) {
            gen.writeObjectFieldStart("value");
            gen.writeFieldName("old");
            gen.writeRawValue(entry.value().toString());
            gen.writeFieldName("new");
            gen.writeRawValue(entry.newValue().toString());
            gen.writeEndObject();
        } else {
            gen.writeFieldName("value");
            gen.writeRawValue(entry.value().toString());
        }
        gen.writeStringField("operation", entry.operation().toString());
        gen.writeEndObject();
    }


}

public final class JsonFormatter implements Formatter {

    @Override
    public String format(final List<DiffEntry> json) {
        try {
            var objectMapper = new ObjectMapper();
            SimpleModule module =
                new SimpleModule("DiffEntrySerializer",
                    new Version(1, 0, 0, null, null, null));
            module.addSerializer(DiffEntry.class, new DiffEntrySerializer());
            objectMapper.registerModule(module);
            return objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(json);
        } catch (Exception err) {
            return "";
        }
    }
}
