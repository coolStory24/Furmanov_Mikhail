package org.example.sort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test bubble sort")
class BubbleSortTest {
  @Test
  @DisplayName("Throw exception if array size is too big")
  void testMaxSizeException() {
    List<Integer> stubArr = new ArrayList<Integer>(List.of(1, 2, 3));
    BubbleSort bubbleSort = new BubbleSort(2);
    Exception exception = assertThrows(RuntimeException.class, () -> {
      bubbleSort.sort(stubArr);
    });

    String expectedMessage = "The size of array is 3, while max size is 2";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @DisplayName("Sort sample array")
  void testSort() {
    List<Integer> stubArr = new ArrayList<Integer>(List.of(7, 4, 36, 34));
    BubbleSort bubbleSort = new BubbleSort(10);

    bubbleSort.sort(stubArr);
    List<Integer> expectedArr = new ArrayList<Integer>(List.of(4, 7, 34, 36));

    if (expectedArr.size() != stubArr.size()) {
      fail("Different array size");
    }
    for (int i = 0; i < expectedArr.size(); i++) {
      if (!Objects.equals(expectedArr.get(i), stubArr.get(i))) {
        fail("Array is not sorted");
      }
    }
    assertTrue(true);
  }
}