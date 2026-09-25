import java.io.*;
import java.util.*;

public class SaveManager {
    public static String currentUser = "guest";
    private static final String FILE_NAME = "game_data.csv";

    public static void loadProgress(String username) {
        if (username != null && !username.trim().isEmpty()) {
            // Lowercase for case-insensitivity, replace comma to protect CSV format
            currentUser = username.trim().toLowerCase().replace(",", "");
            if (currentUser.isEmpty()) currentUser = "guest";
        } else {
            currentUser = "guest";
        }
        
        resetProgress(); // Default state

        File saveFile = new File(FILE_NAME);
        if (!saveFile.exists()) {
            saveProgress();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            String line;
            boolean userFound = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Username")) continue; // Skip header
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(currentUser)) {
                    userFound = true;
                    for (int i = 0; i < 4; i++) {
                        if (i + 1 < parts.length) {
                            MenuPanel.levelUnlocked[i] = Boolean.parseBoolean(parts[i + 1]);
                        }
                    }
                    break;
                }
            }
            if (!userFound) {
                saveProgress(); // User not found, save new user defaults
            }
        } catch (IOException e) {
            System.out.println("Error reading save file: " + e.getMessage());
        }
    }

    public static void saveProgress() {
        File saveFile = new File(FILE_NAME);
        List<String> allLines = new ArrayList<>();
        boolean userFound = false;

        if (saveFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Username")) {
                        allLines.add(line); // Keep header
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].equals(currentUser)) {
                        userFound = true;
                        allLines.add(buildUserLine()); // Replace user's line
                    } else {
                        allLines.add(line); // Keep other users
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading before save: " + e.getMessage());
            }
        } else {
            // Create header if new file
            StringBuilder header = new StringBuilder("Username");
            for (int i = 1; i <= 4; i++) header.append(",Level").append(i);
            allLines.add(header.toString());
        }

        if (!userFound) {
            allLines.add(buildUserLine()); // Append this new user to the end
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
            for (String l : allLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing save file: " + e.getMessage());
        }
    }

    private static String buildUserLine() {
        StringBuilder sb = new StringBuilder(currentUser);
        for (int i = 0; i < 4; i++) {
            sb.append(",").append(MenuPanel.levelUnlocked[i]);
        }
        return sb.toString();
    }

    private static void resetProgress() {
        for (int i = 0; i < 4; i++) {
            MenuPanel.levelUnlocked[i] = (i == 0); // Only level 1 unlocked
        }
    }

    public static List<String> getLeaderboard() {
        File saveFile = new File(FILE_NAME);
        if (!saveFile.exists()) return new ArrayList<>();

        Map<String, Integer> userScores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Username")) continue;
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String username = parts[0];
                    int levelsCount = 0;
                    for (int i = 1; i < parts.length; i++) {
                        if (Boolean.parseBoolean(parts[i])) {
                            levelsCount++;
                        }
                    }
                    userScores.put(username, levelsCount);
                }
            }
        } catch (Exception e) {}

        List<Map.Entry<String, Integer>> list = new ArrayList<>(userScores.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> leaderboard = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : list) {
            int level = entry.getValue();
            String levelStr = (level > 3) ? "All Cleared" : "Lv." + level;
            leaderboard.add(entry.getKey() + " - " + levelStr);
            count++;
            if (count >= 5) break; 
        }
        return leaderboard;
    }

    public static boolean isExistingUser(String username) {
        File saveFile = new File(FILE_NAME);
        if (!saveFile.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            String line;
            String lowerUser = username.trim().toLowerCase().replace(",", "");
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Username")) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(lowerUser)) {
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }
}
