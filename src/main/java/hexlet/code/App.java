package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "gendiff", description = "Compares two configuration files and shows a difference.", version = "gendiff 1.0.0", mixinStandardHelpOptions = true)
public class App implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Hello world");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}