package com.ef.parser;

@FunctionalInterface
public interface ArgHandler<T> {

   <T> T getValue(String strArgVal) throws ArgsException;
}
