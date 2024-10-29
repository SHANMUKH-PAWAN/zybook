package edu.ncsu.zybook.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentReadDTO {
    boolean hidden;
    String ownedBy;
    Integer sectionId;
    Integer contentId;
    Integer tbookId;
    Integer chapId;
    String contentType;
    String data;
}
