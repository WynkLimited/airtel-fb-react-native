package com.facebook.logger;

import java.lang.reflect.Method;

public class AirtelLogger {

  private Class errorLogger,breadcrumbLogger;
  private Method logException, logBreadCrumb;
  private Object breadcrumbLoggerInstance, errorLoggerInstance;

  public AirtelLogger(){
    setupAirtelLogger();
  }

  private void setupAirtelLogger() {
    try {
      errorLogger = Class.forName("com.myairtelapp.logging.BugsnagLoggingUtils");
      errorLoggerInstance = errorLogger.newInstance();
      logException = errorLogger.getDeclaredMethod("logException", Exception.class);
      logException.setAccessible(true);

      breadcrumbLogger = Class.forName("com.myairtelapp.logging.BreadcrumbLoggingUtils");
      breadcrumbLoggerInstance = breadcrumbLogger.newInstance();
      logBreadCrumb = breadcrumbLogger.getDeclaredMethod("logBugsnagBreadcrumb", String.class, String.class);
      logBreadCrumb.setAccessible(true);
    } catch (java.lang.Exception ignored) {
    }
  }

  public Method getLogException() {
    return logException;
  }

  public Method getLogBreadCrumb() {
    return logBreadCrumb;
  }

  public Object getBreadcrumbLoggerInstance() {
    return breadcrumbLoggerInstance;
  }

  public Object getErrorLoggerInstance() {
    return errorLoggerInstance;
  }
}
