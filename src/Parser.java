import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Parser {

    //Create a BST tree of Integer type
    private BST<FIFARecord> mybst = new BST<>();
    // lookup table
    private Map<String, FIFARecord> allPlayers = new HashMap<>(); // lookup table

    public Parser(String filename) throws FileNotFoundException, IOException {
        process(new File(filename));
    }

    // Implement the process method
    public void process(File input) throws FileNotFoundException, IOException {
        Path path = input.toPath();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String headerLine = br.readLine(); // read header
            if (headerLine == null) {
                writeToFile("Empty CSV file.", "./result.txt");
                return;
            }

            String[] headers = headerLine.split(",");
            Map<String,Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                idx.put(headers[i].trim(), i);
            }

            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String playerSlug   = row[idx.get("player_slug")].trim();
                String name         = row[idx.get("name")].trim();
                String fullName     = row[idx.get("full_name")].trim();
                String bestPosition = row[idx.get("best_position")].trim();
                int overall         = parseInt(row[idx.get("overall_rating")]);
                int potential       = parseInt(row[idx.get("potential")]);

                FIFARecord rec = new FIFARecord(playerSlug, name, bestPosition, fullName, overall, potential);
                allPlayers.put(playerSlug, rec);

                mybst.add(rec);
                count++;
            }
            writeToFile("Inserted " + count + " FIFA records into BST.", "./result.txt");
        }
    }

    // Implement the operate_BST method
    // Determine the incoming command and operate on the BST
    public void operate_BST(String[] command) {
        if (command == null || command.length == 0) return;
        String op = command[0].toUpperCase();

        switch (op) {
            //add by player's slug
            case "ADD" -> {
                if (command.length < 2) {
                    writeToFile("Usage: ADD <player_slug>", "./result.txt");
                    break;
                }
                String slug = command[1];
                FIFARecord rec = allPlayers.get(slug);

                if (rec == null) {
                    writeToFile("No player found with slug: " + slug, "./result.txt");
                } else {
                    mybst.add(rec);
                    writeToFile("ADDED: " + rec, "./result.txt");
                }
            }

            case "PRINT_INORDER" -> {
                for (FIFARecord r : mybst) {
                    writeToFile(r.toString(), "./result.txt");
                }
            }
            case "SIZE" -> writeToFile("SIZE=" + mybst.size(), "./result.txt");
            case "CLEAR" -> { mybst.clear(); writeToFile("CLEARED", "./result.txt"); }

            case "REMOVE" -> {
                if (command.length < 2) {
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

    // CSV helper
    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
