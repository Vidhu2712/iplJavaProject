import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
public class Top10EconomicalBowlers2015 {
    public static void main(String[] args) {
        // path set
        String matchesFile = "src/main/java/org/project/data/matches.csv";
        String deliveryFile = "src/main/java/org/project/data/deliveries.csv";
        // step 1 extract the match_id that has year 2015 in it.
        HashSet<String>matchesIn2015 = new HashSet<>();
        try(BufferedReader br = new BufferedReader(new FileReader(matchesFile))){
            String line;
            line = br.readLine();//skip 1st line
            while((line=br.readLine())!=null){
                String[] data = line.split(",");
                String matchId = data[0];
                String season = data[1];
                if(season.equals("2015")){
                    matchesIn2015.add(matchId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //step 2 create hashmap of <bowlersName,stats> stats[] = stats[0]--> totalRunsConceeded stats[1]--> valid deliveries bowled
        HashMap<String,int[]>bowlerStats = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(deliveryFile))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine())!=null){
                String[]data = line.split(",");
                String matchId = data[0];
                String bowlerName = data[8];
                String extraRunsString = data[16];
                String totalRunsString = data[17];
                // only use the matches of 2015 by comparing its matchId
                if(matchesIn2015.contains(matchId)) {
                    int extraRuns = Integer.parseInt(extraRunsString);
                    int totalRuns = Integer.parseInt(totalRunsString);

                    // create the stats integer and store totalRuns and validDeliveries bowled
                    int[] stats = bowlerStats.getOrDefault(bowlerName, new int[2]);// return new int[2]
                    // because in our hashmap the values for key bolwerName is null.
                    stats[0] += totalRuns;
                    // valid deliveries will only be there if there are no extra runs.
                    if(extraRuns==0){
                        stats[1]+=1;
                    }
                    bowlerStats.put(bowlerName,stats);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // calculate the economy rate of each bowler and store it in arraylist
        HashMap<String,Double>bowlerEconomyRates = new HashMap<>();
        for(Map.Entry<String,int[]>entry:bowlerStats.entrySet()){
            String bowlerName = entry.getKey();
            int[]stats = entry.getValue();
            int totalRunsConceeded = stats[0];
            int ValidDeliveries = stats[1];
            // economy rate = totalRunsConceeded/totalOvers--> ValidDeliveries/6.0
            if(ValidDeliveries>0){
                double overs = (ValidDeliveries)/6.0;
                double economyRate = (double) totalRunsConceeded/ overs;
                bowlerEconomyRates.put(bowlerName,economyRate);
            }

        }
        System.out.println("Top 10 economical Bowlers from 2015");

        List<Map.Entry<String, Double>> sortedBowlers = new ArrayList<>(bowlerEconomyRates.entrySet());

        // Sort based on the economy rate (ascending order)
        sortedBowlers.sort(Map.Entry.comparingByValue());

        // Print the top 10 bowlers
        int topBowlersCount = Math.min(10, sortedBowlers.size());
        for (int i = 0; i < topBowlersCount; i++) {
            Map.Entry<String, Double> entry = sortedBowlers.get(i);
            System.out.println("Bowler: " + entry.getKey() + " -> Economy Rate: " + entry.getValue());
        }


        }


    }




