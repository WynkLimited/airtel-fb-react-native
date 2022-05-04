/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.modules.core;

import com.facebook.common.logging.FLog;
import com.facebook.fbreact.specs.NativeExceptionsManagerSpec;
import com.facebook.logger.AirtelLogger;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.JavascriptException;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.devsupport.interfaces.DevSupportManager;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.util.ExceptionDataHelper;
import com.facebook.react.util.JSStackTrace;

@ReactModule(name = ExceptionsManagerModule.NAME)
public class ExceptionsManagerModule extends NativeExceptionsManagerSpec {

  public static final String NAME = "ExceptionsManager";

  private final DevSupportManager mDevSupportManager;

  public ExceptionsManagerModule(DevSupportManager devSupportManager) {
    super(null);
    mDevSupportManager = devSupportManager;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void reportFatalException(String message, ReadableArray stack, double idDouble) {
    int id = (int) idDouble;

    JavaOnlyMap data = new JavaOnlyMap();
    data.putString("message", message);
    data.putArray("stack", stack);
    data.putInt("id", id);
    data.putBoolean("isFatal", true);
    reportException(data);
  }

  @Override
  public void reportSoftException(String message, ReadableArray stack, double idDouble) {
    int id = (int) idDouble;

    JavaOnlyMap data = new JavaOnlyMap();
    data.putString("message", message);
    data.putArray("stack", stack);
    data.putInt("id", id);
    data.putBoolean("isFatal", false);
    reportException(data);
  }

  @Override
  public void reportException(ReadableMap data) {
    String message = data.hasKey("message") ? data.getString("message") : "";
    ReadableArray stack = data.hasKey("stack") ? data.getArray("stack") : Arguments.createArray();
    int id = data.hasKey("id") ? data.getInt("id") : -1;
    boolean isFatal = data.hasKey("isFatal") ? data.getBoolean("isFatal") : false;

    if (mDevSupportManager.getDevSupportEnabled()) {
      boolean suppressRedBox = false;
      if (data.getMap("extraData") != null && data.getMap("extraData").hasKey("suppressRedBox")) {
        suppressRedBox = data.getMap("extraData").getBoolean("suppressRedBox");
      }

      if (!suppressRedBox) {
        mDevSupportManager.showNewJSError(message, stack, id);
      }
    } else {
      String extraDataAsJson = ExceptionDataHelper.getExtraDataAsJson(data);
      if (isFatal) {
        /**
         * Logging exception to bugsnag instead of throwing exception and crashing app
         */
        JavascriptException exception = new JavascriptException(JSStackTrace.format(message, stack));
        exception.setExtraDataAsJson(extraDataAsJson);
        String breadcrumb = "Fatal Exception: " + message;
        if (stack != null && stack.size() > 0) {
          breadcrumb += "\nStack Trace: " + stack.toString();
        }
        try {
          AirtelLogger.getInstance().getLogException().invoke(AirtelLogger.getInstance().getErrorLoggerInstance(), exception);
          AirtelLogger.getInstance().getLogBreadCrumb().invoke(AirtelLogger.getInstance().getBreadcrumbLoggerInstance(), breadcrumb);
        } catch (java.lang.Exception ignored) {}
      } else {
        FLog.e(ReactConstants.TAG, JSStackTrace.format(message, stack));
        if (extraDataAsJson != null) {
          FLog.d(ReactConstants.TAG, "extraData: %s", extraDataAsJson);
        }
      }
    }
  }

  @Override
  public void updateExceptionMessage(
      String title, ReadableArray details, double exceptionIdDouble) {
    int exceptionId = (int) exceptionIdDouble;

    if (mDevSupportManager.getDevSupportEnabled()) {
      mDevSupportManager.updateJSError(title, details, exceptionId);
    }
  }

  @Override
  public void dismissRedbox() {
    if (mDevSupportManager.getDevSupportEnabled()) {
      mDevSupportManager.hideRedboxDialog();
    }
  }
}
