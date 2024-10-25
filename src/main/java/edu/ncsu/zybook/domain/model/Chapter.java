package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Chapter {
    private int cno;
    private String chapterCode;
    private String title;
    private boolean isHidden;
    private int  tbookId;
}