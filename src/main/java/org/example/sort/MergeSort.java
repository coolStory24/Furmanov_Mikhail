package org.example.sort;

import org.example.enums.SortingTypes;

import java.util.Collections;
import java.util.List;

public class MergeSort extends Sort {

  public MergeSort(int maxLength) {
    super(SortingTypes.MERGE_SORT, maxLength);
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

    Collections.sort(arrToSort);
  }
}
