package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Section {
    private int sno;
    private String title;
    private boolean isHidden;
    private int chapId;
    private int tbookId;
    private List<Content> contents;
}
