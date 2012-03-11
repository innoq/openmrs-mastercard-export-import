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

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openmrs.PersonAddress;
import org.openmrs.module.mastercard.Helper;

/**
 *
 */
public class GeneralEncounterDataContainer {
	
	static Logger logger = Logger.getLogger(GeneralEncounterDataContainer.class);
	
	public final static int altConceptID = 654;
	
	public final static int alt1stLineArvsConceptID = 6879;
	
	public final static int arvRegimenTypConceptID = 6882;
	
	public final static int cd4CountConceptID = 5497;
	
	public final static int cd4DateConceptID = 5499;
	
	public final static int cd4PercentageConceptID = 5498;
	
	public final static int cd4PercentageDateTimeConceptID = 7030;
	
	public final static int commentsAtConclusionOfExaminationConceptID = 1620;
	
	public final static int cptGivenConceptID = 3590;
	
	public final static int cptDateConceptID = 7024;
	
	public final static int cd4DateTimeConceptID = 5499;
	
	//datePlace II
	public final static int dateOfHivDiagnososConceptID = 2515;
	
	public final static int dosesMissedConceptId = 2973;
	
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
	
	public final static int nextAppointmentConceptID = 5096;
	
	public final static int outcomeConceptID = 2530;
	
	public final static int phoneNumberCountConceptID = 1426;
	
	public final static int phoneTypeCountConceptID = 2613;
	
	public final static int pillCountConceptID = 2540;
	
	public final static int pregConceptID = 5272;
	
	/**
	 * @return the arvRegimen
	 */
	public String getArvRegimen() {
		return arvRegimen;
	}
	
	/**
	 * @param arvRegimen the arvRegimen to set
	 */
	public void setArvRegimen(String arvRegimen) {
		this.arvRegimen = arvRegimen;
	}
	
	/**
	 * @return the arvDrugsReceived
	 */
	public String getArvDrugsReceived() {
		return arvDrugsReceived;
	}
	
	/**
	 * @param arvDrugsReceived the arvDrugsReceived to set
	 */
	public void setArvDrugsReceived(String arvDrugsReceived) {
		this.arvDrugsReceived = arvDrugsReceived;
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
	
	//regimen
	public final static int statusOfArvRegimen = 2538;
	
	//new Regimen
	public final static int arvDrugsReceivedConceptID = 2589;
	
	public final static int sexConceptID = 5923;
	
	//Break both side effects up to two getter/setter
	
	public final static int sideEffectsCommentsConceptID = 3332;
	
	public final static int sideEffectsOfTreatmentConceptID = 1581;
	
	public final static int sideEffectsYesNoConceptID = 2146;
	
	public final static int sideEffectsStringConceptID = 1297;
	
	public final static int stageConceptID = 1480;
	
	public final static int tbStatusConceptID = 7459;
	
	public final static int typeConceptID = 6773;
	
	public final static int vhwProgramConceptID = 3568;
	
	public final static int wgtConceptID = 5089;
	
	//TODO mild: get the ID
	//public final static int cd4PConceptID = null;
	
	public final static int igno01DateOfLastMenstrualBlood2ConceptID = 968;
	
	public final static int igno02ReasonAntiretroviralsStarted2ConceptID = 1251;
	
	public final static int igno03Name2ConceptID = 1620;
	
	public final static int igno04CommentsAtConclustionOfExamination2ConceptID = 1662;
	
	public final static int igno05IsOnCpt2ConceptID = 1623;
	
	public final static int igno06GuardianPresent2ConceptID = 2122;
	
	public final static int igno07LastNameCommunityHealthWorkerReportedConceptID = 2298;
	
	public final static int igno07FirstNameCommunityHealthWorkerReportedConceptID = 2299;
	
	public final static int igno09HiVDiagnososConstructConceptID = 2520;
	
	public final static int igno10TransferInConceptID = 2536;
	
	public final static int igno11GoesToWorkOrSchoolConceptID = 2539;
	
	public final static int igno12Name2ConceptID = 2541;
	
	public final static int igno13TinsOfAntiretroviralsGivenToPatientConceptID = 2542;
	
	public final static int igno14WhoStageCriteriaPresentConceptID = 2743;
	
	public final static int igno15DataClerkCommentsConceptID = 2922;
	
	public final static int igno16LikuniPhalaGivenToPatientConceptID = 2972;
	
	//pregConceptId
	//public final static int igno17Name2ConceptID = 5272;
	
	public final static int igno18AppointmentReasonOrTypeConceptID = 6784;
	
	public final static int igno19AppointmentSetConceptID = 6785;
	
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
		return patientGivenName + "/" + patientFamilyName;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		String[] nameElementArrays = name.split("/");
		this.patientGivenName = nameElementArrays[0];
		this.patientFamilyName = nameElementArrays[1];
	}
	
