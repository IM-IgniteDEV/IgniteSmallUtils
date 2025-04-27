/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.component;

import com.ignitedev.igniteSmallUtils.IgniteSmallUtils;
import com.ignitedev.igniteSmallUtils.interfaces.PaperOnly;
import com.ignitedev.igniteSmallUtils.util.PaperUtility;
import com.ignitedev.igniteSmallUtils.util.text.TextUtility;
import lombok.*;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@ToString
public class IgniteComponent {

  @NotNull
  @Singular(value = "string")
  private List<String> strings;

  @NotNull
  @Singular(value = "component")
  @PaperOnly
  private List<Component> components;

  @SneakyThrows
  @PaperOnly
  public IgniteComponent(ListComponents components) {
    PaperUtility.checkPaper();
    this.components = components.get();
    this.strings = TextUtility.serializeComponent(components.get());
  }

  public IgniteComponent(ListStrings strings) {
    this.strings = strings.get();
    if (IgniteSmallUtils.isUsingPaper()) {
      this.components = TextUtility.colorizeToComponent(strings.get());
    }
  }

  public IgniteComponent(String string) {
    this.strings = new ArrayList<>(List.of(string));
    if (IgniteSmallUtils.isUsingPaper()) {
      this.components = new ArrayList<>(List.of(TextUtility.colorizeToComponent(string)));
    }
  }

  @SneakyThrows
  @PaperOnly
  public IgniteComponent(Component component) {
    PaperUtility.checkPaper();
    this.components = new ArrayList<>(List.of(component));
    this.strings = TextUtility.serializeComponent(new ArrayList<>(List.of(component)));
  }

  @NotNull
  public static IgniteComponent of(@NotNull String string) {
    return new IgniteComponent(string);
  }

  @SneakyThrows
  @NotNull
  @PaperOnly
  public static IgniteComponent of(@NotNull Component component) {
    PaperUtility.checkPaper();
    return new IgniteComponent(component);
  }

  public IgniteComponent replace(String from, String to) {
    List<String> replacedStrings = new LinkedList<>();

    this.strings.forEach(string -> replacedStrings.add(string.replace(from, to)));
    this.strings = replacedStrings;

    if (IgniteSmallUtils.isUsingPaper()) {
      List<Component> replacedComponents = new LinkedList<>();

      this.components.forEach(
          component ->
              replacedComponents.add(
                  component.replaceText((value) -> value.matchLiteral(from).replacement(to))));
      this.components = replacedComponents;
    }
    return this;
  }

  @Nullable
  public List<String> getAsStrings() {
    fetch();

    if (!strings.isEmpty()) {
      return TextUtility.colorize(strings);
    } else if (!components.isEmpty()) {
      if (IgniteSmallUtils.isUsingPaper()) {
        return TextUtility.serializeComponent(components);
      }
    }
    return null;
  }

  @Nullable
  public String getAsString() {
    fetch();
    List<String> asStrings = getAsStrings();

    if (asStrings != null && !asStrings.isEmpty()) {
      return asStrings.getFirst();
    }
    return null;
  }

  @SneakyThrows
  @Nullable
  @PaperOnly
  public Component getAsComponent() {
    PaperUtility.checkPaper();
    fetch();
    List<Component> asComponents = getAsComponents();

    if (asComponents != null && !asComponents.isEmpty()) {
      return asComponents.getFirst();
    }
    return null;
  }

  @Nullable
  @PaperOnly
  @SneakyThrows
  public List<Component> getAsComponents() {
    PaperUtility.checkPaper();
    fetch();

    if (!strings.isEmpty()) {
      return TextUtility.colorizeToComponent(strings);
    } else if (!components.isEmpty()) {
      return components;
    }
    return null;
  }

  @SneakyThrows
  @PaperOnly
  public void fetch() {
    PaperUtility.checkPaper();
    // paper code
    if (IgniteSmallUtils.isUsingPaper()) {
      if (this.components.isEmpty() && !this.strings.isEmpty()) {
        this.components = TextUtility.colorizeToComponent(this.strings);
      }
      if (this.strings.isEmpty() && !this.components.isEmpty()) {
        this.strings = TextUtility.serializeComponent(this.components);
      }
    }
  }

  public interface ListStrings extends Supplier<List<String>> {
  }

  public interface ListComponents extends Supplier<List<Component>> {
  }
}
