/**
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @flow strict
 * @format
 */

// These annotations are copy/pasted from the built-in Flow definitions for
// Native Promises with some non-standard APIs added in
declare class Promise<+R> {
  constructor(
    callback: (
      resolve: (result?: Promise<R> | R) => void,
      reject: (error?: any) => void,
    ) => mixed,
  ): void;

  then<U>(
    onFulfill?: ?(value: R) => Promise<U> | ?U,
    onReject?: ?(error: any) => Promise<U> | ?U,
  ): Promise<U>;

  catch<U>(onReject?: (error: any) => ?Promise<U> | U): Promise<U>;

  static resolve<T>(object?: Promise<T> | T): Promise<T>;
  static reject<T>(error?: any): Promise<T>;

  static all<T: Iterable<mixed>>(
    promises: T,
  ): Promise<$TupleMap<T, typeof $await>>;
  static race<T>(promises: Array<Promise<T>>): Promise<T>;

  // Non-standard APIs

  // See https://github.com/facebook/fbjs/blob/master/packages/fbjs/src/__forks__/Promise.native.js#L21
  finally<U>(onFinally?: ?(value: any) => Promise<U> | U): Promise<U>;

  done<U>(
    onFulfill?: ?(value: R) => mixed,
    onReject?: ?(error: any) => mixed,
  ): void;

  static cast<T>(object?: T): Promise<T>;
}
