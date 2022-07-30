package hexlet.code;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void testGenerateDiffJson() throws Exception {
        String[] cases = {"1", "2", "3"};
        for (var idx : cases) {
            var filePath1 = String.format("differ/json/case%s/file1.json", idx);
            var filePath2 = String.format("differ/json/case%s/file2.json", idx);
            var resultPath =
                String.format("differ/json/case%s/result.txt", idx);
            var file1 = getFile(filePath1);
            var file2 = getFile(filePath2);
            var result = Differ.generate(file1, file2);
            var expectResult = getFileAsString(resultPath);
            assertThat(result).isEqualTo(expectResult);
        }
    }

    @Test
    public void testGenerateDiffYaml() throws Exception {
        String[] cases = {"1", "3"};
        for (var idx : cases) {
            var filePath1 = String.format("differ/yaml/case%s/file1.yml", idx);
            var filePath2 = String.format("differ/yaml/case%s/file2.yml", idx);
            var resultPath =
                String.format("differ/yaml/case%s/result.txt", idx);
            var file1 = getFile(filePath1);
            var file2 = getFile(filePath2);
            var result = Differ.generate(file1, file2);
            var expectResult = getFileAsString(resultPath);
            assertThat(result).isEqualTo(expectResult);
        }
    }

    @Test
    public void testGenerateDiffTypes() throws Exception {
        var filePath1 = "differ/json/case1/file1.json";
        var filePath2 = "differ/yaml/case1/file2.yml";
        var resultPath = "differ/yaml/case1/result.txt";
        var file1 = getFile(filePath1);
        var file2 = getFile(filePath2);
        var result = Differ.generate(file1, file2);
        var expectResult = getFileAsString(resultPath);
        assertThat(result).isEqualTo(expectResult);
    }

    @Test
    public void testGenerateDiffYamlExtension() throws Exception {
        var filePath1 = "differ/yaml/case2/file1.yml";
        var filePath2 = "differ/yaml/case2/file2.yaml";
        var resultPath = "differ/yaml/case2/result.txt";
        var file1 = getFile(filePath1);
        var file2 = getFile(filePath2);
        var result = Differ.generate(file1, file2);
        var expectResult = getFileAsString(resultPath);
        assertThat(result).isEqualTo(expectResult);
    }
}
