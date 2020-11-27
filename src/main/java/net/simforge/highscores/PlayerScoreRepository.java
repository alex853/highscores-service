package net.simforge.highscores;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerScoreRepository extends CrudRepository<PlayerScore, Integer> {
    List<PlayerScore> findTop10ByOrderByScoreDesc();
}