	/**
	 * @return the nextAppointment
	 */
	public String getNextAppointment() {
		return nextAppointment;
	}
	
	/**
	 * @param nextAppointment the nextAppointment to set
	 */
	public void setNextAppointment(String nextAppointment) {
		this.nextAppointment = nextAppointment;
	}
	
	/**
	 * @return the noOfArvGiven
	 */
	public Double getNoOfArvGiven() {
		return noOfArvGiven;
	}
	
	public String getNoOfArvGivenAsString() {
		if (noOfArvGiven != null)
			return noOfArvGiven.toString();
		else
			return Constants.NOT_AVAILABLE;
	}
	
	/**
	 * @param noOfArvGiven the noOfArvGiven to set
	 */
	public void setNoOfArvGiven(Double noOfArvGiven) {
		this.noOfArvGiven = noOfArvGiven;
	}
	
	public void setNoOfArvGiven(String noOfArvGiven) {
		if (!Constants.NOT_AVAILABLE.equals(noOfArvGiven)) {
			this.noOfArvGiven = Double.valueOf(noOfArvGiven);
		} else {
			this.noOfArvGiven = null;
		}
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
		return dateOfHiVDiagnosis + "/" + locationWhereTestTookPlace;
	}
	
	/**
	 * @param datePlace the datePlace to set
	 */
	public void setDatePlace(String datePlace) {
		throw new NoClassDefFoundError();
	}
	
	/**
	 * @return the dosesMissed
	 */
	public String getDosesMissed() {
		return dosesMissed;
	}
	
