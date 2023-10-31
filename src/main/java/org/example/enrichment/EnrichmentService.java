package org.example.enrichment;

import org.example.enrichment.message.Message;
import org.example.exceptions.EnrichmentTypeNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.user.User;
import org.example.user.UserRepository;

public class EnrichmentService {
  public static Message enrich(Message message) {
    switch (message.enrichmentType) {
      case MSISDN:
        try {
          User user = UserRepository.getUserByMsisdn(message.content.get("msisdn"));
          message.content.put("firstName", user.get("firstName"));
          message.content.put("lastName", user.get("lastName"));

          return message;
        } catch (UserNotFoundException e) {
          return message;
        }

        // TODO: 10/30/23 other enrichment types
      default:
        throw new EnrichmentTypeNotFoundException();
    }
  }
}
