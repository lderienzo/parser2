package com.ef;

@FunctionalInterface
public interface ArgHandler<T> {

   <T> T getValue(String strArgVal) throws ArgsException;
}
