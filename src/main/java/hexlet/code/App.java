package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
    name = "gendiff",
    description = "Compares two configuration files and shows a difference.",
    version = "gendiff 1.0.0",
    mixinStandardHelpOptions = true
)
public final class App implements Callable<Integer> {

    @Parameters(index = "0", description = "path to first file")
    private String filepath1;

    @Parameters(index = "1", description = "path to second file")
    private String filepath2;

    @Option(names = {"-f", "--format"}, description =
        "output format: ${COMPLETION-CANDIDATES}"
            + " [default: ${DEFAULT-VALUE}]")
    private FormatTypes format = FormatTypes.stylish;

    @Override
    public Integer call() {
        try {
            System.out.println(Differ.generate(filepath1, filepath2, format));
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        return 0;
    }

    public static void main(final String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
