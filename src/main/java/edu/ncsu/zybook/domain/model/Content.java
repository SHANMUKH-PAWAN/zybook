package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Content {

    protected boolean isHidden;
    protected String ownedBy;
    protected int sectionId;
    protected int contentId;
    protected int tbook_id;
    protected int chapId;
    protected String contentType;
}
