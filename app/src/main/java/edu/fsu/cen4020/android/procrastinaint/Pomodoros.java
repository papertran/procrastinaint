package edu.fsu.cen4020.android.procrastinaint;

public class Pomodoros {

    private long OverallTime;
    private long OverallPomodoro;
    private long GoldenTomatoes;
    private long GlobalPomodoro;

    public Pomodoros(){

    }

    public Pomodoros(String username){
        this.Username = username;
    }
    private String Username;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public long getOverallTime() {
        return OverallTime;
    }

    public void setOverallTime(long overallTime) {
        OverallTime = overallTime;
    }


    public long getOverallPomodoro() {
        return OverallPomodoro;
    }

    public void setOverallPomodoro(long overallPomodoro) {
        OverallPomodoro = overallPomodoro;
    }


    public long getGoldenTomatoes() {
        return GoldenTomatoes;
    }

    public void setGoldenTomatoes(long goldenTomatoes) {
        GoldenTomatoes = goldenTomatoes;
    }


    public long getGlobalPomodoro() {
        return GlobalPomodoro;
    }

    public void setGlobalPomodoro(long globalPomodoro) {
        GlobalPomodoro = globalPomodoro;
    }
}
