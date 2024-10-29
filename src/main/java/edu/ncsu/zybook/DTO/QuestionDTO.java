package edu.ncsu.zybook.DTO;

import edu.ncsu.zybook.domain.model.Answer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDTO {
    private AnswerDTO[] answers;
    private String question;
    private Integer isHidden;
    private Integer answerId;
    private Integer questionId;
    private Integer activityId;
    private Integer contentId;
    private Integer sectionId;
    private Integer chapterId;
    private Integer tbookId;
}
