package com.ya.simplegames.repository;

import com.ya.simplegames.entity.Lobby;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LobbyRepository extends CrudRepository<Lobby, Long> {

    /**
     * Returns all lobbies whithout a second player
     * @param gameId id of a game (1-hangman, 2-cowsandbulls)
     * @param userId id of a player in "users" table
     * @return query where the first player is already occupied and is not the second at the same time
     */
    @Query(value = "SELECT * FROM lobby WHERE game = ?1 AND player1 IS NOT NULL AND player1 <> ?2 AND player2 IS NULL", nativeQuery = true)
    @Transactional(readOnly = true)
    Lobby findGameWithEmptyPlayer2(Long gameId, Long userId);

    /**
     * Adds to the finished game of the second player with his secret word
     * @param userId id of a player in "users" table
     * @param lobbyId id of lobby without second player
     * @param gameLog "state" of the game, but at this stage it's player's secret word
     */
    @Modifying
    @Query(value = "UPDATE lobby SET player2 = ?1, gameLog = ?3 WHERE idLobby = ?2", nativeQuery = true)
    @Transactional
    void setPlayer2(Long userId, Long lobbyId, String gameLog);

    /**
     * Delete lobby query without second player
     * @param userId id of a player in "users" table
     */
    @Modifying
    @Query(value = "DELETE FROM lobby WHERE player1 = ?1 AND player2 IS NULL", nativeQuery = true)
    @Transactional
    void deleteSemiEmptyLobbies(Long userId);

    /**
     * Returns lobbies with first player by user id
     * @param userId id of a player in "users" table
     * @return query of "lobby" table without second player (player one is waiting)
     */
    @Query(value = "SELECT COUNT(*) FROM lobby WHERE player1  = ?1 AND player2 IS NULL", nativeQuery = true)
    @Transactional(readOnly = true)
    Long findPlayer1Lobbies(Long userId);

    /**
     * Change game (when user at first click on a game button and then clicks on another)
     * @param gameId id of a game (1-hangman, 2-cowsandbulls)
     * @param userId id of a player in "users" table
     * @param word is player secret word
     */
    @Modifying
    @Query(value = "UPDATE lobby SET game = ?1, gameLog = ?3 WHERE player1  = ?2 AND player2 IS NULL", nativeQuery = true)
    @Transactional
    void setSwitchGame(Long gameId, Long userId, String word);

    /**
     * Create new game
     * @param gameId id of a game (1-hangman, 2-cowsandbulls)
     * @param userId id of a player in "users" table
     * @param word is player secret word
     */
    @Modifying
    @Query(value = "INSERT INTO lobby (game, player1, gameLog) VALUES (?1, ?2, ?3)", nativeQuery = true)
    @Transactional
    void setNewGame(Long gameId, Long userId, String word);

    /**
     * Find a specific game without a second player
     * @param gameId id of a game (1-hangman, 2-cowsandbulls)
     * @param userId id of a player in "users" table
     * @return id of lobbies where game is @gameId and player one is @userId
     */
    @Query(value = "SELECT idLobby FROM lobby WHERE game = ?1 AND player1 = ?2 AND player2 IS NULL", nativeQuery = true)
    @Transactional(readOnly = true)
    Long findPlayer1Lobby(Long gameId,Long userId);

    /**
     * Returns second player id
     * @param lobbyId id of lobby with second player
     * @return id of second player
     */
    @Query(value = "SELECT player2 FROM lobby WHERE idLobby = ?1", nativeQuery = true)
    @Transactional(readOnly = true)
    Long findPlayer2(Long lobbyId);

    /**
     * Update game state in database
     * @param lobbyId id of lobby
     * @param gameLog "state" of game
     */
    @Modifying
    @Query(value = "UPDATE lobby SET gameLog = ?2 WHERE IdLobby = ?1", nativeQuery = true)
    @Transactional
    void setUpdatedGameLog(Long lobbyId, String gameLog);

    /**
     * Delete lobby where one of players is inactive
     * @param lobbyId id of lobby
     */
    @Modifying
    @Query(value = "DELETE FROM lobby WHERE IdLobby = ?1", nativeQuery = true)
    @Transactional
    void deleteFailedLobbies(Long lobbyId);

}
