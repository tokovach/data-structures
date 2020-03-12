package com.mytraining.javatraining.mergesort;

/**
 * MergeSort algorithm implementation.
 */
public class MergeSort {

	/**
	 * Recursive method that sorts an array based on the divide and conquer approach.
	 *
	 * @param array is an integer array we want to sort
	 */
	public static void mergeSort(int[] array) {
		int arrayLength = array.length;
		if (arrayLength < 2) {
			return;
		}
		int midLength = arrayLength / 2;
		int[] leftSubArray = new int[midLength];
		int[] rightSubArray = new int[arrayLength - midLength];
		addElements(leftSubArray, array, 0);
		addElements(rightSubArray, array, midLength);
		mergeSort(leftSubArray);
		mergeSort(rightSubArray);
		merge(array, leftSubArray, rightSubArray);
	}

	/**
	 * Method merges two sub arrays in a main array based on their values.
	 *
	 * @param mainArray is the integer array where we merge the sub arrays
	 * @param leftSubArray is an integer sub array which we want to merge
	 * @param rightSubArray is an integer sub array which we want to merge
	 */
	private static void merge(int[] mainArray, int[] leftSubArray, int[] rightSubArray) {
		int leftLength = leftSubArray.length;
		int rightLength = rightSubArray.length;
		int leftIndex = 0;
		int rightIndex = 0;
		int tempIndex = 0;
		while (leftIndex < leftLength && rightIndex < rightLength) {
			if (leftSubArray[leftIndex] <= rightSubArray[rightIndex]) {
				mainArray[tempIndex] = leftSubArray[leftIndex];
				leftIndex++;
			} else {
				mainArray[tempIndex] = rightSubArray[rightIndex];
				rightIndex++;
			}
			tempIndex++;
		}
		while (leftIndex < leftLength) {
			mainArray[tempIndex] = leftSubArray[leftIndex];
			leftIndex++;
			tempIndex++;
		}
		while (rightIndex < rightLength) {
			mainArray[tempIndex] = rightSubArray[rightIndex];
			rightIndex++;
			tempIndex++;
		}
	}

	/**
	 * Method is used to add elements from main array to sub array starting from offset.
	 *
	 * @param subArray is an integer sub array where we want to add elements
	 * @param mainArray is the integer array from which we are adding the numbers
	 * @param offSet is the integer starting index from which we want to add elements from main array
	 */
	private static void addElements(int[] subArray, int[] mainArray, int offSet) {
		int tempSubIndex = 0;
		int tempMainIndex = offSet;
		while (tempSubIndex < subArray.length) {
			subArray[tempSubIndex] = mainArray[tempMainIndex];
			tempMainIndex++;
			tempSubIndex++;
		}
	}
}
