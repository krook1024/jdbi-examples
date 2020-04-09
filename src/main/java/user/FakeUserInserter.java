package user;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Uses {@code java-faker} to generate fake users which it then
 * tries to insert to the table using the {@code UserDao} it gets.
 */
public class FakeUserInserter {
    private UserDao dao;
    private int count;

    public FakeUserInserter(UserDao dao) {
        this.dao = dao;
        this.count = count;
    }

    public void insert() {
        this.insert(1);
    }

    public void insert(int count) {
        Faker faker = new Faker();
        User user = null;

        // using the goes-to operator
        // more info on that: https://stackoverflow.com/questions/25722718/what-is-in-java
        while (count --> 0) {
            String fullName = faker.funnyName().name();
            String userName = fullName
                    .toLowerCase()
                    .replace(' ', '.');
            String email = userName + "@" + faker.internet().domainName();
            user = User.builder()
                    .username(userName)
                    .password(faker.internet().password())
                    .name(fullName)
                    .email(email)
                    .gender(User.Gender.MALE)
                    .dob(convertToLocalDateViaInstant(faker.date().birthday()))
                    .enabled(faker.bool().bool())
                    .build();
            dao.insert(user);
        }
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
