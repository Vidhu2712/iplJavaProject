import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class ExtraRunsConcededByTeamsIn2016 {
    public static void main(String[] args) {
        String matchesFilePath = "src/main/java/org/project/data/matches.csv";
        String deliveryFilePath = "src/main/java/org/project/data/deliveries.csv";
        // step 1 extract matches id for year 2016 in matches.csv file
        HashSet<String> matchesIn2016 = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(matchesFilePath))) {
            String line = br.readLine();//skip the 1st line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String matchId = data[0];
                String Season = data[1];
                if (Season.equals("2016")) {
                    matchesIn2016.add(matchId);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // step 2 Calculate extra runs conceeded by each team in 2016
        HashMap<String, Integer> extraRunsConceeded = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(deliveryFilePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String matchId = data[0];
                String bowlingTeam = data[3];
                int extraRuns = Integer.parseInt(data[16]);
                //step 3 if this above matchId is there in the set of matchesIn2016 then only add.
                if (matchesIn2016.contains(matchId)) {
                    extraRunsConceeded.put(bowlingTeam, extraRunsConceeded.getOrDefault(bowlingTeam, 0) + extraRuns);
                }
            }
            System.out.println("Extra Runs Conceeded by each team in 2016");
            for (String team : extraRunsConceeded.keySet()) {
                System.out.println("Team" + ":"+ team + ":" + "ExtraRuns:-->" + extraRunsConceeded.get(team));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




