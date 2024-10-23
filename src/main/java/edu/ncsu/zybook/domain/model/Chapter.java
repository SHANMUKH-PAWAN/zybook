package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Chapter {
    private int cno;
    private String chapterCode;
    private String title;
    private boolean isHidden;
    private int  tbookId;
    private List<Section> sections;

}