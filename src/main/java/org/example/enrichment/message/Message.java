package org.example.enrichment.message;

import java.util.Map;

public class Message {
  public Map<String, String> content;
  public EnrichmentType enrichmentType;

  public enum EnrichmentType {
    MSISDN;
  }

  public Message(Map<String, String> content, EnrichmentType enrichmentType) {
    this.content = content;
    this.enrichmentType = enrichmentType;
  }
}