	/**
	 * @param dosesMissed the dosesMissed to set
	 */
	public void setDosesMissed(String dosesMissed) {
		this.dosesMissed = dosesMissed;
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * @return the dob
	 */
	public String getDateOfBirthAsString() {
		return Helper.getStringFromDate(dateOfBirth);
	}
	
	public void setDateOfBirth(Date dob) {
		this.dateOfBirth = dob;
	}
	
	public void setDateOfBirth(String dob) {
		this.dateOfBirth = Helper.getDateFromString(dob);
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
		return guardianFirstName + "/" + guardianLastName;
	}
	
	/**
	 * @param guardianName the guardianName to set
	 */
	public void setGuardianName(String guardianName) {
		String[] nameElementArrays = guardianName.split("/");
		this.guardianFirstName = nameElementArrays[0];
		this.guardianLastName = nameElementArrays[1];
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
	public String getHgtAsString() {
		if (hgt == null)
			return Constants.NOT_AVAILABLE;
		else
			return hgt.toString();
	}
	
	/**
	 * @param hgt the hgt to set
	 */
	public void setHgt(Double hgt) {
		this.hgt = hgt;
	}
	
	/**
	 * @param hgt the hgt to set
	 */
	public void setHgtAsString(String s) {
		this.hgt = Helper.getNumericFromString(s);
	}
	
	/**
	 * @return the wgt
	 */
	public Double getWgt() {
		return wgt;
	}
	
	/**
	 * @return the hgt
	 */
	public Double getHgt() {
		return hgt;
	}
	
	/**
	 * @return the wgt
	 */
	public String getWgtAsString() {
		if (wgt == null)
			return Constants.NOT_AVAILABLE;
		else
			return wgt.toString();
	}
	
	/**
	 * @param wgt the wgt to set
	 */
	public void setWgt(Double wgt) {
		this.wgt = wgt;
	}
	
	/**
	 * @param wgt the wgt to set
	 */
	public void setWgtAsString(String s) {
		this.wgt = Helper.getNumericFromString(s);
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
	public String getSideEffectsYesNo() {
		return sideEffects;
	}
	
	/**
	 * @param sideEffects the sideEffects to set
	 */
	public void setSideEffectsYesNo(String sideEffects) {
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
	
	private String addr = Constants.NOT_AVAILABLE;
	
	private String alt1stL = Constants.NOT_AVAILABLE;
	
	private String alt1stLDate = Constants.NOT_AVAILABLE;
	
	private String artNos = Constants.NOT_AVAILABLE;
	
	private String cd4 = Constants.NOT_AVAILABLE;
	
	private String cd4P = Constants.NOT_AVAILABLE;
	
	private String cd4Date = Constants.NOT_AVAILABLE;
	
	private String cd4Percentage = Constants.NOT_AVAILABLE;
	
	private String cd4PercentageDateTime = Constants.NOT_AVAILABLE;
	
	private String comment = Constants.NOT_AVAILABLE;
	
	/**
	 * @return the cp4tGivem
	 */
	public String getCp4tGiven() {
		return cp4tGiven;
	}
	
	public String getCp4tGivenAsString() {
		if (cp4tGiven != null)
			return cp4tGiven.toString();
		else
			return Constants.NOT_AVAILABLE;
	}
	
	/**
	 * @param cp4tGivem the cp4tGivem to set
	 */
	public void setCp4tGiven(String cp4tGiven) {
		this.cp4tGiven = cp4tGiven;
	}
	
	/**
	 * @return the cp4TDate
	 */
	public String getCp4TDate() {
		return cp4TDate;
	}
	
	/**
	 * @param cp4tDate the cp4TDate to set
	 */
	public void setCp4TDate(String cp4tDate) {
		cp4TDate = cp4tDate;
	}
	
	private String cp4tGiven = Constants.NOT_AVAILABLE;
	
	private String cp4TDate = Constants.NOT_AVAILABLE;
	
	private String d4TDate = Constants.NOT_AVAILABLE;
	
	private Date dateOfBirth = null;
	
	//DatePlace II
	private String dateOfHiVDiagnosis = Constants.NOT_AVAILABLE;
	
	private String dosesMissed = Constants.NOT_AVAILABLE;
	
	private String guardianFirstName = Constants.NOT_AVAILABLE;
	
	private String guardianLastName = Constants.NOT_AVAILABLE;
	
	private String ks = Constants.NOT_AVAILABLE;
	
	//DatePlace I
	private String locationWhereTestTookPlace = Constants.NOT_AVAILABLE;
	
	private String nextAppointment = Constants.NOT_AVAILABLE;
	
	private Double noOfArvGiven = null;
	
	private String outcome = Constants.NOT_AVAILABLE;
	
	/**
	 * @return the outcome
	 */
	public String getOutcome() {
		return outcome;
	}
	
	/**
	 * @param outcome the outcome to set
	 */
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	/**
	 * @return the patientGivenName
	 */
	public String getPatientGivenName() {
		return patientGivenName;
	}
	
	/**
	 * @param patientGivenName the patientGivenName to set
	 */
	public void setPatientGivenName(String patientGivenName) {
		this.patientGivenName = patientGivenName;
	}
	
	/**
	 * @return the patientFamilyName
	 */
	public String getPatientFamilyName() {
		return patientFamilyName;
	}
	
	/**
	 * @param patientFamilyName the patientFamilyName to set
	 */
	public void setPatientFamilyName(String patientFamilyName) {
		this.patientFamilyName = patientFamilyName;
	}
	
	private String patientGivenName = Constants.NOT_AVAILABLE;
	
	private String patientFamilyName = Constants.NOT_AVAILABLE;
	
	private String partNos = Constants.NOT_AVAILABLE;
	
	private String partStart = Constants.NOT_AVAILABLE;
	
	private Integer patientId = null;
	
	private Double pillCount = null;
	
	/**
	 * @return the pillCount
	 */
	public Double getPillCount() {
		return pillCount;
	}
	
	public String getPillCountAsString() {
		if (pillCount != null)
			return pillCount.toString();
		else
			return Constants.NOT_AVAILABLE;
	}
	
	/**
	 * @param pillCount the pillCount to set
	 */
	public void setPillCount(Double pillCount) {
		this.pillCount = pillCount;
	}
	
	public void setPillCount(String pillCount) {
		if (!pillCount.equals(Constants.NOT_AVAILABLE))
			this.pillCount = Double.valueOf(pillCount);
		else
			this.pillCount = null;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		if (comment == null) {
			this.comment = comment;
		}
	}
	
	private String phone = Constants.NOT_AVAILABLE;
	
	private String preg = Constants.NOT_AVAILABLE;
	
	private String sex = Constants.NOT_AVAILABLE;
	
	private String stage = Constants.NOT_AVAILABLE;
	
	private String tbStat = Constants.NOT_AVAILABLE;
	
	private String vhwName = Constants.NOT_AVAILABLE;
	
	private Double hgt = null;
	
	private Double wgt = null;
	
	private String everArv = Constants.NOT_AVAILABLE;
	
	private String fup = Constants.NOT_AVAILABLE;
	
	private String type = Constants.NOT_AVAILABLE;
	
	private String grel = Constants.NOT_AVAILABLE;
	
	private String gphone = Constants.NOT_AVAILABLE;
	
	private String ageInit = Constants.NOT_AVAILABLE;
	
	private String lastArv = Constants.NOT_AVAILABLE;
	
	private String arvRegimen = Constants.NOT_AVAILABLE;
	
	private String arvDrugsReceived = Constants.NOT_AVAILABLE;
	
	private String secondL = Constants.NOT_AVAILABLE;
	
	private String secondLDate = Constants.NOT_AVAILABLE;
	
	private String sideEffects = Constants.NOT_AVAILABLE;
	
	private String sideEffectsComments = Constants.NOT_AVAILABLE;
	
	private String sideEffectsOfTreatment = Constants.NOT_AVAILABLE;
	
	private String unknownObs = Constants.NOT_AVAILABLE;

	private String openMrsId;

    public void setOpenMrsId(String openMrsId) {
    	this.openMrsId = openMrsId;
    }

	
    public static Logger getLogger() {
    	return logger;
    }

	
    public static int getAltconceptid() {
    	return altConceptID;
    }

	
    public static int getAlt1stlinearvsconceptid() {
    	return alt1stLineArvsConceptID;
    }

	
    public static int getArvregimentypconceptid() {
    	return arvRegimenTypConceptID;
    }

	
    public static int getCd4countconceptid() {
    	return cd4CountConceptID;
    }

	
    public static int getCd4dateconceptid() {
    	return cd4DateConceptID;
    }

	
    public static int getCd4percentageconceptid() {
    	return cd4PercentageConceptID;
    }

	
    public static int getCd4percentagedatetimeconceptid() {
    	return cd4PercentageDateTimeConceptID;
    }

	
    public static int getCommentsatconclusionofexaminationconceptid() {
    	return commentsAtConclusionOfExaminationConceptID;
    }

	
    public static int getCptgivenconceptid() {
    	return cptGivenConceptID;
    }

	
    public static int getCptdateconceptid() {
    	return cptDateConceptID;
    }

	
    public static int getCd4datetimeconceptid() {
    	return cd4DateTimeConceptID;
    }

	
    public static int getDateofhivdiagnososconceptid() {
    	return dateOfHivDiagnososConceptID;
    }

	
    public static int getDosesmissedconceptid() {
    	return dosesMissedConceptId;
    }

	
    public static int getDateantiretroviralsstartedconceptid() {
    	return dateAntiretroviralsStartedConceptID;
    }

	
    public static int getDateartlasttakenconceptid() {
    	return dateArtLastTakenConceptID;
    }

	
    public static int getDateofcd4countconceptid() {
    	return dateofCd4CountConceptID;
    }

	
    public static int getFupconceptid() {
    	return fupConceptID;
    }

	
    public static int getGuardianfirstnameconceptid() {
    	return guardianFirstNameConceptID;
    }

	
    public static int getGuardianlastnameconceptid() {
    	return guardianLastNameConceptID;
    }

	
    public static int getHgtconceptid() {
    	return hgtConceptID;
    }

	
    public static int getKsconceptid() {
    	return ksConceptID;
    }

	
    public static int getLocationwheretesttookplaceconceptid() {
    	return locationWhereTestTookPlaceConceptID;
    }

	
    public static int getNoofarvgivenconceptid() {
    	return noOfArvGivenConceptID;
    }

	
    public static int getNewregimenconceptid() {
    	return newRegimenConceptID;
    }

	
    public static int getNextappointmentconceptid() {
    	return nextAppointmentConceptID;
    }

	
    public static int getOutcomeconceptid() {
    	return outcomeConceptID;
    }

	
    public static int getPhonenumbercountconceptid() {
    	return phoneNumberCountConceptID;
    }

	
    public static int getPhonetypecountconceptid() {
    	return phoneTypeCountConceptID;
    }

	
    public static int getPillcountconceptid() {
    	return pillCountConceptID;
    }

	
    public static int getPregconceptid() {
    	return pregConceptID;
    }

	
    public static int getStatusofarvregimen() {
    	return statusOfArvRegimen;
    }

	
    public static int getArvdrugsreceivedconceptid() {
    	return arvDrugsReceivedConceptID;
    }

	
    public static int getSexconceptid() {
    	return sexConceptID;
    }

	
    public static int getSideeffectscommentsconceptid() {
    	return sideEffectsCommentsConceptID;
    }

	
    public static int getSideeffectsoftreatmentconceptid() {
    	return sideEffectsOfTreatmentConceptID;
    }

	
    public static int getSideeffectsyesnoconceptid() {
    	return sideEffectsYesNoConceptID;
    }

	
    public static int getSideeffectsstringconceptid() {
    	return sideEffectsStringConceptID;
    }

	
    public static int getStageconceptid() {
    	return stageConceptID;
    }

	
    public static int getTbstatusconceptid() {
    	return tbStatusConceptID;
    }

	
    public static int getTypeconceptid() {
    	return typeConceptID;
    }

	
    public static int getVhwprogramconceptid() {
    	return vhwProgramConceptID;
    }

	
    public static int getWgtconceptid() {
    	return wgtConceptID;
    }

	
    public static int getIgno01dateoflastmenstrualblood2conceptid() {
    	return igno01DateOfLastMenstrualBlood2ConceptID;
    }

	
    public static int getIgno02reasonantiretroviralsstarted2conceptid() {
    	return igno02ReasonAntiretroviralsStarted2ConceptID;
    }

	
    public static int getIgno03name2conceptid() {
    	return igno03Name2ConceptID;
    }

	
    public static int getIgno04commentsatconclustionofexamination2conceptid() {
    	return igno04CommentsAtConclustionOfExamination2ConceptID;
    }

	
    public static int getIgno05isoncpt2conceptid() {
    	return igno05IsOnCpt2ConceptID;
    }

	
    public static int getIgno06guardianpresent2conceptid() {
    	return igno06GuardianPresent2ConceptID;
    }

	
    public static int getIgno07lastnamecommunityhealthworkerreportedconceptid() {
    	return igno07LastNameCommunityHealthWorkerReportedConceptID;
    }

	
    public static int getIgno07firstnamecommunityhealthworkerreportedconceptid() {
    	return igno07FirstNameCommunityHealthWorkerReportedConceptID;
    }

	
    public static int getIgno09hivdiagnososconstructconceptid() {
    	return igno09HiVDiagnososConstructConceptID;
    }

	
    public static int getIgno10transferinconceptid() {
    	return igno10TransferInConceptID;
    }

	
    public static int getIgno11goestoworkorschoolconceptid() {
    	return igno11GoesToWorkOrSchoolConceptID;
    }

	
    public static int getIgno12name2conceptid() {
    	return igno12Name2ConceptID;
    }

	
    public static int getIgno13tinsofantiretroviralsgiventopatientconceptid() {
    	return igno13TinsOfAntiretroviralsGivenToPatientConceptID;
    }

	
    public static int getIgno14whostagecriteriapresentconceptid() {
    	return igno14WhoStageCriteriaPresentConceptID;
    }

	
    public static int getIgno15dataclerkcommentsconceptid() {
    	return igno15DataClerkCommentsConceptID;
    }

	
    public static int getIgno16likuniphalagiventopatientconceptid() {
    	return igno16LikuniPhalaGivenToPatientConceptID;
    }

	
    public static int getIgno18appointmentreasonortypeconceptid() {
    	return igno18AppointmentReasonOrTypeConceptID;
    }

	
    public static int getIgno19appointmentsetconceptid() {
    	return igno19AppointmentSetConceptID;
    }

	
    public String getOpenMrsId() {
    	return openMrsId;
    }
	
}
