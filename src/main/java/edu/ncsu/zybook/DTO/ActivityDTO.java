package edu.ncsu.zybook.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ActivityDTO {
    private List<QuestionDTO> questions;
    private Integer activityId;
}
