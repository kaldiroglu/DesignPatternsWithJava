package dev.kaldiroglu.dp.creational.introduction.employee.better;

import java.time.LocalDate;

public class PersonalInfo {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String education;
    private String skills;
    private String previousCompany;

    public PersonalInfo(String firstName, String lastName, LocalDate birthDate,
                        String education, String skills, String previousCompany) {
        this.firstName = (firstName == null) ? "" : firstName;
        this.lastName = (lastName == null) ? "" : lastName;
        this.birthDate = birthDate;
        this.education = (education == null) ? "" : education;
        this.skills = (skills == null) ? "" : skills;
        this.previousCompany = (previousCompany == null) ? "" : previousCompany;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getPreviousCompany() { return previousCompany; }
    public void setPreviousCompany(String previousCompany) { this.previousCompany = previousCompany; }
}