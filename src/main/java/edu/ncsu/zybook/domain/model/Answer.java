package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Answer {
    private int answerId;
    private int activityId;
    private int contentId;
    private int sectionId;
    private int chapId;
    private int tbookId;
    private String answerText;
    private String justification;
}
