package org.example.user;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test user repository")
class UserRepositoryTest {
  @BeforeEach
  void setUp() {
    User stubUser1 = new User();
    stubUser1.put("action", "button_click");
    stubUser1.put("page", "book_card");
    stubUser1.put("firstName", "John");
    stubUser1.put("lastName", "Doe");
    stubUser1.put("msisdn", "89180000000");

    User stubUser2 = new User();
    stubUser2.put("action", "input_submit");
    stubUser2.put("page", "book_card");
    stubUser2.put("firstName", "Bojan");
    stubUser2.put("lastName", "Ennis");
    stubUser2.put("msisdn", "89180000001");

    User stubUser3 = new User();
    stubUser3.put("action", "button_click");
    stubUser3.put("page", "start_page");
    stubUser3.put("firstName", "Olu");
    stubUser3.put("lastName", "Mina");
    stubUser3.put("msisdn", "89180000002");

    UserRepository.addUser(stubUser1);
    UserRepository.addUser(stubUser2);
    UserRepository.addUser(stubUser3);
  }

  @AfterEach
  void tearDown() {
    UserRepository.clear();
  }


  @Test
  @DisplayName("Get user by msisdn")
  void testGetUserByMsisdn() {
    var user = UserRepository.getUserByMsisdn("89180000000");

    assertEquals(user.get("msisdn"), "89180000000");
  }

  @Test
  @DisplayName("Throw user not found")
  void testThrowOnUserNotFound() {
    Exception exception = assertThrows(RuntimeException.class, () -> {
      var user = UserRepository.getUserByMsisdn("89189999999");
    });

    String expectedMessage = "User with id '89189999999' not found";

    String actualMessage = exception.getMessage();

    Assertions.assertTrue(actualMessage.contains(expectedMessage));
  }
}