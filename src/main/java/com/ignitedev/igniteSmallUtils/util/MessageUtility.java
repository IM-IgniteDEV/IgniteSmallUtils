/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util;

import com.ignitedev.igniteSmallUtils.IgniteSmallUtils;
import com.ignitedev.igniteSmallUtils.component.IgniteComponent;
import com.ignitedev.igniteSmallUtils.interfaces.PaperOnly;
import com.ignitedev.igniteSmallUtils.util.text.Placeholder;
import com.ignitedev.igniteSmallUtils.util.text.TextUtility;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
@UtilityClass
public class MessageUtility {

  public void send(Player target, IgniteComponent text, Placeholder... placeholders) {
    String messageString = text.getAsString();
    String placeholdersString = TextUtility.replace(text, placeholders).getAsString();

    if (placeholdersString != null) {
      text = IgniteComponent.of(PlaceholderAPI.setPlaceholders(target, placeholdersString));
    }
    // PAPER CODE
    if (IgniteSmallUtils.isUsingPaper()) {
      Component asComponents = text.getAsComponent();

      if (asComponents != null) {
        target.sendMessage(asComponents);
        return;
      }
    }
    // END OF PAPER CODE
    if (messageString != null) {
      if (messageString.equalsIgnoreCase("{BLANK}")) {
        return;
      }
      target.sendMessage(TextUtility.parseMiniMessage(messageString));
    }
  }

  @SneakyThrows
  @PaperOnly
  public void send(Audience target, IgniteComponent text, Placeholder... placeholders) {
    PaperUtility.checkPaper();
    send(((Player) target), text, placeholders);
  }

  @SneakyThrows
  @PaperOnly
  public void send(Audience target, String text, Placeholder... placeholders) {
    PaperUtility.checkPaper();
    send(target, IgniteComponent.of(text), placeholders);
  }

  public void send(Player target, String text, Placeholder... placeholders) {
    send(target, IgniteComponent.of(text), placeholders);
  }

  @PaperOnly
  @SneakyThrows
  public void send(Audience target, List<String> text, Placeholder... placeholders) {
    PaperUtility.checkPaper();
    text.forEach(message -> send(target, IgniteComponent.of(message), placeholders));
  }

  public void send(Player target, List<String> text, Placeholder... placeholders) {
    text.forEach(message -> send(target, IgniteComponent.of(message), placeholders));
  }

  @SneakyThrows
  @PaperOnly
  public void sendIgniteComponents(
      Audience target, List<IgniteComponent> text, Placeholder... placeholders) {
    PaperUtility.checkPaper();
    text.forEach(message -> send(target, message, placeholders));
  }

  public void sendIgniteComponents(
      Player target, List<IgniteComponent> text, Placeholder... placeholders) {
    text.forEach(message -> send(target, message, placeholders));
  }

  @PaperOnly
  @SneakyThrows
  public void send(Audience target, Component text, Placeholder... placeholders) {
    PaperUtility.checkPaper();

    if (TextUtility.serializeComponent(text).equalsIgnoreCase("{BLANK}")) {
      return;
    }
    if (target instanceof OfflinePlayer offlinePlayer) {
      String placeholdersString =
          TextUtility.replace(IgniteComponent.of(text), placeholders).getAsString();

      if (placeholdersString != null) {
        text =
            TextUtility.parseMiniMessage(
                PlaceholderAPI.setPlaceholders(offlinePlayer, placeholdersString));
      }
    }
    send(target, IgniteComponent.of(text), placeholders);
  }

  @PaperOnly
  @SneakyThrows
  public void sendComponents(Audience target, List<Component> text, Placeholder... placeholders) {
    PaperUtility.checkPaper();
    text.forEach(message -> send(target, message, placeholders));
  }

  public void sendConsole(IgniteComponent text) {
    String messageString = text.getAsString();

    if (messageString != null) {
      Bukkit.getConsoleSender().sendMessage(messageString);
    }
  }

  public void sendConsole(List<IgniteComponent> text) {
    for (IgniteComponent IgniteComponent : text) {
      sendConsole(IgniteComponent);
    }
  }
}
