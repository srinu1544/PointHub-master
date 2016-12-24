package com.pointhub;

/**
 * Created by chowd on 05-12-2016.
 */

public class RegistrationDB {
    String emailID;
    String profileImage;
    String firstName;
    String lastName;
    String mobileNumber;
    String dob;
    String gender;


    public RegistrationDB(String emailID, String profileImage, String firstName, String lastName, String mobileNumber, String dob, String gender){
        this.emailID=emailID;
        this.profileImage=profileImage;
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNumber=mobileNumber;
        this.dob=dob;
        this.gender=gender;
    }

    public String getEmailID() { return emailID; }

    public void setEmailID(String email) {
        this.emailID = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
