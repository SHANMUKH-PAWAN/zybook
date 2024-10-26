package edu.ncsu.zybook.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChapterWeakDTO {
    private int cno;
    private String chapterCode;
    private String title;
    private boolean isHidden;
}
