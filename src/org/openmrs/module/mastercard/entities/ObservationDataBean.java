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

import org.apache.log4j.Logger;
import org.openmrs.PersonAddress;

/**
 *
 */
public class ObservationDataBean {
	
	static Logger logger = Logger.getLogger(ObservationDataBean.class);
	
	public final static int altConceptID = 654;
	
	public final static int alt1stLineArvsConceptID = 6879;
	
	public final static int arvRegimenTypConceptID = 6882;
	
	public final static int cd4CountConceptID = 5497;
	
	public final static int cd4DateConceptID = 5499;
	
	public final static int cd4PercentageConceptID = 5498;
	
	public final static int cd4PercentageDateTimeConceptID = 7030;
	
	public final static int cd4DateTimeConceptID = 5499;
	
	//datePlace II
	public final static int dateOfHivDiagnososConceptID = 2515;
	
	//not yet used
	public final static int dateAntiretroviralsStartedConceptID = 2516;
	
	//not yet used
	public final static int dateArtLastTakenConceptID = 2516;
	
	public final static int dateofCd4CountConceptID = 3416;
	
	public final static int fupConceptID = 2552;
	
	// section ConstantIDs
	public final static int guardianFirstNameConceptID = 2927;
	
	public final static int guardianLastNameConceptID = 2928;
	
	public final static int hgtConceptID = 5090;
	
	public final static int ksConceptID = 507;
	
	//datePlace I
	public final static int locationWhereTestTookPlaceConceptID = 2170;
	
	//doses missed
	public final static int noOfArvGivenConceptID = 2929;
	
	//Check on this- getter/setter missing also
	public final static int newRegimenConceptID = 2589;
	
	public final static int nextApptConceptID = 5096;
	
	public final static int outcomeConceptID = 2530;
	
	public final static int pillCountConceptID = 2540;
	
	public final static int pregConceptID = 5272;
	
	public final static int regimenConceptID = 2538;
	
	public final static int sexConceptID = 5923;
	
	//Break both side effects up to two getter/setter
	
	public final static int sideEffectsCommentsConceptID = 3332;
	
	public final static int sideEffectsOfTreatmentConceptID = 1581;
	
	public final static int sideEffectsYesNoConceptID = 2146;
	
	public final static int sideEffectsStringConceptID = 1297;
	
	public final static int stageConceptID = 1480;
	
	public final static int tbStatusConceptID = 7459;
	
	public final static int typeConceptID = 6773;
	
	public final static int wgtConceptID = 5089;
	
	//TODO mild: get the ID
	//public final static int cd4PConceptID = null;
	
	public final static int igno01Name2ConceptID = 968;
	
	public final static int igno02Name2ConceptID = 1251;
	
	public final static int igno03Name2ConceptID = 1620;
	
	public final static int igno04Name2ConceptID = 1662;
	
	public final static int igno05Name2ConceptID = 1623;
	
	public final static int igno06Name2ConceptID = 2122;
	
	public final static int igno07Name2ConceptID = 2298;
	
	public final static int igno08Name2ConceptID = 2299;
	
	public final static int igno09Name2ConceptID = 2520;
	
	public final static int igno10Name2ConceptID = 2536;
	
	public final static int igno11Name2ConceptID = 2539;
	
	public final static int igno12Name2ConceptID = 2541;
	
	public final static int igno13Name2ConceptID = 2542;
	
	public final static int igno14Name2ConceptID = 2743;
	
	public final static int igno15Name2ConceptID = 2922;
	
	public final static int igno16Name2ConceptID = 2972;
	
	//pregConceptId
	//public final static int igno17Name2ConceptID = 5272;
	
	public final static int igno18Name2ConceptID = 6784;
	
