package org.example.user;

import org.example.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class UserRepository {
  private static Collection<User> users = Collections.synchronizedCollection(new ArrayList<>());

  public static synchronized void addUser(User user) {
    try {
      UserRepository.getUserByMsisdn(user.get("msisdn"));
    } catch (UserNotFoundException e) {
      UserRepository.users.add(user);
    }
  }

  public static User getUserByMsisdn(String tel) throws UserNotFoundException {
    var user = UserRepository.users.stream().filter((Map<String, String> usr) -> usr.get("msisdn").equals(tel)).findFirst().orElse(null);

    if (user == null) {
      throw new UserNotFoundException(tel);
    }

    return user;
  }

  public static Collection<User> getAll() {
    return UserRepository.users;
  }

  public static void clear() {
    UserRepository.users = Collections.synchronizedCollection(new ArrayList<>());
  }
}
