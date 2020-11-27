
CREATE TABLE player_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player VARCHAR(20) NOT NULL,
    score INT NOT NULL,
    dt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_player_scores_by_score ON player_scores(score);
