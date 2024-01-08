package com.nophasenokill.lib;

import lombok.Value;

/**
 * @author CX无敌
 * 2022-10-29
 */
@Value
public class OldMessageModel {
  String message;

  public OldMessageModel(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public OldMessageModel() {
    this.message = null;
  }
}
