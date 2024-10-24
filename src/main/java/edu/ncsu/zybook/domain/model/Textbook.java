package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Textbook {
  int uid;
  String title;
  List<Chapter> chapters;
}
