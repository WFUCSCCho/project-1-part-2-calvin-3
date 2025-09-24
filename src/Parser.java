/*∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
  @file: Parser.java
  @description: This file reads and processes input commands from my FIFA player data file and applies them to a generic BST
  @author: Calvin Malaney
  @date: September 23, 2025
∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*/

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Parser {

    //Create a BST tree of Integer type
    private BST<FIFARecord> mybst = new BST<>();
    // lookup table
    private Map<String, FIFARecord> allPlayers = new HashMap<>();
    private static final String CSV_SPLIT = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    // parser constructor
    public Parser(String csvPath, String commandPath) throws FileNotFoundException, IOException {
        loadCsv(Paths.get(csvPath));
        process(new File(commandPath));
    }

    // Implement the process method
    public void process(File input) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(input.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                // calls operate_BST
                operate_BST(line.split("\\s+"));
            }
        }
    }

    // loads the CSV - fills allPlayers map - BST stays empty.
    private void loadCsv(Path path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = readCsvRecord(br);
            if (header == null) { writeToFile("Empty CSV.", "./result.txt"); return; }

            String[] headers = header.split(CSV_SPLIT, -1);
            Map<String,Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.length; i++) idx.put(headers[i].trim(), i);

            for (String req : new String[]{"player_slug","name","full_name","best_position","overall_rating","potential"}) {
                if (!idx.containsKey(req)) { writeToFile("Missing header: " + req, "./result.txt"); return; }
            }

            String rec;
            int count = 0;
            while ((rec = readCsvRecord(br)) != null) {
                if (rec.isBlank()) continue;
                String[] row = rec.split(CSV_SPLIT, -1);

                String slug   = unquote(row[idx.get("player_slug")]).trim();
                String name   = unquote(row[idx.get("name")]).trim();
                String full   = unquote(row[idx.get("full_name")]).trim();
                String best   = unquote(row[idx.get("best_position")]).trim();
                int overall   = parseInt(row[idx.get("overall_rating")]);
                int potential = parseInt(row[idx.get("potential")]);

                allPlayers.put(slug, new FIFARecord(slug, name, best, full, overall, potential));
                count++;
            }
            writeToFile("Loaded " + count + " players from CSV.", "./result.txt");
        }
    }

    // CSV helpers
    private String readCsvRecord(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) return null;
        StringBuilder sb = new StringBuilder(line);
        // append lines while quotes are unbalanced (handles multiline quoted fields)
        while ((countQuotes(sb) & 1) == 1) {
            String next = br.readLine();
            if (next == null) break;
            sb.append('\n').append(next);
        }
        return sb.toString();
    }

    private int countQuotes(CharSequence s) {
        int c = 0; for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '"') c++; return c;
    }

    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\""))
            return s.substring(1, s.length()-1).replace("\"\"", "\"");
        return s;
    }

    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // Implement the operate_BST method
    // Determine the incoming command and operate on the BST
    public void operate_BST(String[] command) {
        if(command == null || command.length == 0) {
            writeToFile("Invalid command","./result.txt");
            return;
        }

        switch (command[0]) {
            //add by player's slug
            case "insert" -> {
                if (command.length != 2) {
                    writeToFile("Usage: INSERT <player_slug>", "./result.txt");
                    break;
                }
                String slug = command[1];
                FIFARecord rec = allPlayers.get(slug);
                if (rec == null) {
                    writeToFile("No player found with slug: " + slug, "./result.txt");
                    return;
                }
                mybst.add(rec);
                writeToFile("INSERT: " + rec, "./result.txt");
            }

            //prints in-order
            case "print" -> {
                for (FIFARecord r : mybst) {
                    writeToFile(r.toString(), "./result.txt");
                }
            }

            case "size" -> writeToFile("SIZE=" + mybst.size(), "./result.txt");
            case "clear" -> { mybst.clear(); writeToFile("CLEARED", "./result.txt"); }

            case "remove" -> {
                if (command.length != 2) {
                    writeToFile("Usage: REMOVE <player_slug>", "./result.txt");
                    break;
                }
                String slug = command[1];
                FIFARecord rec = allPlayers.get(slug);    // exact same object we built

                if (rec == null) {
                    writeToFile("No player found with slug: " + slug, "./result.txt");
                } else {
                    var removed = mybst.remove(rec);
                    writeToFile(removed != null ? "REMOVED: " + rec : "NOT FOUND IN TREE: " + slug, "./result.txt");
                }
            }
            // default case for Invalid Command
            default -> writeToFile("Invalid Command", "./result.txt");
        }
    }

    // Implement the writeToFile method
    // Generate the result file
    public void writeToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(content);
        } catch (IOException e) {
            System.out.println("Error Writing to File: " + e.getMessage());
        }
    }
}
