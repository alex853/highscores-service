package net.simforge.highscores;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HighScores {
    private static final Queue<PlayerScore> queue = new LinkedList<>();
    private static List<PlayerScore> highscores;

    public static synchronized void putScore(String player, int score) {
        queue.add(new PlayerScore(player, score));
        removeExpired();
        resetHighscores();
    }

    public static synchronized List<PlayerScore> getHighScores() {
        if (removeExpired()) {
            resetHighscores();
        }

        if (highscores != null) {
            return highscores;
        }

        highscores = new ArrayList<>(queue);
        highscores.sort((s1, s2) -> {
            int scoreDiff = -Integer.compare(s1.score, s2.score);
            if (scoreDiff != 0) {
                return scoreDiff;
            }
            return Long.compare(s1.added, s2.added);
        });
        highscores = highscores.subList(0, Math.min(10, highscores.size()));
        return highscores;
    }

    private static void resetHighscores() {
        highscores = null;
    }

    private static boolean removeExpired() {
        boolean hasAnythingChanged = false;
        while (!queue.isEmpty()) {
            PlayerScore playerScore = queue.peek();
            if (playerScore == null) {
                break;
            }

            if (System.currentTimeMillis() - playerScore.getAdded() < 3600000) {
                break;
            }

            queue.poll();
            hasAnythingChanged = true;
        }
        return hasAnythingChanged;
    }

    public static class PlayerScore {
        private final String player;
        private final int score;
        private final long added;

        PlayerScore(String player, int score) {
            this.player = player;
            this.score = score;
            this.added = System.currentTimeMillis();
        }

        public String getPlayer() {
            return player;
        }

        public int getScore() {
            return score;
        }

        public long getAdded() {
            return added;
        }
    }
}
