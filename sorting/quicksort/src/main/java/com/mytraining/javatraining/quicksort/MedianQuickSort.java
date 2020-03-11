package com.mytraining.javatraining.quicksort;

/**
 * QuickSort algorithm implementation that uses last index as pivot.
 */
public class MedianQuickSort {

	/**
	 * Method validates the input array and calls the quicksort method.
	 *
	 * @param array is an integer array we want to sort
	 */
	public static void sort(int[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		quickSort(array, 0, array.length - 1);
	}

	/**
	 * Recursive method that sorts an array based on the divide and conquer approach.
	 *
	 * @param array is an integer array we want to sort
	 * @param startIndex is the starting index for partitioning
	 * @param endIndex is the ending index for partitioning
	 */
	private static void quickSort(int[] array, int startIndex, int endIndex) {
		int middleIndex = getMiddleIndex(startIndex, endIndex);
		int pivot = array[middleIndex];
		int i = startIndex;
		int j = endIndex;
		while (i <= j) {
			while (array[i] < pivot) {
				i++;
			}
			while (array[j] > pivot) {
				j--;
			}
			if (i <= j) {
				swap(i, j, array);
				i++;
				j--;
			}
			if (startIndex < j) {
				quickSort(array, startIndex, j);
			}
			if (i < endIndex) {
				quickSort(array, i, endIndex);
			}
		}
	}

	/**
	 * Method swaps elements in an array.
	 *
	 * @param firstIndex is the integer index of the first element we want to swap
	 * @param secondIndex is the integer index of the second element we want to swap
	 * @param array is the array in which we want to swap the elements
	 */
	private static void swap(int firstIndex, int secondIndex, int[] array) {
		int tempArrayElement = array[firstIndex];
		array[firstIndex] = array[secondIndex];
		array[secondIndex] = tempArrayElement;
	}

	/**
	 * Method returns the middle index based on start and end input indices.
	 *
	 * @param startIndex is an integer starting index
	 * @param endIndex is an integer ending index
	 * @return
	 */
	private static int getMiddleIndex(int startIndex, int endIndex) {
		return startIndex + ((endIndex - startIndex) / 2);
	}
}
