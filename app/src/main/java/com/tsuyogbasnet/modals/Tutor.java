package com.tsuyogbasnet.modals;

/**
 * Created by tsuyogbasnet on 20/04/15.
 */
public class Tutor {
    private String tutorId;
    private String firstName;
    private String lastName;
    private String cell;
    private String email;


    public void setTutorId(String tutorId){this.tutorId=tutorId;}
    public void setFirstName(String firstName){this.firstName=firstName;}
    public void setLastName(String lastName){this.lastName=lastName;}
    public void setCell(String cell){this.cell=cell;}
    public void setEmail(String email){this.email=email;}

    //getters are here.
    public String getTutorId (){return this.tutorId;}
    public String getFirstName() {return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getCell(){return this.cell;}
    public String getEmail(){return  this.email;}
    public String getFullName(){
        String fullName= this.firstName + this.lastName;
        return fullName;
    }

}
