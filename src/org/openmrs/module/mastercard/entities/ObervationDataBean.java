/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.mastercard.entities;

import java.util.Set;

import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;

/**
 *
 */
public class ObervationDataBean {
	
	/**
	 * @return the addresses
	 */
	public Set<PersonAddress> getAddresses() {
		return addresses;
	}
	
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(Set<PersonAddress> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * @return the artNos
	 */
	public String getArtNos() {
		return artNos;
	}
	
	/**
	 * @param artNos the artNos to set
	 */
	public void setArtNos(String artNos) {
		this.artNos = artNos;
	}
	
	/**
	 * @return the partNos
	 */
	public String getPartNos() {
		return partNos;
	}
	
	/**
	 * @param partNos the partNos to set
	 */
	public void setPartNos(String partNos) {
		this.partNos = partNos;
	}
	
	/**
	 * @return the partStart
	 */
	public String getPartStart() {
		return partStart;
	}
	
	/**
	 * @param partStart the partStart to set
	 */
	public void setPartStart(String partStart) {
		this.partStart = partStart;
	}
	
	/**
	 * @return the patientId
	 */
	public String getPatientId() {
		return patientId;
	}
	
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	/**
	 * @return the vhwName
	 */
	public String getVhwName() {
		return vhwName;
	}
	
	/**
	 * @param vhwName the vhwName to set
	 */
	public void setVhwName(String vhwName) {
		this.vhwName = vhwName;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	/**
	 * @return the tbStat
	 */
	public String getTbStat() {
		return tbStat;
	}
	
	/**
	 * @param tbStat the tbStat to set
	 */
	public void setTbStat(String tbStat) {
		this.tbStat = tbStat;
	}
	
	/**
	 * @return the datePlace
	 */
	public String getDatePlace() {
		return datePlace;
	}
	
	/**
	 * @param datePlace the datePlace to set
	 */
	public void setDatePlace(String datePlace) {
		this.datePlace = datePlace;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	
	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * @return the cd4
	 */
	public String getCd4() {
		return cd4;
	}
	
	/**
	 * @param cd4 the cd4 to set
	 */
	public void setCd4(String cd4) {
		this.cd4 = cd4;
	}
	
	/**
	 * @return the cd4P
	 */
	public String getCd4P() {
		return cd4P;
	}
	
	/**
	 * @param cd4p the cd4P to set
	 */
	public void setCd4P(String cd4p) {
		cd4P = cd4p;
	}
	
	/**
	 * @return the ks
	 */
	public String getKs() {
		return ks;
	}
	
	/**
	 * @param ks the ks to set
	 */
	public void setKs(String ks) {
		this.ks = ks;
	}
	
	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}
	
	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	/**
	 * @return the cd4Date
	 */
	public String getCd4Date() {
		return cd4Date;
	}
	
	/**
	 * @param cd4Date the cd4Date to set
	 */
	public void setCd4Date(String cd4Date) {
		this.cd4Date = cd4Date;
	}
	
	/**
	 * @return the preg
	 */
	public String getPreg() {
		return preg;
	}
	
	/**
	 * @param preg the preg to set
	 */
	public void setPreg(String preg) {
		this.preg = preg;
	}
	
	/**
	 * @return the d4TDate
	 */
	public String getD4TDate() {
		return d4TDate;
	}
	
	/**
	 * @param d4tDate the d4TDate to set
	 */
	public void setD4TDate(String d4tDate) {
		d4TDate = d4tDate;
	}
	
	/**
	 * @return the guardianName
	 */
	public String getGuardianName() {
		return guardianName;
	}
	
	/**
	 * @param guardianName the guardianName to set
	 */
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	
	/**
	 * @return the hgt
	 */
	public String getHgt() {
		return hgt;
	}
	
	/**
	 * @param hgt the hgt to set
	 */
	public void setHgt(String hgt) {
		this.hgt = hgt;
	}
	
	/**
	 * @return the wgt
	 */
	public String getWgt() {
		return wgt;
	}
	
	/**
	 * @param wgt the wgt to set
	 */
	public void setWgt(String wgt) {
		this.wgt = wgt;
	}
	
	/**
	 * @return the everArv
	 */
	public String getEverArv() {
		return everArv;
	}
	
	/**
	 * @param everArv the everArv to set
	 */
	public void setEverArv(String everArv) {
		this.everArv = everArv;
	}
	
	/**
	 * @return the alt1stL
	 */
	public String getAlt1stL() {
		return alt1stL;
	}
	
	/**
	 * @param alt1stL the alt1stL to set
	 */
	public void setAlt1stL(String alt1stL) {
		this.alt1stL = alt1stL;
	}
	
	/**
	 * @return the alt1stLDate
	 */
	public String getAlt1stLDate() {
		return alt1stLDate;
	}
	
	/**
	 * @param alt1stLDate the alt1stLDate to set
	 */
	public void setAlt1stLDate(String alt1stLDate) {
		this.alt1stLDate = alt1stLDate;
	}
	
	/**
	 * @return the fup
	 */
	public String getFup() {
		return fup;
	}
	
	/**
	 * @param fup the fup to set
	 */
	public void setFup(String fup) {
		this.fup = fup;
	}
	
	/**
	 * @return the grel
	 */
	public String getGrel() {
		return grel;
	}
	
	/**
	 * @param grel the grel to set
	 */
	public void setGrel(String grel) {
		this.grel = grel;
	}
	
	/**
	 * @return the gphone
	 */
	public String getGphone() {
		return gphone;
	}
	
	/**
	 * @param gphone the gphone to set
	 */
	public void setGphone(String gphone) {
		this.gphone = gphone;
	}
	
	/**
	 * @return the ageInit
	 */
	public String getAgeInit() {
		return ageInit;
	}
	
	/**
	 * @param ageInit the ageInit to set
	 */
	public void setAgeInit(String ageInit) {
		this.ageInit = ageInit;
	}
	
	/**
	 * @return the lastArv
	 */
	public String getLastArv() {
		return lastArv;
	}
	
	/**
	 * @param lastArv the lastArv to set
	 */
	public void setLastArv(String lastArv) {
		this.lastArv = lastArv;
	}
	
	/**
	 * @return the secondL
	 */
	public String getSecondL() {
		return secondL;
	}
	
	/**
	 * @param secondL the secondL to set
	 */
	public void setSecondL(String secondL) {
		this.secondL = secondL;
	}
	
	/**
	 * @return the secondLDate
	 */
	public String getSecondLDate() {
		return secondLDate;
	}
	
	/**
	 * @param secondLDate the secondLDate to set
	 */
	public void setSecondLDate(String secondLDate) {
		this.secondLDate = secondLDate;
	}
	
	/**
	 * @return the unknownObs
	 */
	public String getUnknownObs() {
		return unknownObs;
	}
	
	/**
	 * @param unknownObs the unknownObs to set
	 */
	public void setUnknownObs(String unknownObs) {
		this.unknownObs = unknownObs;
	}
	
	Set<PersonAddress> addresses = null;
	
	private String artNos = Constants.NOT_AVAILABLE;
	
	//Header: todo
	private String partNos = Constants.NOT_AVAILABLE;
	
	private String partStart = Constants.NOT_AVAILABLE;
	
	private String patientId = Constants.NOT_AVAILABLE;
	
	//Header: todo
	private String vhwName = Constants.NOT_AVAILABLE;
	
	private String name = Constants.NOT_AVAILABLE;
	
	private String stage = Constants.NOT_AVAILABLE;
	
	private String tbStat = Constants.NOT_AVAILABLE;
	
	private String datePlace = Constants.NOT_AVAILABLE;
	
	private String type = Constants.NOT_AVAILABLE;
	
	private String sex = Constants.NOT_AVAILABLE;
	
	private String dob = Constants.NOT_AVAILABLE;
	
	private String phone = Constants.NOT_AVAILABLE;
	
	private String cd4 = Constants.NOT_AVAILABLE;
	
	private String cd4P = Constants.NOT_AVAILABLE;
	
	private String ks = Constants.NOT_AVAILABLE;
	
	private String addr = Constants.NOT_AVAILABLE;
	
	private String cd4Date = Constants.NOT_AVAILABLE;
	
	private String preg = Constants.NOT_AVAILABLE;
	
	private String d4TDate = Constants.NOT_AVAILABLE;
	
	private String guardianName = Constants.NOT_AVAILABLE;
	
	private String hgt = Constants.NOT_AVAILABLE;
	
	private String wgt = Constants.NOT_AVAILABLE;
	
	private String everArv = Constants.NOT_AVAILABLE;
	
	private String alt1stL = Constants.NOT_AVAILABLE;
	
	private String alt1stLDate = Constants.NOT_AVAILABLE;
	
	private String fup = Constants.NOT_AVAILABLE;
	
	private String grel = Constants.NOT_AVAILABLE;
	
	private String gphone = Constants.NOT_AVAILABLE;
	
	private String ageInit = Constants.NOT_AVAILABLE;
	
	private String lastArv = Constants.NOT_AVAILABLE;
	
	private String secondL = Constants.NOT_AVAILABLE;
	
	private String secondLDate = Constants.NOT_AVAILABLE;
	
	private String unknownObs = Constants.NOT_AVAILABLE;
	
	// section ConstantIDs
	public int guardianNameConceptID = 2927;
	
	public int guardianName2ConceptID = 2928;
	
	public int fupConceptID = 2552;
	
	public int datePlaceConceptID = 2170;
	
	public int datePlace2ConceptID = 2515;
	
	public int wgtConceptID = 5089;
	
	public int hgtConceptID = 5090;
	
	public int stageConceptID = 1480;
	
	public int pregConceptID = 5272;
	
	public int outcomeConceptID = 2530;
	
	public int regimenConceptID = 2538;
	
	//Check on this- getter/setter missing also
	public int newRegimenConceptID = 2589;
	
	//Break both side effects up to two getter/setter
	public int sideEffectsYesNoConceptID = 2146;
	
	public int sideEffectsStringConceptID = 1297;
	
	public int tbStatusConceptID = 7459;
	
	public int pillCountConceptID = 2540;
	
	//doses missed
	public int noOfArvGivenConceptID = 2929;
	
	public int nextApptConceptID = 5096;
	
	public int igno01Name2ConceptID = 968;
	
	public int igno02Name2ConceptID = 1251;
	
	public int igno03Name2ConceptID = 1620;
	
	public int igno04Name2ConceptID = 1662;
	
	public int igno05Name2ConceptID = 1623;
	
	public int igno06Name2ConceptID = 2122;
	
	public int igno07Name2ConceptID = 2298;
	
	public int igno08Name2ConceptID = 2299;
	
	public int igno09Name2ConceptID = 2520;
	
	public int igno10Name2ConceptID = 2536;
	
	public int igno11Name2ConceptID = 2539;
	
	public int igno12Name2ConceptID = 2541;
	
	public int igno13Name2ConceptID = 2542;
	
	public int igno14Name2ConceptID = 2743;
	
	public int igno15Name2ConceptID = 2922;
	
	public int igno16Name2ConceptID = 2972;
	
	public int igno17Name2ConceptID = 5272;
	
	public int igno18Name2ConceptID = 6784;
	
	public int igno19Name2ConceptID = 6785;
	
}
