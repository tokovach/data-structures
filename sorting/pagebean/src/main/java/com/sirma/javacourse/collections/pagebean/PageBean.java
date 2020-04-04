package com.sirma.javacourse.collections.pagebean;

import java.util.List;
/**
 * PageBean implementation that uses subList to show list pages and works with generic types.
 *
 * @author Tomas Kovachev <t.kovachev1996@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class PageBean<T> {
  private int pageSize;
  private List<T> objectList;
  private int currentIndex = 0;
  private int reminder;
  private int currentPageNumber = 0;
  private int listSize;

  /**
   * Constructor that specifies how many elements we want on one page and which list we want to
   * iterate.
   *
   * @param pageSize an integer representing the number of elements we want to see on one page
   * @param objectList is the list we want to iterate
   */
  public PageBean(int pageSize, List<T> objectList) {
    this.pageSize = pageSize;
    this.objectList = objectList;
    listSize = objectList.size();
    reminder = listSize - ((listSize / pageSize) * pageSize);
  }

  /**
   * next method shows the next number of elements as sublist.
   *
   * @return a sublist with the next elements
   */
  public List next() {
    Next hasNext = hasNext();
    if (hasNext == Next.EMPTY_PAGE) {
      return null;
    }
    int tempCurrentIndex = currentIndex;
    currentPageNumber++;
    if (hasNext == Next.FULL_PAGE) {
      currentIndex += pageSize;
    } else {
      currentIndex = listSize;
    }
    return objectList.subList(tempCurrentIndex, currentIndex);
  }

  /**
   * previous method shows the previous number of elements as sublist
   *
   * @return a sublist with previous elements
   */
  public List previous() {
    Previous hasPrevious = hasPrevious();
    if (hasPrevious == Previous.NO_PAGE) {
      return null;
    }
    currentPageNumber--;
    if (hasPrevious == Previous.PRE_LASTPAGE && reminder != 0) {
      currentIndex -= reminder;
    } else {
      currentIndex -= pageSize;
    }
    return objectList.subList((currentIndex - pageSize), currentIndex);
  }

  /**
   * hasNext checks whether there are more elements left after the tracker to be processed as
   * subLists.
   *
   * @return an enum of type Next which depends on the number of elements left to be traversed
   *     forwards
   */
  private Next hasNext() {
    if (currentIndex < (listSize - pageSize)) {
      return Next.FULL_PAGE;
    } else if (currentIndex < listSize) {
      return Next.PARTIAL_PAGE;
    } else {
      return Next.EMPTY_PAGE;
    }
  }

  /**
   * hasPrevious checks whether there are elements left before the tracker to be processed as
   * subLists.
   *
   * @return an enum of type Previous which depends on the number of elements left to be traversed
   *     backwards
   */
  private Previous hasPrevious() {
    if (currentIndex == listSize) {
      return Previous.PRE_LASTPAGE;
    } else if (currentIndex >= pageSize) {
      return Previous.FULL_PAGE;
    } else {
      return Previous.NO_PAGE;
    }
  }

  /**
   * method returns first page of the list.
   *
   * @return a sublist of the first elements of the list
   */
  public List firstPage() {
    currentPageNumber = 1;
    currentIndex = pageSize;
    return objectList.subList(0, pageSize);
  }

  /**
   * method returns the last page of list.
   *
   * @return a sublist of the last elements to be displayed
   */
  public List lastPage() {
    currentIndex = listSize;
    currentPageNumber = getLastPageNumber();
    if (listSize % pageSize != 0) {
      return objectList.subList((currentIndex - reminder), currentIndex);
    } else {
      return objectList.subList((currentIndex - pageSize), currentIndex);
    }
  }

  public int getLastPageNumber() {
    if (listSize % pageSize != 0) {
      return (listSize / pageSize) + 1;
    }
    return (listSize / pageSize);
  }

  public int getCurrentPageNumber() {
    return currentPageNumber;
  }

  /**
   * Previous enum class implementation that is used by the hasPrevious method to return whether
   * there is an entire page, no page or pre-last page to be displayed.
   */
  public enum Previous {
    PRE_LASTPAGE,
    FULL_PAGE,
    NO_PAGE
  }

  /**
   * Next enum class implementation that is used by the hasNext method to return whether there is an
   * entire page, partial page or no page to be displayed.
   */
  public enum Next {
    FULL_PAGE,
    PARTIAL_PAGE,
    EMPTY_PAGE
  }
}
