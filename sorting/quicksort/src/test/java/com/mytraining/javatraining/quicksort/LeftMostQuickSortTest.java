package com.mytraining.javatraining.quicksort;

import static com.mytraining.javatraining.quicksort.LeftMostQuickSort.sort;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LeftMostQuickSortTest {

	@Test
	void sort_Normal() {
		int[] unsortedArray = { 1, 5, 4, 6, 3, 12, 15, 2 };
		int[] expectedSortedArray = { 1, 2, 3, 4, 5, 6, 12, 15 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_AlreadySorted() {
		int[] unsortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		int[] expectedSortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_ReverseSorted() {
		int[] unsortedArray = { 7, 6, 5, 4, 3, 2, 1, 0 };
		int[] expectedSortedArray = { 0, 1, 2, 3, 4, 5, 6, 7 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_SomeSameElements() {
		int[] unsortedArray = { 1, 5, 2, 4, 6, 3, 3, 12, 15, 2 };
		int[] expectedSortedArray = { 1, 2, 2, 3, 3, 4, 5, 6, 12, 15 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_AllSameElements() {
		int[] unsortedArray = { 1, 1, 1, 1, 1 };
		int[] expectedSortedArray = { 1, 1, 1, 1, 1 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_OneElement() {
		int[] unsortedArray = { 1 };
		int[] expectedSortedArray = { 1 };
		sort(unsortedArray);
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}

	@Test
	void sort_ZeroElements() {
		int[] unsortedArray = {};
		int[] expectedSortedArray = {};
		assertDoesNotThrow(() -> sort(unsortedArray));
		assertArrayEquals(expectedSortedArray, unsortedArray);
	}
}