package com.ignitedev.igniteSmallUtils.exception;

public class IncorrectEngineException extends Exception {
  public IncorrectEngineException() {
    super("You are trying to run code only executable with Paper Engine");
  }
}
