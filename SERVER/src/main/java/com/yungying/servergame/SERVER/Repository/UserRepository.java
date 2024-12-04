package com.yungying.servergame.SERVER.Repository;

import com.yungying.servergame.SERVER.DTO.UserDTO;
import com.yungying.servergame.SERVER.Model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    //top5 users with highest score show only username and highest score
    @Query("SELECT new com.yungying.servergame.SERVER.DTO.UserDTO(u.username, u.highestScore) FROM User u ORDER BY u.highestScore DESC")
    List<UserDTO> findTop5ByOrderByHighestScoreDesc(PageRequest pageRequest);

}
