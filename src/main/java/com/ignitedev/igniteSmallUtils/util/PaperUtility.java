/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util;

import com.ignitedev.igniteSmallUtils.IgniteSmallUtils;
import com.ignitedev.igniteSmallUtils.exception.IncorrectEngineException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaperUtility {

  public void checkPaper() throws IncorrectEngineException {
    if (!IgniteSmallUtils.isUsingPaper()) {
      throw new IncorrectEngineException();
    }
  }

  public boolean checkPaperClass() {
    try {
      Class.forName("com.destroystokyo.paper.ClientOption");
      return true;
    } catch (ClassNotFoundException ignored) {
      return false;
    }
  }
}
