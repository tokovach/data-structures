package com.mytraining.javatraining.quicksort;

/**
 * QuickSort algorithm implementation that uses starting index as pivot.
 */
public class LeftMostQuickSort {

	/**
	 * Method initializes the starting indices for the quicksort method and calls it.
	 *
	 * @param array is an integer array we want to sort
	 */
	public static void sort(int[] array) {
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
		if (endIndex > startIndex) {
			int partitionIndex = partition(array, startIndex, endIndex);
			quickSort(array, startIndex, partitionIndex - 1);
			quickSort(array, partitionIndex + 1, endIndex);
		}
	}

	/**
	 * Method compares elements in array to pivot element and repositions them.
	 *
	 * @param array is the array we want to sort
	 * @param startIndex is the index from where we start the partitioning
	 * @param endIndex is the index where we stop the partitioning
	 * @return a partition index of the last updated array element
	 */
	private static int partition(int[] array, int startIndex, int endIndex) {
		int pivot = array[startIndex];
		int partitionIndex = endIndex;
		for (int i = endIndex; i > startIndex; i--) {
			if (array[i] >= pivot) {
				swap(i, partitionIndex, array);
				partitionIndex--;
			}
		}
		swap(partitionIndex, startIndex, array);
		return partitionIndex;
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
