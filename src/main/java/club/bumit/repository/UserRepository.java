package club.bumit.repository;

import club.bumit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoDB Spring Data Repository for {@see User} Objects
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findById(String id);

    User findByName(String name);
}
