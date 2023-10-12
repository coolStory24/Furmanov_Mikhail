package org.example.sort;

import org.example.enums.SortingTypes;
import org.example.lists.ListCloner;

import java.util.List;

public class SortStrategy {
  final protected ListCloner listCloner;
  final protected Sort[] sortsArr;

  public SortStrategy(Sort[] sortsArr) {
    listCloner = new ListCloner();
    this.sortsArr = sortsArr;
  }

  protected List<Integer> cloneArr(List<Integer> arr) {
    return this.listCloner.clone(arr);
  }

  public List<Integer> sort(List<Integer> arrToSort, SortingTypes sortingType) {
    boolean sortCompleted = false;
    List<Integer> clonedArr = listCloner.clone(arrToSort);

    // try to sort with first sorting method with appropriate type
    for (Sort sortMethod : sortsArr) {
      try {
        if (sortMethod.sortingType == sortingType) {
          sortMethod.sort(clonedArr);
          sortCompleted = true;
        }
      } catch (Exception ignored) {
      }
    }

    if (sortCompleted) return clonedArr;

    // try to sort with first sorting method available
    for (Sort sortMethod : sortsArr) {
      try {
        sortMethod.sort(clonedArr);
        sortCompleted = true;
      } catch (Exception ignored) {
      }
    }

    if (sortCompleted) {
      return clonedArr;
    } else throw new RuntimeException("Appropriate sorting method not found");
  }
}
