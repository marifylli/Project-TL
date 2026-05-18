package com.unipath.model;

import java.util.Date;
import java.util.List;

public class Thesis {

    private int diplomaticId;
    private int professorId;
    private String title;
    private String description;
    private String prerequisites;
    private int requiredECTS;
    private boolean isAvailable;
    private int maxCandidates;
    private int interestedStudents;
    private Date publicationDate;
    private String requiredSkills;
    private Date lastModifiedDate;


    public Thesis(){
    }

    public Thesis(int professorId, String title, String description,
                  String prerequisites, int requiredECTS,
                  int maxCandidates, String requiredSkills) {
        this.professorId = professorId;
        this.title = title;
        this.description = description;
        this.prerequisites = prerequisites;
        this.requiredECTS = requiredECTS;
        this.maxCandidates = maxCandidates;
        this.requiredSkills = requiredSkills;
        this.isAvailable = true;
        this.publicationDate = new Date();
        this.lastModifiedDate = new Date();
    }

    public int getDiplomaticId()       {
        return diplomaticId;
    }
    public int getProfessorId()        {
        return professorId;
    }
    public String getTitle()           {
        return title;
    }
    public String getDescription()     {
        return description;
    }
    public String getPrerequisites()   {
        return prerequisites;
    }
    public int getRequiredECTS()       {
        return requiredECTS;
    }
    public boolean isAvailable()       {
        return isAvailable;
    }
    public int getMaxCandidates()      {
        return maxCandidates;
    }
    public int getInterestedStudents() {
        return interestedStudents;
    }
    public Date getPublicationDate()   {
        return publicationDate;
    }
    public String getRequiredSkills()  {
        return requiredSkills;
    }
    public Date getLastModifiedDate()  {
        return lastModifiedDate;
    }

    // Setters
    public void setDiplomaticId(int id)            {
        this.diplomaticId = id;
    }
    public void setProfessorId(int id)             {
        this.professorId = id;
    }
    public void setTitle(String title)             {
        this.title = title;
    }
    public void setDescription(String desc)        {
        this.description = desc;
    }
    public void setPrerequisites(String prereq)    {
        this.prerequisites = prereq;
    }
    public void setRequiredECTS(int ects)          {
        this.requiredECTS = ects;
    }
    public void setAvailable(boolean available)    {
        this.isAvailable = available;
    }
    public void setMaxCandidates(int max)          {
        this.maxCandidates = max;
    }
    public void setInterestedStudents(int count)   {
        this.interestedStudents = count;
    }
    public void setPublicationDate(Date date)      {
        this.publicationDate = date;
    }
    public void setRequiredSkills(String skills)   {
        this.requiredSkills = skills;
    }
    public void setLastModifiedDate(Date date)     {
        this.lastModifiedDate = date;
    }






    public void findAllThesis() {}
    public void findThesisDetails() {}
}