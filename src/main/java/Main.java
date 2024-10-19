import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int MATCH_ID = 0;
    private static final int YEAR = 1;
    private static final int TEAM_1 = 4;
    private static final int TEAM_2 = 5;
    private static final int PLAYER_OF_THE_MATCH = 13;
    private static final int VENUE = 2;

    private static final int DELIVERY_MATCH_ID = 0;
    private static final int BOWLER = 8;
    private static final int EXTRA_RUNS = 16;
    private static final int TOTAL_RUNS = 17;
    private static final int Bowling_Team = 3;
    private static final int Wicket = 18;
    public static void main(String[] args) {
        List<Match>matches = getMatchesData();
        List<Delivery>deliveries = getDeliveriesData();


        findMatchesPlayedPerYear(matches);
        findMatchesWonPerTeam(matches);
        findTopTenEconomicalBowlersIn2015(matches, deliveries);
        findTheExtraRunsConcededPerTeamIn2016(matches, deliveries);
        findThePlayerOfTheMatchInAllMatches2016(matches);
        findWicketsByEachBowlerIn2016(matches,deliveries);
        findWicketsByEachBowlerIn2016PerVenuw(matches,deliveries);
        findBowlerWithWicketsPerVenueInAllYears(matches,deliveries);


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
                match.setPlayerOfTheMatch(data[PLAYER_OF_THE_MATCH]);
                match.setVenue(data[VENUE]);
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
                if (data.length > Wicket  && !data[Wicket].trim().isEmpty()) {
                    delivery.setWicket(true);
                } else {
                    delivery.setWicket(false);
                }
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

    public static void findThePlayerOfTheMatchInAllMatches2016(List<Match>matches){
        HashMap<String,String> potm2016 = new HashMap<>();
        for(Match match:matches){
            String MatchNo = match.getMatchId();
            String Potm = match.getPlayerOfTheMatch();
            String year = match.getYear();
            if(year.equals("2016")){
                potm2016.put(MatchNo,Potm);
            }
        }
        System.out.println("Man of the Match in every match of year 2016: ");
        for(String m: potm2016.keySet()){
            System.out.println("Match Number:"+" "+m+" "+"Player of the Match:"+" "+potm2016.get(m));

        }
        System.out.println("--------------");
    }
    public static void findWicketsByEachBowlerIn2016(List<Match>matches,List<Delivery>deliveries){
        Set<String>matchIn2016 = new HashSet<>();
        for(Match match:matches){
            if(match.getYear().equals("2016")){
                matchIn2016.add(match.getMatchId());
            }
        }
        Map<String,Integer>BowlerWickets = new HashMap<>();
        for(Delivery delivery:deliveries){
            String bowler = delivery.getBowler();
            boolean isWicket = delivery.isWicket();
            if(isWicket){
                if(BowlerWickets.containsKey(bowler)){
                    BowlerWickets.put(bowler,BowlerWickets.get(bowler)+1);
                }
                else{
                    BowlerWickets.put(bowler,1);
                }
            }
        }
        System.out.println("Wickets taken by each bowler in 2016");
        for(String bowler:BowlerWickets.keySet()){
            System.out.println("Bowler:"+" "+bowler+" "+BowlerWickets.get(bowler));
        }
        System.out.println("-------------");
    }
    public static void findWicketsByEachBowlerIn2016PerVenuw(List<Match> matches, List<Delivery> deliveries) {
        Set<String> matchIds2016 = new HashSet<>();
        Map<String, String> matchVenueMap = new HashMap<>();
        for (Match match : matches) {
            if (match.getYear().equals("2016")) {
                matchIds2016.add(match.getMatchId());
                matchVenueMap.put(match.getMatchId(), match.getVenue());
            }
        }
        Map<String, Map<String, Integer>> venueBowlerWickets = new HashMap<>();
        for (Delivery delivery : deliveries) {
            String matchId = delivery.getMatchId();

            if (matchIds2016.contains(matchId)) {
                String venue = matchVenueMap.get(matchId);
                String bowler = delivery.getBowler();
                boolean isWicket = delivery.isWicket();

                if (isWicket) {
                    venueBowlerWickets.putIfAbsent(venue, new HashMap<>());
                    Map<String, Integer> bowlerWickets = venueBowlerWickets.get(venue);
                    bowlerWickets.put(bowler, bowlerWickets.getOrDefault(bowler, 0) + 1);
                }
            }
        }
        System.out.println("Bowler with the most wickets in 2016 at each venue:");
        for (String venue : venueBowlerWickets.keySet()) {
            Map<String, Integer> bowlerWickets = venueBowlerWickets.get(venue);
            String topBowler = null;
            int maxWickets = 0;
            for (Map.Entry<String, Integer> entry : bowlerWickets.entrySet()) {
                String bowler = entry.getKey();
                int wickets = entry.getValue();

                if (wickets > maxWickets) {
                    maxWickets = wickets;
                    topBowler = bowler;
                }
            }
            System.out.println("Venue: " + venue + " -> Bowler: " + topBowler + " with " + maxWickets + " wickets");
        }
        System.out.println("---------------");
    }
    public static void findBowlerWithWicketsPerVenueInAllYears(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Map<String, Integer>> venueBowlerWickets = new HashMap<>();
        Map<String, String> matchVenueMap = new HashMap<>();
        for (Match match : matches) {
            matchVenueMap.put(match.getMatchId(), match.getVenue());
        }
        for (Delivery delivery : deliveries) {
            String matchId = delivery.getMatchId();
            String venue = matchVenueMap.get(matchId);
            String bowler = delivery.getBowler();
            boolean isWicket = delivery.isWicket();

            if (isWicket) {
                venueBowlerWickets.putIfAbsent(venue, new HashMap<>());
                Map<String, Integer> bowlerWickets = venueBowlerWickets.get(venue);
                bowlerWickets.put(bowler, bowlerWickets.getOrDefault(bowler, 0) + 1);
            }
        }
        System.out.println("Bowler with the most wickets at each venue (all years):");
        for (String venue : venueBowlerWickets.keySet()) {
            Map<String, Integer> bowlerWickets = venueBowlerWickets.get(venue);

            String topBowler = null;
            int maxWickets = 0;
            for (Map.Entry<String, Integer> entry : bowlerWickets.entrySet()) {
                String bowler = entry.getKey();
                int wickets = entry.getValue();

                if (wickets > maxWickets) {
                    maxWickets = wickets;
                    topBowler = bowler;
                }
            }
            System.out.println("Venue: " + venue + " -> Bowler: " + topBowler + " with " + maxWickets + " wickets");
        }
    }
}




