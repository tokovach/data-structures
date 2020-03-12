package com.mytraining.javatraining.mergesort;

import static com.mytraining.javatraining.mergesort.MergeSort.mergeSort;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MergeSortTest {

	@Test
	void sort_Normal() {
		int[] unsortedArray = { 1, 5, 4, 6, 3, 12, 15, 2 };
		int[] expectedSortedArray = { 1, 2, 3, 4, 5, 6, 12, 15 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_AlreadySorted() {
		int[] unsortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		int[] expectedSortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_ReverseSorted() {
		int[] unsortedArray = { 7, 6, 5, 4, 3, 2, 1, 0 };
		int[] expectedSortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_SomeSameElements() {
		int[] unsortedArray = { 1, 5, 2, 4, 6, 3, 3, 12, 15, 2 };
		int[] expectedSortedArray = { 1, 2, 2, 3, 3, 4, 5, 6, 12, 15 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_AllSameElements() {
		int[] unsortedArray = { 1, 1, 1, 1, 1 };
		int[] expectedSortedArray = { 1, 1, 1, 1, 1 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_OneElement() {
		int[] unsortedArray = { 1 };
		int[] expectedSortedArray = { 1 };
		mergeSort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_ZeroElements() {
		int[] unsortedArray = {};
		int[] expectedSortedArray = {};
		assertDoesNotThrow(() -> mergeSort(unsortedArray));
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}
}