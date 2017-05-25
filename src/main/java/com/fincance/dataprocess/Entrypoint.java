package com.fincance.dataprocess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Balint Csikos on 5/22/2017.
 */
public class Entrypoint {
    private static final int ITERATION = 10000;
    private static final int PARALLELISM = 100;
    private static final int DATE = 0;
    private static final int CLOSE = 4;
    // last close compared to actual close percentage
    private static double lastClose = -1; //impossible close according to input data
    private static FileWriter fw;
    private static boolean first = true;
    private static List<Double> last240Close = new ArrayList<>();
    private static String lastDate = "1970-01";

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
        }
        long start = System.currentTimeMillis();
        Map<String, String> rawData = new LinkedHashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(args[0]))) {
            stream.filter(line -> {
                // filter header
                return !line.startsWith("Date,Open,High,Low,Close,Adj Close,Volume");
            }).forEach(line -> {
                        // load raw data into memory for sorting (cpu optimized)
                        String[] row = line.split(",");
                        String[] date = row[DATE].split("-");
                        // store only the last day of month (the read goes line by line and the data is ordered by date)
                        rawData.put(date[0] + "-" + date[1], row[DATE] + "," + row[CLOSE]);
                        Entrypoint.lastDate = date[0] + "-" + date[1];
                    }
            );

        } catch (IOException ioe) {
            // Handel read error
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
            System.exit(2);
        }

        try {
            fw = new FileWriter("past.json");
            Entrypoint.fw.write("[");
            Stream<Map.Entry<String, String>> stream = rawData.entrySet().stream();

            // create past data and write it
            stream.forEach(d -> {
                try {
                    String line = d.getValue();
                    if (!first) {
                        Entrypoint.fw.write(",");
                    } else {
                        first = false;
                    }
                    double close = Double.valueOf(line.split(",")[1]);
                    Entrypoint.fw.write(
                            "{\"date\": \"" + d.getKey() + "\"," +
                                    "\"close\": \"" + close + "\"," +
                                    "\"change\": \"" + Entrypoint.calcChange(Entrypoint.lastClose, close) + "\"}"
                    );
                    Entrypoint.last240Close.add(close);
                    Entrypoint.lastClose = close;
                } catch (IOException ioe) {
                    // Handle io error
                    System.out.println("Cannot read/write! Exit!");
                    ioe.printStackTrace();
                    System.exit(3);
                }
            });
            Entrypoint.fw.write("]");
            Entrypoint.fw.close();

        } catch (Exception e) {
            // Handle io error
            System.out.println("Cannot read/write! Exit!");
            e.printStackTrace();
            System.exit(3);
        }

        // cratering pool
        ForkJoinPool forkJoinPool = new ForkJoinPool(PARALLELISM);
        try {
            forkJoinPool.submit(() -> {
                IntStream.rangeClosed(1, ITERATION)
                        .parallel()
                        .forEach(i -> {
                                    long s = System.currentTimeMillis();
                                    Map<String, String> future = new LinkedHashMap<>();
                                    DateFormat df = new SimpleDateFormat("yyyy-MM");
                                    Calendar c = Calendar.getInstance();
                                    Random random = new Random();

                                    double lClose = Entrypoint.lastClose;
                                    try {
                                        c.setTime(df.parse(Entrypoint.lastDate));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    for (int j = 0; j < 240; j++) {
                                        c.add(Calendar.MONTH, 1);
                                        // get random value from the last 240 months
                                        double close = Entrypoint.last240Close.get(Entrypoint.last240Close.size() - 1 - random.nextInt(240));
                                        future.put(df.format(c.getTime()), close + "," + Entrypoint.calcChange(lClose, close));
                                        lClose = close;
                                    }
                                    writeToJson(future, random, "future", "iteration");

                                    System.out.println(System.currentTimeMillis() - s + " ms for thread #" + Thread.currentThread().getId());
                                }
                        );
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Iteration interrupted...skipping iteration!");
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - start + " ms");
    }

    /**
     * Write to json file
     *
     * @param map    the data to write
     * @param random random generator
     * @param dir    folder to create
     * @param file   to write (should not be unique)
     */
    private static void writeToJson(Map<String, String> map, Random random, String dir, String file) {
        try {
            FileWriter fw;
            new File(dir).mkdir();
            fw = new FileWriter(dir + File.separator + file + "-" + Thread.currentThread().getId() + "-" + random.nextInt(Integer.MAX_VALUE) + ".json");
            fw.write("[");
            boolean first = true;

            for (Map.Entry<String, String> d : map.entrySet()) {
                try {
                    String line = d.getValue();
                    if (!first) {
                        fw.write(",");
                    } else {
                        first = false;
                    }
                    double close = Double.valueOf(line.split(",")[0]);
                    double change = Double.valueOf(line.split(",")[1]);
                    fw.write(
                            "{\"date\": \"" + d.getKey() + "\"," +
                                    "\"close\": \"" + close + "\"," +
                                    "\"change\": \"" + change + "\"}"
                    );
                } catch (IOException ioe) {
                    // Handle read error
                    System.out.println(ioe.getMessage());
                    ioe.printStackTrace();
                }
            }
            fw.write("]");
            fw.close();

        } catch (Exception e) {
            System.out.println("Error writing file. Write operation has been skipped!");
            e.printStackTrace();
        }
    }

    /**
     * Calculate drop or gain compared to the last close
     *
     * @param lastClose last close value
     * @param actClose  actual close value
     * @return percentage of change
     */

    public static String calcChange(double lastClose, double actClose) {
        return lastClose < 0.0 ? "0" : String.valueOf(100.0 - ((lastClose / actClose) * 100.0));
    }

    /**
     * Print help
     */
    private static void printHelp() {
        System.out.println("<cmd> \"<input.csv>\"");
        System.exit(-1);
    }


}
