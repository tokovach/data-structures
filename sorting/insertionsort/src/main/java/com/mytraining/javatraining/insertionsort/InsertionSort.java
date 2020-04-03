package com.mytraining.javatraining.insertionsort;

public class InsertionSort {

  /**
   * Method calls the insert sort method.
   *
   * @param array is an integer array we want to sort
   */
  public static void sort(int[] array) {
    insertSort(array);
  }

  /**
   * Method sorts an array based on insertion sort algorithm.
   *
   * @param array is an integer array we want to sort
   */
  private static void insertSort(int[] array) {
    for (int i = 0; i < array.length; i++) {
      int tempIndex = i;
      while (tempIndex > 0 && array[tempIndex - 1] > array[tempIndex]) {
        swap(tempIndex, tempIndex - 1, array);
        tempIndex--;
      }
    }
  }

  /**
   * Method swaps elements in an array.
   *
   * @param firstIndex is the index of the first element we want to swap
   * @param secondIndex is the index of the second element we want to swap
   * @param array is the array in which we want to swap the elements
   */
  private static void swap(int firstIndex, int secondIndex, int[] array) {
    int tempArrayElement = array[firstIndex];
    array[firstIndex] = array[secondIndex];
    array[secondIndex] = tempArrayElement;
  }
}
