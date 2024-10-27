package edu.ncsu.zybook.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentWeakDTO {
    boolean isHidden;
    String ownedBy;
    Integer sectionId;
    Integer contentId;
    Integer tbook_id;
    Integer chapId;
    String contentType;
}
