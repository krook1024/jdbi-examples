package user;

import user.User;
import user.UserDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:app");
        jdbi.installPlugin(new SqlObjectPlugin());
        try (Handle handle = jdbi.open()) {
            UserDao dao = handle.attach(UserDao.class);
            dao.createTable();

            User user = User.builder()
                    .username("johndoe1")
                    .password("asd")
                    .name("John Doe")
                    .email("johndoe@outlook.com")
                    .gender(User.Gender.MALE)
                    .dob(LocalDate.parse("1970-01-01"))
                    .enabled(true)
                    .build();

            dao.insert(user);

            dao.findByUsername("johndoe1").ifPresent(System.out::println);

            dao.findById(1).ifPresent(System.out::println);

            user = dao.findByUsername("johndoe1").orElse(null);
            dao.delete(user);

            FakeUserInserter fakeUserInserter = new FakeUserInserter(dao);
            fakeUserInserter.insert(100);

            dao.list().stream().forEach(System.out::println);
        }
    }
}
