package edu.ncsu.zybook.domain.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Question {
    private int activity_id;
    private int content_id;
    private int section_id;
    private int tbook_id;
    private int chapter_id;
    private int question_id;
    private int answer_id;
}
