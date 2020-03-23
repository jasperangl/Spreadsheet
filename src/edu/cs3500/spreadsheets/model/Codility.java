package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Codility {

  public int solution(int[] A) {
    // write your code in Java SE
    int x = A.length;
    int fin = 0;
    int y = 0;
    for (int i = 0; i < x; i += 2) {
      if((i == 0 || A[i] < A[i-1]) && (A[i] < A[i+1])) {
        continue;
      }
      else {
        y += 1;
      }
    }
    int z = 0;
    for (int i = 1; i < x; i += 2) {
      if((A[i] < A[i-1]) && (i == x -1 || A[i] < A[i+1])) {
        continue;
      }
      else {
        z += 1;

      }
    }
    return Math.min(x,y);
  }

}
