package ds.microservice.user.services;

import ds.microservice.user.Ds2020TestConfig;
import ds.microservice.user.dtos.PersonDTO;
import ds.microservice.user.dtos.PersonDetailsDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class PersonServiceIntegrationTests extends Ds2020TestConfig {

    @Autowired
    PersonServiceImpl personService;

    @Test
    public void testGetCorrect() {
        List<PersonDTO> personDTOList = personService.findAllPersons();
        assertEquals("Test Insert Person", 1, personDTOList.size());
    }

    @Test
    public void testInsertCorrectWithGetById() {
        PersonDetailsDTO p = new PersonDetailsDTO("John", "Somewhere Else street", 22);
        UUID insertedID = personService.insertPerson(p);

        PersonDetailsDTO insertedPerson = new PersonDetailsDTO(insertedID, p.getPersonName(),p.getAddress(), p.getAge(), p.getEmail(), p.getPassword());
        PersonDetailsDTO fetchedPerson = personService.findPersonById(insertedID);

        assertEquals("Test Inserted Person", insertedPerson, fetchedPerson);
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        PersonDetailsDTO p = new PersonDetailsDTO("John", "Somewhere Else street", 22);
        personService.insertPerson(p);

        List<PersonDTO> personDTOList = personService.findAllPersons();
        assertEquals("Test Inserted Persons", 2, personDTOList.size());
    }
}
