package hexlet.code;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

enum Extension {
    yaml,
    json;

    @Override
    public String toString() {
        return name();
    }
    public String toExt() {
        return  switch (this) {
            case json -> "json";
            case yaml -> "yml";
        };
    }
}

record TestCase(String index, FormatTypes type, Extension extension) {
}

class DifferTest {

    private File getFile(final String filepath) {
        return new File(
            Objects
                .requireNonNull(
                    getClass().getClassLoader().getResource(filepath))
                .getFile()
        );
    }

    private String getFileAsString(final String filepath) throws Exception {
        return FileUtils.readFileToString(
            getFile(filepath),
            Charset.defaultCharset()
        );
    }

    static final int TEST_CASE_SIZES = 3;
    static Stream<TestCase> testCaseStream() {
        List<TestCase> testCases = new ArrayList<>();
        for (var i = 1; i <= TEST_CASE_SIZES; i++) {
            for (var format : FormatTypes.values()) {
                for (var extension : Extension.values()) {
                    testCases.add(
                        new TestCase(String.valueOf(i), format, extension));
                }
            }

        }
        return testCases.stream();
    }

    @ParameterizedTest
    @MethodSource("testCaseStream")
    public void testGenerateDiff(final TestCase testCase) throws Exception {
        var idx = testCase.index();
        var type = testCase.type();
        var extDir = testCase.extension().toString();
        var ext = testCase.extension().toExt();
        var typeDir = type.toString();
        var filePath1 = String.format(
            "differ/%s/%s/case%s/file1.%s", typeDir, extDir, idx, ext);
        var filePath2 = String.format(
            "differ/%s/%s/case%s/file2.%s", typeDir, extDir, idx, ext);
        var resultPath =
            String.format("differ/%s/%s/case%s/result.txt",
                typeDir,
                extDir,
                idx);
        var file1 = getFile(filePath1);
        var file2 = getFile(filePath2);
        var result = Differ.generate(file1, file2, type);
        var expectResult = getFileAsString(resultPath);
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest
    @EnumSource
    public void testGenerateDiffTypes(final FormatTypes formatType)
        throws Exception {
        var filePath1 = String.format("differ/%s/json/case1/file1.json",
            formatType);
        var filePath2 = String.format("differ/%s/yaml/case1/file2.yml",
            formatType);
        var resultPath = String.format("differ/%s/yaml/case1/result.txt",
            formatType);
        var file1 = getFile(filePath1);
        var file2 = getFile(filePath2);
        var result = Differ.generate(file1, file2);
        var expectResult = getFileAsString(resultPath);
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest
    @EnumSource
    public void testGenerateDiffYamlExtension(final FormatTypes formatType)
        throws Exception {
        var filePath1 = String.format("differ/%s/yaml/case4/file1.yml",
            formatType);
        var filePath2 = String.format("differ/%s/yaml/case4/file2.yml",
            formatType);
        var resultPath = String.format("differ/%s/yaml/case4/result.txt",
            formatType);
        var file1 = getFile(filePath1);
        var file2 = getFile(filePath2);
        var result = Differ.generate(file1, file2);
        var expectResult = getFileAsString(resultPath);
        assertThat(result).isEqualTo(expectResult);
    }
}
