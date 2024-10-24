package edu.ncsu.zybook.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Content {

    protected boolean isHidden;
    protected String ownedBy;
    protected int sectionId;
    protected int contentId;
    protected int tbook_id;
    protected int chapId;
    protected List<Activity> activities;
}
