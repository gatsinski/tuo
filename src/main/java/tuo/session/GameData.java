package tuo.session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GameData {
    private Points points;
    private int totalKnowledge;  // Stores the total knowledge gathered during preparation stage
    private List<Round> rounds;
    private int currentRoundIndex;
    private List<BattleRound> battleRounds;
    private int currentBattleRoundIndex;
    private List<Short> grades;

    public GameData(List<Round> rounds, List<BattleRound> battleRounds) {
        this.points = new Points(0, 0);
        this.totalKnowledge = 0;
        this.rounds = rounds;
        this.currentRoundIndex = 0;
        this.battleRounds = battleRounds;
        this.currentBattleRoundIndex = 0;
        this.grades = new ArrayList<>();
    }

    private static int getRandomNumber(int min, int max) {
        return (int)(Math.random()*((max-min)+1))+min;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public int getTotalKnowledge() {
        return totalKnowledge;
    }

    public void setTotalKnowledge(int totalKnowledge) {
        this.totalKnowledge = totalKnowledge;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public int getCurrentRoundNumber() { return currentRoundIndex + 1; }

    public void setCurrentRoundIndex(int currentRoundIndex) {
        this.currentRoundIndex = currentRoundIndex;
    }

    public List<BattleRound> getBattleRounds() {
        return battleRounds;
    }

    public void setBattleRounds(List<BattleRound> battleRounds) {
        this.battleRounds = battleRounds;
    }

    public int getCurrentBattleRoundIndex() {
        return currentBattleRoundIndex;
    }

    public int getCurrentBattleRoundNumber() { return currentBattleRoundIndex + 1; }

    public void setCurrentBattleRoundIndex(int currentBattleRoundIndex) {
        this.currentBattleRoundIndex = currentBattleRoundIndex;
    }

    public List<Short> getGrades() {
        return grades;
    }

    public void setGrades(List<Short> grades) {
        this.grades = grades;
    }

    public Round getCurrentRound() {
        return rounds.get(currentRoundIndex);
    }

    public void goToNextRound() {
        currentRoundIndex++;
    }

    public BattleRound getCurrentBattleRound() {
        return battleRounds.get(currentBattleRoundIndex);
    }

    public void goToNextBattleRound() {
        currentBattleRoundIndex++;
    }

    public boolean isTheGameValid() {
        return this.rounds.size() + this.battleRounds.size() == 0;
    }

    public boolean isExaminationReached() {
        return currentRoundIndex == rounds.size() && rounds.get(rounds.size()-1).isFinished();
    }

    public boolean isTheGameOver() {
        return this.currentBattleRoundIndex + 1 >= this.battleRounds.size();
    }

    public void placeBet(int bet) {
        points.knowledge -= bet;
        short grade = 2;

        for(short i=3; i<=6; i++){
            int gradeLevel = getRandomNumber((i*5)-3, (i*5)+3);
            if (bet > gradeLevel) {
                grade = i;
            }
        }

        grades.add(grade);

        if (points.knowledge == 0) {
            for(int i = currentBattleRoundIndex; i<battleRounds.size(); i++) {
                grades.add((short) 2);
            }
            this.currentBattleRoundIndex = this.battleRounds.size();
        }
    }

    public void updateScore(Points points) {
        this.points.reputation += points.reputation;
        this.points.knowledge += points.knowledge;
        this.totalKnowledge += points.knowledge;
    }

    public double calculateFinalScore() {
        double sum = grades.stream().mapToInt(Short::intValue).sum();
        double multiplier = sum / grades.size();
        return round(points.reputation * multiplier, 2);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
