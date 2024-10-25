package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Activity {
    private int activityId;
    private int sectionId;
    private int contentId;
    private int chapId;
    private  int tbookId;
}