	public final static int igno19Name2ConceptID = 6785;
	
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
	public Integer getPatientId() {
		return patientId;
	}
	
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Integer patientId) {
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
		return dateOfHiVDiagnosis + " / " + getLocationWhereTestTookPlace();
	}
	
	/**
	 * @param datePlace the datePlace to set
	 */
	public void setDatePlace(String datePlace) {
		throw new NoClassDefFoundError();
	}
	
	/**
	 * @return the locationWhereTestTookPlace
	 */
	public String getLocationWhereTestTookPlace() {
		return locationWhereTestTookPlace;
	}
	
	/**
	 * @param locationWhereTestTookPlace the locationWhereTestTookPlace to set
	 */
	public void setLocationWhereTestTookPlace(String locationWhereTestTookPlace) {
		this.locationWhereTestTookPlace = locationWhereTestTookPlace;
	}
	
	/**
	 * @return the dateOfHiVDiagnosis
	 */
	public String getDateOfHiVDiagnosis() {
		return dateOfHiVDiagnosis;
	}
	
	/**
	 * @param dateOfHiVDiagnosis the dateOfHiVDiagnosis to set
	 */
	public void setDateOfHiVDiagnosis(String dateOfHiVDiagnosis) {
		this.dateOfHiVDiagnosis = dateOfHiVDiagnosis;
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * @param dob the dob to set
	 */
	public void setDateOfBirth(String dob) {
		this.dateOfBirth = dob;
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
	 * @return the cd4Percentage
	 */
	public String getCd4Percentage() {
		return cd4Percentage;
	}
	
	/**
	 * @param cd4Percentage the cd4Percentage to set
	 */
	public void setCd4Percentage(String cd4Percentage) {
		this.cd4Percentage = cd4Percentage;
	}
	
	/**
	 * @return the cd4PercentageDateTime
	 */
	public String getCd4PercentageDateTime() {
		return cd4PercentageDateTime;
	}
	
	/**
	 * @param cd4PercentageDateTime the cd4PercentageDateTime to set
	 */
	public void setCd4PercentageDateTime(String cd4PercentageDateTime) {
		this.cd4PercentageDateTime = cd4PercentageDateTime;
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
		return guardianLastName + " / " + guardianFirstName;
	}
	
	/**
	 * @param guardianName the guardianName to set
	 */
	public void setGuardianName(String guardianName) {
		throw new NoClassDefFoundError();
	}
	
	/**
	 * @return the guardianFirstName
	 */
	public String getGuardianFirstName() {
		return guardianFirstName;
	}
	
	/**
	 * @param guardianFirstName the guardianFirstName to set
	 */
	public void setGuardianFirstName(String guardianFirstName) {
		this.guardianFirstName = guardianFirstName;
	}
	
	/**
	 * @return the guardianLastName
	 */
	public String getGuardianLastName() {
		return guardianLastName;
	}
	
	/**
	 * @param guardianLastName the guardianLastName to set
	 */
	public void setGuardianLastName(String guardianLastName) {
		this.guardianLastName = guardianLastName;
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
	
	/**
	 * @return the sideEffects
	 */
	public String getSideEffects() {
		return sideEffects;
	}
	
	/**
	 * @param sideEffects the sideEffects to set
	 */
	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}
	
	/**
	 * @return the sideEffectsComments
	 */
	public String getSideEffectsComments() {
		return sideEffectsComments;
	}
	
	/**
	 * @param sideEffectsComments the sideEffectsComments to set
	 */
	public void setSideEffectsComments(String sideEffectsComments) {
		logger.warn("Not handled: SideEffectsComments" + sideEffectsComments);
		this.sideEffectsComments = sideEffectsComments;
	}
	
	/**
	 * @return the sideEffectsOfTreatment
	 */
	public String getSideEffectsOfTreatment() {
		return sideEffectsOfTreatment;
	}
	
	/**
	 * @param sideEffectsOfTreatment the sideEffectsOfTreatment to set
	 */
	public void setSideEffectsOfTreatment(String sideEffectsOfTreatment) {
		logger.warn("Not handled: SideEffectsOfTreatment" + sideEffectsOfTreatment);
		this.sideEffectsOfTreatment = sideEffectsOfTreatment;
	}
	
	Set<PersonAddress> addresses = null;
	
	private String artNos = Constants.NOT_AVAILABLE;
	
	//Header: todo
	private String partNos = Constants.NOT_AVAILABLE;
	
	private String partStart = Constants.NOT_AVAILABLE;
	
	private Integer patientId = null;
	
	private String guardianFirstName = Constants.NOT_AVAILABLE;
	
	private String guardianLastName = Constants.NOT_AVAILABLE;
	
	//Header: todo
	private String vhwName = Constants.NOT_AVAILABLE;
	
	private String name = Constants.NOT_AVAILABLE;
	
	private String stage = Constants.NOT_AVAILABLE;
	
	private String tbStat = Constants.NOT_AVAILABLE;
	
	//DatePlace I
	private String locationWhereTestTookPlace = Constants.NOT_AVAILABLE;
	
	//DatePlace II
	private String dateOfHiVDiagnosis = Constants.NOT_AVAILABLE;
	
	private String type = Constants.NOT_AVAILABLE;
	
	private String sex = Constants.NOT_AVAILABLE;
	
	private String dateOfBirth = Constants.NOT_AVAILABLE;
	
	private String phone = Constants.NOT_AVAILABLE;
	
	private String cd4 = Constants.NOT_AVAILABLE;
	
	private String cd4P = Constants.NOT_AVAILABLE;
	
	private String ks = Constants.NOT_AVAILABLE;
	
	private String addr = Constants.NOT_AVAILABLE;
	
	private String cd4Date = Constants.NOT_AVAILABLE;
	
	private String cd4Percentage = Constants.NOT_AVAILABLE;
	
	private String cd4PercentageDateTime = Constants.NOT_AVAILABLE;
	
	private String preg = Constants.NOT_AVAILABLE;
	
	private String d4TDate = Constants.NOT_AVAILABLE;
	
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
	
	private String sideEffects = Constants.NOT_AVAILABLE;
	
	private String sideEffectsComments = Constants.NOT_AVAILABLE;
	
	private String sideEffectsOfTreatment = Constants.NOT_AVAILABLE;
	
	private String unknownObs = Constants.NOT_AVAILABLE;
	
}
