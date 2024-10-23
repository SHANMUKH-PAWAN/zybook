package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Answer {
    private int answerId;
    private String answerText;
    private String justification;
    private int sectionId;
    private int contentId;
    private int chapId;
    private  int tbookId;
    private int activityId;
    }
