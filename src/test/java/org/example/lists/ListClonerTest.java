package org.example.lists;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@DisplayName("Test array clone")
class ListClonerTest {
  ListCloner listCloner = new ListCloner();

  @Test
  @DisplayName("Different references")
  void testReferences() {
    var stubArr = new ArrayList<Integer>(List.of(1, 2, 3));

    var newArr = listCloner.clone(stubArr);

    assertNotSame(stubArr, newArr);
  }

  @Test
  @DisplayName("Same values")
  void testValues() {
    var stubArr = new ArrayList<Integer>(List.of(1, 2, 3));

    var newArr = listCloner.clone(stubArr);

    assertEquals(stubArr, newArr);
  }
}