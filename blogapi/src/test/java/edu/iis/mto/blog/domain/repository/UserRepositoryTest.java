package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.flush();
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    void shouldFindOneUserByFirstaname(){
        User persistedUser = repository.save(user);

        List<User> users = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "", "");

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    void shouldFindOneUserByPartOfEmail(){
        repository.save(user);
        List<User> users = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "john@do");

        assertThat(users, hasSize(1));

    }

    @Test
    void shouldFindOneUserByFirstnameAndEmail(){
        repository.save(user);
        List<User> users = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "", "john@domain.com");

        assertThat(users, hasSize(1));

    }
    @Test
    void shouldFindZeroUserByInvalidFirstnameAndEmail(){
        List<User> users = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jand", "", "johsn@domadin.com");

        assertThat(users, hasSize(0));

    }

    @Test
    void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

}
