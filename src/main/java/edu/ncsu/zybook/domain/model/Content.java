package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Content {

    protected boolean isHidden;
    protected String ownedBy;
    protected Integer sectionId;
    protected Integer contentId;
    protected Integer tbook_id;
    protected Integer chapId;
    protected String contentType;
}
