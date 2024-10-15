import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
public class MatchesWonByAllTeamsOverAllYearsOfIpl {
    public static void main(String[] args) {
        String filepath = "src/main/java/org/project/data/matches.csv";
        HashMap<String, Integer> matchesWonByTeam = new HashMap();
        // try-with-resources block to handle file reading
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            // read the header line and skip it
            line = br.readLine();
            // override the above line with 1st row onwards
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // winner section is in the 11th column and the data array is starting from 0th index
                String winner = data[10];
                // if winner column is not empty,update the hashmap winner key's value count+1
                if (!winner.isEmpty()) {
                    matchesWonByTeam.put(winner, matchesWonByTeam.getOrDefault(winner, 0) + 1);


                }
            }
        } catch (IOException e) {
            // Handle any potential IO Exceptions
            e.printStackTrace();
        }
        // print the number of matches won by each team
        System.out.println("Matches won by each team overall the years in ipl");
        // here team is working as an iterator collecting the keys from the map in string form.
        for (String team : matchesWonByTeam.keySet()) {
            System.out.println("Team" + team + "won:" + matchesWonByTeam.get(team));

        }
    }
}

