package edu.ncsu.zybook.domain.model;

import java.util.Objects;

public class Textbook {
  int uid;
  String title;


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return String.format("Textbook[id=%d, title=%s]",uid,title);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Textbook textbook = (Textbook) o;
    return uid == textbook.uid;
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
}
