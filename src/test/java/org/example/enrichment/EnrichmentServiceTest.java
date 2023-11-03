package org.example.enrichment;

import org.example.enrichment.message.Message;
import org.example.user.User;
import org.example.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.enrichment.message.Message.EnrichmentType.MSISDN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@DisplayName("Test enrichment service")
class EnrichmentServiceTest {
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
  @DisplayName("testEnrichMessage")
  void testEnrichMessage() {
    var user = new User();
    user.put("msisdn", "89180000000");
    user.put("action", "button_click");
    user.put("page", "book_card");

    var message = new Message(user, MSISDN);

    var enrichedMessage = EnrichmentService.enrich(message);

    assertEquals(enrichedMessage.content.get("firstName"), "John");
    assertEquals(enrichedMessage.content.get("lastName"), "Doe");
  }


  @Test
  @DisplayName("Test on user to enrich not found")
  void testEnrichUserNotFound() {
    var user = new User();
    user.put("msisdn", "89189999999");
    user.put("action", "button_click");
    user.put("page", "book_card");

    var message = new Message(user, MSISDN);

    var enrichedMessage = EnrichmentService.enrich(message);

    assertNull(enrichedMessage.content.get("firstName"));
    assertNull(enrichedMessage.content.get("lastName"));
    assertEquals(enrichedMessage.content.get("action"), "button_click");
    assertEquals(enrichedMessage.content.get("page"), "book_card");
  }

  @Test
  @DisplayName("Test end to end, multiple threads")
  void testEndToEndWithMultipleThreads() throws InterruptedException {

    String[] tels = new String[]{
      "89180000000",
      "89180000001",
      "89180000002",
      "89180000003",
      "89180000004"
    };

    List<Message> enrichmentResults = new CopyOnWriteArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    CountDownLatch latch = new CountDownLatch(tels.length);

    for (String tel : tels) {
      executorService.submit(() -> {
        var user = new User();
        user.put("msisdn", tel);
        var message = new Message(user, MSISDN);
        enrichmentResults.add(EnrichmentService.enrich(message));
        latch.countDown();
      });
    }
    latch.await();

    Collections.sort(enrichmentResults, (message1, message2) ->
      message1.content.get("msisdn").compareToIgnoreCase(message2.content.get("msisdn")));

    String[] expectedResults = new String[]{
      "{firstName=John, lastName=Doe, msisdn=89180000000}",
      "{firstName=Bojan, lastName=Ennis, msisdn=89180000001}",
      "{firstName=Olu, lastName=Mina, msisdn=89180000002}",
      "{msisdn=89180000003}",
      "{msisdn=89180000004}"
    };

    for (int i = 0; i < expectedResults.length; i++) {
      assertEquals(expectedResults[i], enrichmentResults.get(i).content.toString());
    }
  }
}
