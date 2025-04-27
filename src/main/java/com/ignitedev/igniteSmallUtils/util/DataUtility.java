/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util;

import com.ignitedev.igniteSmallUtils.IgniteSmallUtils;
import com.ignitedev.igniteSmallUtils.component.IgniteComponent;
import com.ignitedev.igniteSmallUtils.util.text.Placeholder;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
@UtilityClass
public class DataUtility {

  /**
   * @implNote checking a placeholder list and updating specified data with a specified value
   */
  public List<Placeholder> updateData(
      List<Placeholder> placeholdersToUpdate, String dataToUpdate, String updateValue) {

    AtomicBoolean isDataFound = new AtomicBoolean(false);
    ArrayList<Placeholder> updatedList = new ArrayList<>(placeholdersToUpdate);

    updatedList.forEach(
        placeholder -> {
          String asString = placeholder.getKey().getAsString();

          if (asString != null && asString.equalsIgnoreCase(dataToUpdate)) {
            placeholder.setValue(IgniteComponent.of(updateValue));
            isDataFound.set(true);
          }
        });
    if (!isDataFound.get()) {
      updatedList.add(
          new Placeholder(IgniteComponent.of(dataToUpdate), IgniteComponent.of(updateValue)));
    }

    return updatedList;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public boolean isUUID(String string) {
    try {
      UUID.fromString(string);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  public <T extends Enum<?>> boolean enumsContains(T[] enumValues, String string) {
    for (T enumValue : enumValues) {
      if (enumValue.name().equalsIgnoreCase(string)) {
        return true;
      }
    }
    return false;
  }

  public boolean isBoolean(String string) {
    return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
  }

  public <T> void removeListElements(List<T> list, int elements) {
    if (list.size() <= elements) {
      throw new IllegalArgumentException("Removing more elements than list contains!");
    }
    if (elements > 0) {
      list.subList(0, elements).clear();
    }
  }

  public <T> T getRandomElement(T[] list) {
    return list[ThreadLocalRandom.current().nextInt(list.length)];
  }

  public String[] flatten(String[][] data) {
    List<String> list = new ArrayList<>();
    for (String[] datum : data) {
      list.addAll(Arrays.asList(datum));
    }
    return list.toArray(new String[0]);
  }

  public int[] flatten(int[][] data) {
    int[] arr = new int[totalSize(data)];
    int j = 0;
    for (int[] datum : data) {
      for (int i : datum) {
        arr[j] = i;
        j++;
      }
    }
    return arr;
  }

  public int totalSize(int[][] a2) {
    int result = 0;
    for (int[] a1 : a2) {
      result += a1.length;
    }
    return result;
  }

  public void replaceSignText(Sign sign, String from, String to) {
    if (sign == null) {
      return;
    }

    for (Side value : Side.values()) {
      processSignSide(sign.getSide(value), from, to);
    }
  }

  private void processSignSide(SignSide side, String from, String to) {
    if (IgniteSmallUtils.isUsingPaper()) {
      for (int i = 0; i < side.lines().size(); i++) {
        Component line = side.lines().get(i);
        side.line(i, line.replaceText(value -> value.matchLiteral(from).replacement(to)));
      }
    } else {
      for (int i = 0; i < side.getLines().length; i++) {
        String line = side.getLines()[i];
        side.setLine(i, line.replace(from, to));
      }
    }
  }


  @SafeVarargs
  public <C> List<C> mergeLists(List<C>... lists) {
    List<C> returnList = new ArrayList<>();

    for (List<C> list : lists) {
      returnList.addAll(list);
    }
    return returnList;
  }

  public double round(double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    return (double) Math.round(value * scale) / scale;
  }

  public boolean isCrop(Block block) {
    return block.getState().getBlockData() instanceof Ageable;
  }

  private boolean isDay(World world) {
    long time = world.getTime();
    return time < 12300 || time > 23850;
  }

  /**
   * Convert a millisecond duration to a string format
   *
   * @param millis A duration to convert to a string form
   * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
   */
  public String getDurationBreakdown(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration must be greater than zero!");
    }
    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    return (hours + " Hours " + minutes + " Minutes " + seconds + " Seconds");
  }

  @SneakyThrows
  public void unRegisterBukkitCommand(PluginCommand command, Plugin plugin) {
    Object result =
        ReflectionUtility.getPrivateField(plugin.getServer().getPluginManager(), "commandMap");
    SimpleCommandMap commandMap = (SimpleCommandMap) result;
    Object map = ReflectionUtility.getPrivateField(commandMap, "knownCommands");
    HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;

    knownCommands.remove(command.getName());

    for (String alias : command.getAliases()) {
      if (knownCommands.containsKey(alias)
          && knownCommands.get(alias).toString().contains(plugin.getName())) {
        knownCommands.remove(alias);
      }
    }
  }
}
