package net.simforge.highscores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${highscores.expiration.time}")
    private Long expirationTime;

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @RequestMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello, World!";
    }

    @PutMapping("/v1/rolling/score")
    public ResponseEntity<Object> putScore(@RequestParam(value = "player", required = true) String player, @RequestParam(value = "score", required = true) String scoreStr) {
        updateExpirationTime();

        int score;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        logger.info("Player {} got score {}", player, score);
        HighScores.putScore(player, score);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/rolling/highscores")
    public ResponseEntity<Object> getHighScores() {
        updateExpirationTime();

        logger.info("Highscores requested");
        return ResponseEntity.ok(HighScores.getHighScores());
    }

    private void updateExpirationTime() {
        if (expirationTime != null) {
            HighScores.setExpirationTime(expirationTime);
        }
    }

    @GetMapping("/v1/highscores")
    public ResponseEntity<Object> getHighScoresH2() {
        logger.info("Highscores requested");
        List<PlayerScore> top10 = playerScoreRepository.findTop10ByOrderByScoreDesc();
        return ResponseEntity.ok(top10);
    }

    @PutMapping("/v1/score")
    public ResponseEntity<Object> putScoreH2(@RequestParam(value = "player", required = true) String player, @RequestParam(value = "score", required = true) String scoreStr) {
        int score;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        logger.info("Player {} got score {}", player, score);

        PlayerScore playerScore = new PlayerScore();
        playerScore.setPlayer(player);
        playerScore.setScore(score);
        playerScore.setDt(LocalDateTime.now());
        playerScoreRepository.save(playerScore);

        return ResponseEntity.ok().build();
    }
}
