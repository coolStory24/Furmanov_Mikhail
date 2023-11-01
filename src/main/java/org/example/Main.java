package org.example;

import org.example.enums.SortingTypes;
import org.example.sort.BubbleSort;
import org.example.sort.MergeSort;
import org.example.sort.Sort;
import org.example.sort.SortStrategy;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    Sort[] sorts = {new MergeSort(10), new BubbleSort(5), new MergeSort(100), new MergeSort(50)};
    SortStrategy sortStrategy = new SortStrategy(sorts);

    List<Integer> arr = new ArrayList<>(List.of(1, 4, 2, 32, 2, 6, 4, 4, 4, 6, 4));

    var sortedArr = sortStrategy.sort(arr, SortingTypes.BUBBLE_SORT);
    System.out.println(sortedArr);
    System.out.println(arr);
  }
}
