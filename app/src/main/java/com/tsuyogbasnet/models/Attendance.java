package com.tsuyogbasnet.models;

/**
 *
 * Created by tsuyogbasnet on 18/05/15.
 */
public class Attendance {
    private String studentId;
    private String tutorId;
    private String programmeCode;
    private String subjectCode;
    private String roomCode;
    private String date;
    private String type;

    public void setStudentId(String studentId){this.studentId=studentId;}
    public void setTutorId(String tutorId){this.tutorId=tutorId;}
    public void setProgrammeCode(String programmeCode){this.programmeCode=programmeCode;}
    public void setSubjectCode(String subjectCode){this.subjectCode=subjectCode;}
    public void setRoomCode(String roomCode){this.roomCode=roomCode;}
    public void setDate(String date){this.date=date;}
    public void setType(String type){this.type=type;}

    public String getStudentId(){return this.studentId;}
    public String getTutorId(){return this.tutorId;}
    public String getProgrammeCode(){return this.programmeCode;}
    public String getSubjectCode(){return this.subjectCode;}
    public String getRoomCode(){ return this.roomCode;}
    public String getDate(){ return this.date;}
    public String getType(){return this.type;}


}
