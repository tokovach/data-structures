package com.mytraining.javatraining.bubblesort;

/**
 * BubbleSort algorithm implementation.
 */
public class BubbleSort {

	/**
	 * Linear method that sorts an array.
	 *
	 * @param array is an integer array we want to sort
	 */
	public static void bubbleSort(int[] array) {
		int length = array.length;
		int i = 0;
		boolean swapNeeded;
		while (i < length - 1) {
			swapNeeded = false;
			for (int j = 1; j < length - i; j++) {
				if (array[j - 1] > array[j]) {
					swap(j - 1, j, array);
					swapNeeded = true;
				}
			}
			if (!swapNeeded) {
				break;
			}
			i++;
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
