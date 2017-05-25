package com.fincance.dataprocess;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by Balint Csikos on 5/22/2017.
 */
public class Entrypoint {
    private static final int DATE = 0;
    private static final int CLOSE = 4;
    private static FileWriter fw;
    private static boolean first = true;

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
        }
        long start = System.currentTimeMillis();
        try {
            fw = new FileWriter("out.json");
            ObjectMapper mapper = new ObjectMapper();
            Entrypoint.fw.write("[");
            try (Stream<String> stream = Files.lines(Paths.get(args[0]))
            ) {
                stream.filter(line -> {
                    return !line.startsWith("Date,Open,High,Low,Close,Adj Close,Volume");
                }).forEach(line -> {
                    try {
                        if (!first) {
                            Entrypoint.fw.write(",");
                        } else {
                            first = false;
                        }
                    /*
                    new Data(
                                    line.split(",")[DATE],
                                    line.split(",")[CLOSE]
                     */
                        Entrypoint.fw.write(mapper.writeValueAsString(
                                "{ \"date\": " + line.split(",")[DATE] + "}, { \"close\": " + line.split(",")[CLOSE] + "}")

                        );
                    } catch (IOException ioe) {
                        // Handle read error
                        System.out.println(ioe.getMessage());
                        ioe.printStackTrace();
                    }
                });
            }
            Entrypoint.fw.write("]");
            Entrypoint.fw.close();
        } catch (IOException ioe) {
            // Handel write error
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start + " ms");
    }

    private static void printHelp() {
        System.out.println("<cmd> \"<input.csv>\"");
        System.exit(-1);
    }


}
