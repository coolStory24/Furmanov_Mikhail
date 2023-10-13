package org.example.sort;

import org.example.enums.SortingTypes;

import java.util.List;

public class BubbleSort extends Sort {
  public BubbleSort(int maxLength) {
    super(SortingTypes.BUBBLE_SORT, maxLength);
  }

  @Override
  public void sort(List<Integer> arrToSort) {
    if (arrToSort.size() >= maxLength) {
      throw new RuntimeException(
        String.format(
          "The size of array is %d, while max size is %d",
          arrToSort.size(),
          maxLength
        )
      );
    }

    for (int i = 0; i < arrToSort.size(); i++) {
      for (int j = i + 1; j < arrToSort.size(); j++) {
        if (arrToSort.get(i) > arrToSort.get(j)) {
          int temp = arrToSort.get(i);
          arrToSort.set(i, arrToSort.get(j));
          arrToSort.set(j, temp);
        }
      }
    }

  }
}
