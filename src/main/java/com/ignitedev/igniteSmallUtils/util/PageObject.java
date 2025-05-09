/*
 * Copyright (c) 2022. Made by 2DevsStudio LLC ( https://2devsstudio.com/ ), using one of our available slaves: IgniteDEV. All rights reserved.
 */

package com.ignitedev.igniteSmallUtils.util;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class PageObject<P> {

  private final List<P> objects;
  private final int objectsCountPerPage;
  private final Map<Integer, List<P>> pages = new HashMap<>();

  public PageObject(Collection<P> objects, int objectsCountPerPage) {
    this.objects = new ArrayList<>();
    this.objectsCountPerPage = objectsCountPerPage;
    this.objects.addAll(objects);
  }

  /**
   * @implNote you need to call that method to load all your pages
   */
  public void loadPages() {
    pages.clear();

    int counter = 0;
    int actualPage = 1;

    List<P> loadedObjects = new ArrayList<>();

    for (P object : objects) {
      counter++;
      if (counter <= objectsCountPerPage) {
        loadedObjects.add(object);
      } else {
        pages.put(actualPage, new ArrayList<>(loadedObjects));
        loadedObjects.clear();
        actualPage++;
        loadedObjects.add(object);
        counter = 1;
      }
    }
    if (!loadedObjects.isEmpty()) {
      pages.put(actualPage, new ArrayList<>(loadedObjects));
    }
  }

  public int getPagesAmount() {
    return pages.size();
  }

  @Nullable
  public List<P> getObjectsFromPage(int page) {
    return pages.get(page);
  }
}
