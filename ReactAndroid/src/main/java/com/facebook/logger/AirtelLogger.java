package com.facebook.logger;

import java.lang.reflect.Method;

public class AirtelLogger {

  public static Class errorLogger, breadcrumbLogger;
  public static Method logException, logBreadCrumb;

  static {
    try {
      errorLogger = Class.forName("com.myairtelapp.logging.BugsnagLoggingUtils");
      logException = errorLogger.getDeclaredMethod("logException", Exception.class);
      logException.setAccessible(true);
      breadcrumbLogger = Class.forName("com.myairtelapp.logging.BreadcrumbLoggingUtils");
      logBreadCrumb = breadcrumbLogger.getDeclaredMethod("logBugsnagBreadcrumb", String.class, String.class);
      logBreadCrumb.setAccessible(true);
    } catch (java.lang.Exception ignored) {
    }
  }
}
