package com.facebook.logger;

import androidx.annotation.Keep;

import java.lang.reflect.Method;
import java.lang.Class;

@Keep
public class AirtelLogger {
  public static Class logger, breadcrumbLogger;
  public static Method logException, logBreadCrumb;

  public static void setUpAirtelLogger() {
    try {
      logger = Class.forName("com.myairtelapp.logging.BugsnagLoggingUtils");
      logException = logger.getDeclaredMethod("logException", Exception.class);
      logException.setAccessible(true);
      breadcrumbLogger = Class.forName("com.myairtelapp.logging.BreadcrumbLoggingUtils");
      logBreadCrumb = breadcrumbLogger.getDeclaredMethod("logBugsnagBreadcrumb", String.class, String.class);
      logBreadCrumb.setAccessible(true);
    } catch (java.lang.Exception e) {
    }
  }
}
