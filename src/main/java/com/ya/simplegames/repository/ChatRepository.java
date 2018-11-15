package com.ya.simplegames.repository;

import com.ya.simplegames.entity.Chat;
import com.ya.simplegames.entity.Lobby;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface ChatRepository extends CrudRepository<Chat, Long> {

    /**
     * A method that appeals to the "chat" table and adds the last message written by the user
     * depends on lobby id, player name, message and time mark that pointed when it was sanded
     * @param lobbyId is id of lobby
     * @param player is which player is send it
     * @param message is player's message
     * @param timeMark is time when it was sended to the server
     */
    @Modifying
    @Query(value = "INSERT INTO chat (lobbyId, player, message, timeMark) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    @Transactional
    void setMessage(Long lobbyId,Long player, String message, Timestamp timeMark);

    /**
     * A method that returns iterable of all messages and then deploy it on chat area
     * @param lobbyId is id of a game where it was written
     * @return query of all messages
     */
    @Query(value = "SELECT * FROM chat WHERE lobbyId = ?1", nativeQuery = true)
    @Transactional(readOnly = true)
    Iterable<Chat> findAllByLobbyId(Long lobbyId);

}
