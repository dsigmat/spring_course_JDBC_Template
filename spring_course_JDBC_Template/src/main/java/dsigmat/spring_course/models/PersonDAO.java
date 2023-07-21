package dsigmat.spring_course.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", getPersonRowMapper());
    }

    public Person show(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM person WHERE id = ?", new Object[]{id}, getPersonRowMapper());
        } catch (EmptyResultDataAccessException e) {
            // Обрабатываем ситуацию, когда персона с таким id не найдена
            System.out.println("Person with ID: " + id + " not found.");
            return null;
        }
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person (name, age, email) VALUES (?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name = ?, age = ?, email = ? WHERE id = ?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }

    private RowMapper<Person> getPersonRowMapper() {
        return (resultSet, i) -> {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");

            return new Person(id, name, age, email);
        };
    }
}
