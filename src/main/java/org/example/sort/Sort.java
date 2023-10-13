package org.example.sort;

import org.example.enums.SortingTypes;

import java.util.List;

public abstract class Sort {
  //change the existing array
  public abstract void sort(List<Integer> arrToSort);

  final protected SortingTypes sortingType;
  final protected int maxLength;

  public Sort(SortingTypes sortingType, int maxLength) {
    this.sortingType = sortingType;
    this.maxLength = maxLength;
  }
}
