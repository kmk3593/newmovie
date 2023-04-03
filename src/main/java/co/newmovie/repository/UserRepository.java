package co.newmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.newmovie.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
