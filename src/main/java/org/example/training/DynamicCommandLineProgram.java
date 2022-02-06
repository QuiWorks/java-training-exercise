package org.example.training;

import org.apache.commons.cli.*;
import org.example.training.program.Instruction;
import org.example.training.program.Procedure;

import java.util.Arrays;

/**
 * A dynamic implementation of {@link CommandLineProgram}
 * where program arguments and instructions are provided through the command line.
 * The `-i` flag is used to set instructions.
 */
public class DynamicCommandLineProgram {

    public static void main(String[] args) throws ParseException {
        final CommandLine commandLine = parseArguments(args);
        final String[] instructions = getInstructions(commandLine);
        new CommandLineProgram<>(String.class){}
                .run(commandLine.getArgs(), Arrays.stream(instructions)
                        .map(instruction -> Instruction.get(instruction, String.class))
                        .toArray(Procedure[]::new));
    }

    private static String[] getInstructions(CommandLine commandLine) {
        return commandLine.hasOption('i') ? commandLine.getOptionValue('i').split(",") : new String[0];
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        return DefaultParser.builder()
                .build()
                .parse(new Options().addOption(new Option("i", true, "Instructions")), args);
    }

}
