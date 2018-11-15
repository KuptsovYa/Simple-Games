package com.ya.simplegames.repository;

import com.ya.simplegames.entity.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByNick(String nick);

    User findByName(String user);

    @Query(value = "SELECT * FROM users WHERE idUsers = ?1", nativeQuery = true)
    @Transactional(readOnly = true)
    User findUserByIdUser(Long userId);

    /**
     * Returns rating of this user
     * @param userId id of user
     * @return user's rating
     */
    @Query(value = "SELECT rating FROM users WHERE idUsers = ?1", nativeQuery = true)
    @Transactional(readOnly = true)
    Long getRatingById(Long userId);

    /**
     * Updating rating when player wins
     * @param userId id of winner
     * @param rating his rating
     */
    @Modifying
    @Query(value = "UPDATE users SET rating = ?2 WHERE idUsers = ?1", nativeQuery = true)
    @Transactional
    void setUpdateRating(Long userId, Long rating);

}
