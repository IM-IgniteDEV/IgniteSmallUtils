/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util.text;

import com.ignitedev.igniteSmallUtils.component.IgniteComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Placeholder {

  @Getter
  @Setter
  private IgniteComponent key;
  @Getter
  @Setter
  private IgniteComponent value;

  public static Placeholder replacing(IgniteComponent key, IgniteComponent value) {
    return new Placeholder(key, value);
  }

  public static Placeholder replacing(IgniteComponent key, Number value) {
    if (value == null) {
      return Placeholder.replacing(key, "0");
    }
    return Placeholder.replacing(key, IgniteComponent.of(String.valueOf(value)));
  }

  public static Placeholder replacing(IgniteComponent key, Object value) {
    if (value == null) {
      return Placeholder.replacing(key, "");
    }
    return Placeholder.replacing(key, IgniteComponent.of(value.toString()));
  }

  public IgniteComponent replaceIn(IgniteComponent text) {
    return text.replace(key.getAsString(), value.getAsString());
  }

  public List<IgniteComponent> replaceIn(List<IgniteComponent> text) {
    return text.stream().map(this::replaceIn).collect(Collectors.toList());
  }
}
