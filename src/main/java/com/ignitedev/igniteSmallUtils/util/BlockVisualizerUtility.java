/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util;

import com.ignitedev.igniteSmallUtils.IgniteSmallUtils;
import com.ignitedev.igniteSmallUtils.util.text.TextUtility;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@UtilityClass
public class BlockVisualizerUtility {

  private final Map<Location, Object> visualizedBlocks = new HashMap<>();

  public void visualize(
      @NonNull Block block, BlockData blockData, String blockName, Plugin plugin) {
    Location location = block.getLocation();
    FallingBlock falling = spawnFallingBlock(location, blockData, blockName);

    for (Player player : block.getWorld().getPlayers()) {
      Bukkit.getScheduler()
          .runTaskLater(
              plugin,
              () -> player.sendBlockChange(block.getLocation(), block.getBlockData()),
              2);
    }
    visualizedBlocks.put(location, falling);
  }

  private FallingBlock spawnFallingBlock(Location location, BlockData blockData, String blockName) {
    World world = location.getWorld();

    if (world == null) {
      return null;
    }
    FallingBlock falling =
        world.spawnFallingBlock(location.clone().add(0.5, 0, 0.5), blockData);

    falling.setDropItem(false);
    falling.setVelocity(new Vector(0, 0, 0));
    falling.setCustomNameVisible(true);

    if (IgniteSmallUtils.isUsingPaper()) {
      falling.customName(TextUtility.colorizeToComponent(blockName));
    } else {
      falling.setCustomName(TextUtility.colorize(blockName));
    }
    falling.setGlowing(true);
    falling.setGravity(false);

    return falling;
  }

  public void stopVisualizing(@NonNull Block block, Plugin plugin) {
    Object fallingBlock = visualizedBlocks.remove(block.getLocation());

    if (fallingBlock instanceof FallingBlock) {
      ((FallingBlock) fallingBlock).remove();
    }
    for (Player player : block.getWorld().getPlayers()) {
      Bukkit.getScheduler()
          .runTaskLater(
              plugin,
              () -> player.sendBlockChange(block.getLocation(), block.getBlockData()),
              1);
    }
  }

  public void stopAll(Plugin plugin) {
    for (Location location : new HashSet<>(visualizedBlocks.keySet())) {
      Block block = location.getBlock();

      if (isVisualized(block)) {
        stopVisualizing(block, plugin);
      }
    }
  }

  public boolean isVisualized(@NonNull Block block) {
    return visualizedBlocks.containsKey(block.getLocation());
  }
}
