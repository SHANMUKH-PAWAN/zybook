package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Section {
    private int sno;
    private String title;
    private boolean isHidden;
    private int chapId;
    private int tbookId;
}
