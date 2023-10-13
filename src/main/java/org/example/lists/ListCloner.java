package org.example.lists;

import java.util.ArrayList;
import java.util.List;

public class ListCloner {
  public <T> List<T> clone(List<T> arr) {
    return new ArrayList<T>(arr);
  }
}
