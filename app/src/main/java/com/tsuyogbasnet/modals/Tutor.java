package com.tsuyogbasnet.modals;

/**
 * Created by tsuyogbasnet on 20/04/15.
 */
public class Tutor {
    private String tutorId;
    private String tutorFirstName;
    private String tutorLastName;
    private String tutorCell;
    private String tutorEmail;


    public void setTutorId(String tutorId){this.tutorId=tutorId;}
    public void setFirstName(String firstName){this.tutorFirstName=firstName;}
    public void setLastName(String lastName){this.tutorLastName=lastName;}
    public void setCell(String cell){this.tutorCell=cell;}
    public void setEmail(String email){this.tutorEmail=email;}

    //getters are here.
    public String getTutorId (){return this.tutorId;}
    public String getFirstName() {return this.tutorFirstName;}
    public String getLastName(){return this.tutorLastName;}
    public String getCell(){return this.tutorCell;}
    public String getEmail(){return  this.tutorEmail;}
    public String getFullName(){
        String fullName= this.tutorFirstName + this.tutorLastName;
        return fullName;
    }

}
