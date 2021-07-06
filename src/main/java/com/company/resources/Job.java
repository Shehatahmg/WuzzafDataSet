/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.resources;

import java.io.Serializable;

/**
 *
 * @author MIDO
 */
public class Job implements Serializable{
    private static final long serialVersionUID = 1L;
    private String Title;
    private String Company;
    private String Location;
    private String Type;
    private String Level;
    private String YearsExp;
    private String Country;
    private String Skills;

    
    public Job( String newTitle,String newLocation, String newCompany, String newType, String newLevel, String newYearsExp, String newCountry, String newSkills)
{
    this.Title=newTitle;
    this.Location=newLocation;
    this.Company=newCompany;
    this.Type=newType;
    this.Level=newLevel;
    this.YearsExp=newYearsExp;
    this.Country=newCountry;
    this.Skills=newSkills;
		

	}
    @Override
	public String toString() {
		return "Job [Title=" + Title + ", Company=" + Company + ", Location=" + Location + ", Type=" + Type
                        + ", Level=" + Level + ", YearsExp=" + YearsExp + ", Country=" + Country+ ", Skills=" + Skills
				+ "]";
	}
        public String getTitle() {
		return Title;
	}
	public void setTitle(String Title) {
		this.Title = Title;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String Company) {
		this.Company = Company;
	}
        public String getLocation() {
		return Location;
	}
	public void setLocation(String Location) {
		this.Location = Location;
	}
	public String getType() {
		return Type;
	}
	public void setType(String Type) {
		this.Type = Type;
	}
        public String getLevel() {
		return Level;
	}
	public void setLevel(String Level) {
		this.Level = Level;
	}
	public String getYearsExp() {
		return YearsExp;
	}
	public void setYearsExp(String YearsExp) {
		this.YearsExp = YearsExp;
	}
        public String getCountry() {
		return Country;
	}
	public void setCountry(String Country) {
		this.Country = Country;
	}
	public String getSkills() {
		return Skills;
	}
	public void setSkills(String Skills) {
		this.Skills = Skills;
	}
        
    
}
