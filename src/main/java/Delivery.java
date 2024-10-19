public class Delivery {
    private String matchId;
    private String bowler;
    private int ExtraRuns;
    private int runs;
    private boolean validDelivery;

    public boolean isWicket() {
        return Wicket;
    }

    public void setWicket(boolean wicket) {
        Wicket = wicket;
    }

    private boolean Wicket;

    public String getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBowlingTeam(String bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
    }

    private String bowlingTeam;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }

    public int getExtraRuns() {
        return ExtraRuns;
    }

    public void setExtraRuns(int extraRuns) {
        this.ExtraRuns = extraRuns;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public boolean isValidDelivery() {
        return validDelivery;
    }

    public void setValidDelivery(boolean validDelivery) {
        this.validDelivery = validDelivery;
    }



}
