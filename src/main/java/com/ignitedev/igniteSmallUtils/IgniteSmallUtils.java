package com.ignitedev.igniteSmallUtils;

import com.ignitedev.igniteSmallUtils.util.PaperUtility;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class IgniteSmallUtils extends JavaPlugin {

  @Getter
  private static final boolean usingPaper = PaperUtility.checkPaperClass();

  @Override
  public void onEnable() {
    // Plugin startup logic

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
