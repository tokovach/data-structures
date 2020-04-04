package com.sirma.javacourse.chatcommon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/** Utility class that is used to get current time and date. */
public class Clock {

  /**
   * Method returns the current time in format hours.minutes.seconds.
   *
   * @return a string of the time
   */
  public static String getCurrentTime() {
    return new SimpleDateFormat("HH.mm.ss").format(Calendar.getInstance().getTime());
  }

  /**
   * Method returns the current date in format year.month.date.
   *
   * @return a string of the date
   */
  public static String getCurrentDate() {
    return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
  }
}
