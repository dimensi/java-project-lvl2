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
            Objects.requireNonNull(
                getClass().getClassLoader().getResource(filepath)).getFile()
        );
    }

    private String getFileAsString(final String filepath) throws Exception {
        return FileUtils.readFileToString(
            getFile(filepath),
            Charset.defaultCharset()
        );
    }

    @Test
    public void testGenerateDiff() throws Exception {
        String[] cases = {"1", "2"};
        for (var idx : cases) {
            var file1 = getFile(String.format("differ/case%s/file1.json", idx));
            var file2 = getFile(String.format("differ/case%s/file2.json", idx));
            var result = Differ.generate(file1, file2);
            var expectResult =
                getFileAsString(String.format("differ/case%s/result.txt", idx));
            assertThat(result).isEqualTo(expectResult);
        }
    }
}
