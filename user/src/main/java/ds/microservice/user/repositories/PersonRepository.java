package ds.microservice.user.repositories;

import ds.microservice.user.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Person> findByPersonName(String personName);

    Optional<Person> findByEmail(String email);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT p " +
            "FROM Person p " +
            "WHERE p.personName = :personName " +
            "AND p.age >= 60  ")
    Optional<Person> findSeniorsByName(@Param("personName") String personName);


}
