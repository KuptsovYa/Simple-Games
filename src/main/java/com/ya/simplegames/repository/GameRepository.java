package com.ya.simplegames.repository;

import com.ya.simplegames.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {

    /**
    * Returns all games in "games" table
    */
    Iterable<Game> findAll();

}