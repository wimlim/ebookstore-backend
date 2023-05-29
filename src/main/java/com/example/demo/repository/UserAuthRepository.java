package com.example.demo.repository;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    UserAuth findByUser(User user);

    UserAuth findByToken(int token);
}
