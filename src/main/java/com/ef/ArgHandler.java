package com.ef;

@FunctionalInterface
public interface ArgHandler {

   <T> T getValue(String strArgVal, Class<T> clazz) throws ArgsException;
}
