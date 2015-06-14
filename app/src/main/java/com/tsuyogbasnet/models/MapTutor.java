package com.tsuyogbasnet.models;

/**
 * Created by tsuyogbasnet on 14/06/15.
 */
public class MapTutor {
    private long Id;
    private String tagId;
    private String tutorId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
}
