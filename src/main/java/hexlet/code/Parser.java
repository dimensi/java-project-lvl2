package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

enum ParserType {
    json,
    yml,
}

public class Parser {

    private final ObjectMapper mapper;

    public Parser(final ParserType type) {
        if (type.equals(ParserType.yml)) {
            mapper = new ObjectMapper(new YAMLFactory());
        } else {
            mapper = new ObjectMapper();
        }
    }

    public static Map<String, JsonNode> parse(final File file)
        throws IOException {
        var type = getType(file);
        var parser = new Parser(type);
        return parser.fileToMap(file);
    }

    private static ParserType getType(final File file) {
        var pattern = Pattern.compile("ya?ml$");
        if (pattern.matcher(file.getPath()).find()) {
            return ParserType.yml;
        }
        return ParserType.json;
    }

    private Map<String, JsonNode> fileToMap(final File file)
        throws IOException {
        Map<String, JsonNode> json = sortMap(
            mapper.readValue(file, new TypeReference<>() {
            })
        );

        return sortMap(json);
    }

    private Map<String, JsonNode> sortMap(final Map<String, JsonNode> map) {
        return new TreeMap<>(map);
    }
}
