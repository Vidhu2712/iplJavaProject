import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int MATCH_ID = 0;
    private static final int YEAR = 1;
    private static final int TEAM_1 = 4;
    private static final int TEAM_2 = 5;

    private static final int DELIVERY_MATCH_ID = 0;
    private static final int BOWLER = 8;
    private static final int EXTRA_RUNS = 16;
    private static final int TOTAL_RUNS = 17;
    private static final int Bowling_Team = 3;

    public static void main(String[] args) {
        List<Match>matches = getMatchesData();
        List<Delivery>deliveries = getDeliveriesData();


        findMatchesPlayedPerYear(matches);
        findMatchesWonPerTeam(matches);
        findTopTenEconomicalBowlersIn2015(matches, deliveries);
        findTheExtraRunsConcededPerTeamIn2016(matches, deliveries);
    }

    public static List<Match> getMatchesData() {
        List<Match>matches = new ArrayList<>();
        try (BufferedReader br =
                     new BufferedReader(new FileReader("src/main/java/org/project/data/matches.csv"))){
            String line;
            line = br.readLine();
            while((line= br.readLine())!=null){
                String[]data = line.split(",");
                Match match = new Match();
                match.setYear(data[YEAR]);
                match.setMatchId(data[MATCH_ID]);
                match.setTeam1(data[TEAM_1]);
                match.setTeam2(data[TEAM_2]);
                matches.add(match);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static List<Delivery> getDeliveriesData() {
        List<Delivery> deliveries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader
                (new FileReader("src/main/java/org/project/data/deliveries.csv"))){
            String line;
            line = br.readLine();
            while((line= br.readLine())!=null){
                String [] data = line.split(",");
                Delivery delivery = new Delivery();
                delivery.setBowler(data[BOWLER]);
                delivery.setMatchId(data[DELIVERY_MATCH_ID]);
                delivery.setExtraRuns(Integer.parseInt(data[EXTRA_RUNS]));
                delivery.setValidDelivery(Integer.parseInt(data[EXTRA_RUNS])==0);
                delivery.setRuns(Integer.parseInt(data[TOTAL_RUNS]));
                delivery.setBowlingTeam(data[Bowling_Team]);
                deliveries.add(delivery);
            }
            }
        catch (IOException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    public static void findMatchesPlayedPerYear(List<Match> matches) {
        HashMap<String,Integer>matchesPlayedPerYear = new HashMap<>();
        for(Match match:matches){
            String year = match.getYear();
            if(matchesPlayedPerYear.containsKey(year)){
                matchesPlayedPerYear.put(year,matchesPlayedPerYear.get(year)+1);
            }
            else{
                matchesPlayedPerYear.put(year,1);
            }
        }
        System.out.println("The Matches Played by Teams per Year");
        for(String year:matchesPlayedPerYear.keySet()){
            System.out.println("Year:"+" "+year+" "+"Matches played"+" "+matchesPlayedPerYear.get(year));
        }
        System.out.println("--------");
    }

    public static void findMatchesWonPerTeam(List<Match> matches) {
        HashMap<String,Integer>matchesWonPerTeam = new HashMap<>();
        for(Match match:matches){
            String winner = match.getTeam1();
            if(matchesWonPerTeam.containsKey(winner)){
                matchesWonPerTeam.put(winner,matchesWonPerTeam.get(winner)+1);
            }
            else{
                matchesWonPerTeam.put(winner,1);
            }
        }
        System.out.println("The Matches Won by Each Team");
        for(String team:matchesWonPerTeam.keySet()){
            System.out.println("Team"+" "+team+" "+"won matches:"+matchesWonPerTeam.get(team));
        }
        System.out.println("------");
    }


    public static void findTopTenEconomicalBowlersIn2015(List<Match> matches, List<Delivery> deliveries) {
        Set<String> matchIds2015 = new HashSet<>();
        for (Match match : matches) {
            if (match.getYear().equals("2015")) {
                matchIds2015.add(match.getMatchId());
            }
        }
        if (matchIds2015.isEmpty()) {
            System.out.println("No matches found for 2015.");
            return;
        }
        Map<String, int[]> bowlerStats = new HashMap<>();
        for (Delivery delivery : deliveries) {
            if (matchIds2015.contains(delivery.getMatchId())) {
                String bowler = delivery.getBowler();
                int[] stats = bowlerStats.getOrDefault(bowler, new int[2]);
                stats[0] += delivery.getRuns();
                if (delivery.isValidDelivery()) {
                    stats[1]++;
                }
                bowlerStats.put(bowler, stats);
            }
        }
        if (bowlerStats.isEmpty()) {
            System.out.println("No deliveries found for 2015 matches.");
            return;
        }
        Map<String, Double> economyRates = new HashMap<>();
        for (String bowler : bowlerStats.keySet()) {
            int[] stats = bowlerStats.get(bowler);
            int runsConceded = stats[0];
            int validDeliveries = stats[1];
            if (validDeliveries > 0) {
                double economyRate = (double) runsConceded / (validDeliveries / 6.0);  // Calculate economy rate
                economyRates.put(bowler, economyRate);
            }
        }
        if (economyRates.isEmpty()) {
            System.out.println("No bowlers found with valid deliveries in 2015.");
            return;
        }
        System.out.println("Top 10 Economical Bowlers in 2015:");
        for (int i = 0; i < 10 && !economyRates.isEmpty(); i++) {
            String bestBowler = null;
            double bestEconomyRate = Double.MAX_VALUE;

            // Find the bowler with the lowest economy rate
            for (Map.Entry<String, Double> entry : economyRates.entrySet()) {
                if (entry.getValue() < bestEconomyRate) {
                    bestEconomyRate = entry.getValue();
                    bestBowler = entry.getKey();
                }
            }
            if (bestBowler != null) {
                System.out.println((i + 1) + ". Bowler: " + bestBowler + " -> Economy Rate: " + bestEconomyRate);
                economyRates.remove(bestBowler);  // Remove this bowler so we can find the next best
            }
        }
        System.out.println("-----------");
    }


    public static void findTheExtraRunsConcededPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        Set<String> matchIds2016 = new HashSet<>();
        for (Match match : matches) {
            if (match.getYear().equals("2016")) {
                matchIds2016.add(match.getMatchId());
            }
        }
        Map<String, Integer> extraRunsPerTeam = new HashMap<>();
        for (Delivery delivery : deliveries) {
            if (matchIds2016.contains(delivery.getMatchId())) {
                String bowlingTeam = delivery.getBowlingTeam();
                int extraRuns = delivery.getExtraRuns();
                if (extraRunsPerTeam.containsKey(bowlingTeam)) {
                    extraRunsPerTeam.put(bowlingTeam, extraRunsPerTeam.get(bowlingTeam) + extraRuns);
                } else {
                    extraRunsPerTeam.put(bowlingTeam, extraRuns);
                }
            }
        }
        System.out.println("Extra runs conceded by each team in 2016:");
        for (String team : extraRunsPerTeam.keySet()) {
            System.out.println("Team: " + team + " -> Extra Runs: " + extraRunsPerTeam.get(team));
        }
    }
}

