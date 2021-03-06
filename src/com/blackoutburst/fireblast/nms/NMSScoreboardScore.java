package com.blackoutburst.fireblast.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class NMSScoreboardScore {

    protected Object scoreboardScore;

    protected String name;

    private Class<?> scoreClass;

    public String getName() {
        return name;
    }

    public int getScore() {
        try {
            final Method method = scoreClass.getMethod("getScore");

            return (int) method.invoke(scoreboardScore);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return (-1);
    }

    public NMSScoreboardScore(NMSScoreboard scoreboard, String name) {
        try {
            scoreClass = NMS.getClass("ScoreboardScore");

            final Class<?> scoreboardClass = NMS.getClass("Scoreboard");
            final Class<?> scoreboardObjectiveClass = NMS.getClass("ScoreboardObjective");

            final Constructor<?> scoreboardConstructor = scoreClass.getConstructor(scoreboardClass, scoreboardObjectiveClass, String.class);

            scoreboardScore = scoreboardConstructor.newInstance(scoreboard.scoreboard, scoreboard.objective, name);
            this.name = name;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public NMSScoreboardScore(NMSScoreboard scoreboard, String name, int score) {
        try {
            scoreClass = NMS.getClass("ScoreboardScore");

            final Class<?> scoreboardClass = NMS.getClass("Scoreboard");
            final Class<?> scoreboardObjectiveClass = NMS.getClass("ScoreboardObjective");

            final Constructor<?> scoreboardConstructor = scoreClass.getConstructor(scoreboardClass, scoreboardObjectiveClass, String.class);

            scoreboardScore = scoreboardConstructor.newInstance(scoreboard.scoreboard, scoreboard.objective, name);
            this.name = name;
            setScore(score);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setScore(int value) {
        try {
            final Method method = scoreClass.getMethod("setScore", int.class);

            method.invoke(scoreboardScore, value);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
