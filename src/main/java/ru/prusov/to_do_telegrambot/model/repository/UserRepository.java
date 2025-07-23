package ru.prusov.to_do_telegrambot.model.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByChatId(long chatId);

    @Query("select u.state from uUser u where u.chatId = :chatId")
    UserState getUserStateByChatId(@Param("chatId") long chatId);

    @Modifying
    @Transactional
    @Query("update User u set u.state = :userState where u.chatId = :chatId")
    void setUserStateByChatId(@Param("userState") UserState userState,
                              @Param("chatId") long chatId);
}
