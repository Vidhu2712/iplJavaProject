import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class matchesPerYear {

    public static void main(String[] args) {
        // File path for CSV inside the data folder
        String filePath = "src/main/java/org/project/data/matches.csv";


        // HashMap to store year as key and number of matches played as value
        HashMap<String, Integer> matchesPerYear = new HashMap<>();

        // Try-with-resources block to handle file reading
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the header to skip it
            line = br.readLine();

            // Read each line from the CSV
            while ((line = br.readLine()) != null) {
                // Split the line by commas to extract fields
                String[] data = line.split(",");

                // Assuming the season (year) is in the 2nd column (index 1)
                String year = data[1];

                // If the year is already in the map, increment its count, otherwise add it with count 1
                matchesPerYear.put(year, matchesPerYear.getOrDefault(year, 0) + 1);
            }

        } catch (IOException e) {
            // Handle any potential IO exceptions
            e.printStackTrace();
        }

        // Print the number of matches played per year
        System.out.println("Matches played per year in IPL:");
        for (String year : matchesPerYear.keySet()) {
            System.out.println("Year: " + year + " -> Matches: " + matchesPerYear.get(year));
        }
    }
}
