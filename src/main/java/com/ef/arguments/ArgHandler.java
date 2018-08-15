/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/23/18 12:00 AM
 */

package com.ef.arguments;

@FunctionalInterface
public interface ArgHandler {

   <T> T getValue(String strArgVal, Class<T> clazz);
}
