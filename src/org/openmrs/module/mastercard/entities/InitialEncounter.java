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
import org.openmrs.Encounter;
import org.openmrs.PatientState;
import org.openmrs.PersonAddress;
import org.openmrs.mastercard.exceptions.WrongFormatException;

/**
 *
 */
public class InitialEncounter extends AbstractEncounter {
	
	static Logger logger = Logger.getLogger(InitialEncounter.class);
	
	public InitialEncounter(Encounter e) {
		super(e);
	}
	
	public InitialEncounter(String string) throws WrongFormatException {
		super(string);
	}
	
	protected void marshalEncounterData() {
		logger.info("exportInitial(Encounter " + encounter.getId() + ")");
		
		extractObservations();
		
		PatientState ps = currentProgramWorkflowStatus(1, encounter.getPatient(), new Date());
		//		ps = h.getMostRecentStateAtLocation(encounter.getPatient(), h.program("HIV PROGRAM"), h.location("Neno District Hospital"), sessionFactory().getCurrentSession());
		
		if (ps != null) {
			obsDataBean.setOutcome(ps.getState().getConcept().getName().getName());
		} else {
			obsDataBean.setOutcome("Unknown");
		}
		
		//TODO Mild: Handle Adresses in a better way!
		//String addr = "";
		//Set<PersonAddress> addresses = encounter.getPatient().getAddresses();
		//for (PersonAddress a : addresses) {
		//	addr += h(a.getCityVillage()) + " " + h(a.getCountyDistrict()) + ", ";
		//}
	}
	
	/**
	 * @throws WrongFormatException
	 * @see org.openmrs.module.mastercard.entities.AbstractEncounter#demarshalData(java.lang.String[])
	 */
	@Override
	protected void demarshalData(String string) throws WrongFormatException {
		obsDataBean = new GeneralEncounterDataContainer();
		
		//Here we split the single string into the basic array structure from mastercard format
		//as assembled in the calling ArtImporter function.
		String[] stringArray = string.split("#;;#");
		
		if (stringArray.length != 10) {
			throw new WrongFormatException("not enough header lines found - instead of 10: " + stringArray.length);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Breaking line " + string + " into:");
			for (String lineElement : stringArray) {
				logger.debug("broken into: " + lineElement);
			}
		}
		
		if (!stringArray[0].isEmpty())
			throw new WrongFormatException("Header Line 0 expected to be empty /'/'");
		
		handleLine2(obsDataBean, parseLine(stringArray[1]));
		handleLine3(obsDataBean, parseLine(stringArray[2]));
		if (!stringArray[3].isEmpty())
			throw new WrongFormatException("Header Line 0 expected to be empty /'/'");
		handleLine5(obsDataBean, parseLine(stringArray[4]));
		handleLine6(obsDataBean, parseLine(stringArray[5]));
		handleLine7(obsDataBean, parseLine(stringArray[6]));
		handleLine8(obsDataBean, parseLine(stringArray[7]));
		handleLine9(obsDataBean, parseLine(stringArray[8]));
		handleLine10(obsDataBean, parseLine(stringArray[9]));
	}
	
	/**
	 * Parsing Line 10, similar to
	 * "Agrees to FUP;YES;Guardian Relation;-;Guardian Phone;-;Age at init.;-;;;Last ARVs (drug, date);-;2nd Line;-;-;;Unknown Obs;-"
	 * ;
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine10(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		obsDataBean.setFup(parseLine[1]);
		obsDataBean.setGrel(parseLine[3]);
		obsDataBean.setGphone(parseLine[5]);
		obsDataBean.setAgeInit(parseLine[7]);
		obsDataBean.setLastArv(parseLine[11]);
		obsDataBean.setSecondL(parseLine[13]);
		obsDataBean.setSecondLDate(parseLine[14]);
		//Ignoring Unkown Obs
	}
	
	/**
	 * Parsing Line 9, similar to
	 * "Guardian Name;Njolomole / Watson;;;;;Height;-;Weight;17.8;Ever taken ARVs;-;Alt 1st Line;-;-"
	 * ;
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine9(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		
		obsDataBean.setGuardianName(parseLine[1]);
		obsDataBean.setHgtAsString(parseLine[7]);
		obsDataBean.setWgtAsString(parseLine[9]);
		obsDataBean.setEverArv(parseLine[11]);
		obsDataBean.setAlt1stL(parseLine[13]);
		obsDataBean.setAlt1stLDate(parseLine[14]);
	}
	
	/**
	 * Parsing Line 8, similar to
	 * "Phys. Address;-;;;;;CD4 date;-;;;Pregnant at initiation;-;1st Line;d4T 3TC NVP;-";
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine8(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		//TODO mild: Add proper adress handling
		obsDataBean.setAddr("to be done");
		obsDataBean.setCd4Date(parseLine[7]);
		obsDataBean.setPreg(parseLine[11]);
		obsDataBean.setAlt1stLDate(parseLine[14]);
	}
	
	/**
	 * Parsing Line 7, similar to
	 * "Sex;M;DOB;09 Jun 1999;Patient phone;-;CD4 count;-;%;-;KS;-;ART Regimen;;Start date;-;";
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine7(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		obsDataBean.setSex(parseLine[1]);
		obsDataBean.setDateOfBirth((String) parseLine[3]);
		obsDataBean.setPhone(parseLine[5]);
		obsDataBean.setCd4(parseLine[7]);
		obsDataBean.setCd4Percentage(parseLine[9]);
		obsDataBean.setKs(parseLine[11]);
		obsDataBean.setArvRegimen(parseLine[13]);
		obsDataBean.setArvDrugsReceived(parseLine[15]);
	}
	
	/**
	 * Parsing Line 6, similar to
	 * "Patient name;Dambudzo/Njolomole;;;;;Clin Stage;3;;;TB Status at initiation;-;Date, Place;01 Jul 2005/QECH;Type;-;"
	 * ;
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine6(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		obsDataBean.setName(parseLine[1]);
		obsDataBean.setStage(parseLine[7]);
		obsDataBean.setTbStat(parseLine[11]);
		String[] hivDiagnosisElementArrays = parseLine[13].split("/");
		obsDataBean.setDateOfHiVDiagnosis(hivDiagnosisElementArrays[0]);
		obsDataBean.setLocationWhereTestTookPlace(hivDiagnosisElementArrays[1]);
		
	}
	
	/**
	 * Parsing Line 5, similar to
	 * "Patient Guardian details;;;;;;Status at ART initiation;;;;;;First positive HIV test;";
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine5(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		
		//Line 5 keeps only header text, no processing needed
		
	}
	
	/**
	 * Parsing Line 3, similar to "ART no;NNO 1;OpenMRS ID;15889";
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine3(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		obsDataBean.setArtNos(parseLine[1]);
		//TODO cneumann what to do with OpenMRS-ID?
		
	}
	
	/**
	 * Parsing Line 2, similar to "Outcome NNO;ON ANTIRETROVIRALS;at location;";
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine2(GeneralEncounterDataContainer obsDataBean, String[] parseLine) {
		obsDataBean.setOutcome(parseLine[1]);
		
		//TODO mild: output of location is missing in export
		obsDataBean.setLocationWhereTestTookPlace(parseLine[3]);
	}
	
	/**
	 * @see org.openmrs.module.mastercard.entities.AbstractEncounter#getCsvSerialized(org.openmrs.Encounter)
	 */
	@Override
	public String getCsvSerialized() {
		String r = "";
		
		r += csv("Outcome NNO", obsDataBean.getOutcome(), "at location", obsDataBean.getLocationWhereTestTookPlace());
		r += Constants.NEWLINE;
		
		String addr = "";
		Set<PersonAddress> addresses = encounter.getPatient().getAddresses();
		for (PersonAddress a : addresses) {
			addr += h(a.getCityVillage()) + " " + h(a.getCountyDistrict()) + ", ";
		}
		
		r += csv("ART no", obsDataBean.getArtNos(), "OpenMRS ID", obsDataBean.getPatientId().toString());
		r += Constants.NEWLINE + Constants.NEWLINE;
		r += csv("Patient Guardian details", "", "", "", "", "", "Status at ART initiation", "", "", "", "", "",
		    "First positive HIV test", "");
		r += Constants.NEWLINE;
		r += csv("Patient name", obsDataBean.getName(), "", "", "", "", "Clin Stage", obsDataBean.getStage(), "", "",
		    "TB Status at initiation", obsDataBean.getTbStat(), "Date, Place", obsDataBean.getDatePlace(), "Type",
		    obsDataBean.getType());
		r += Constants.NEWLINE;
		r += csv("Sex", obsDataBean.getSex(), "DOB", obsDataBean.getDateOfBirthAsString(), "Patient phone",
		    obsDataBean.getPhone(), "CD4 count", obsDataBean.getCd4(), "%", obsDataBean.getCd4Percentage(), "KS",
		    obsDataBean.getKs(), "ART Regimen", "", "Start date", obsDataBean.getArvDrugsReceived()); //TODO cneumann: check on getArvDrugsReceived
		r += Constants.NEWLINE;
		r += csv("Phys. Address", obsDataBean.getAddr(), "", "", "", "", "CD4 date", obsDataBean.getCd4Date(), "", "",
		    "Pregnant at initiation", obsDataBean.getPreg(), "1st Line", "d4T 3TC NVP", obsDataBean.getD4TDate());
		r += Constants.NEWLINE;
		r += csv("Guardian Name", obsDataBean.getGuardianName(), "", "", "", "", "Height", obsDataBean.getHgtAsString(),
		    "Weight", obsDataBean.getWgtAsString(), "Ever taken ARVs", obsDataBean.getEverArv(), "Alt 1st Line",
		    obsDataBean.getAlt1stL(), obsDataBean.getAlt1stLDate());
		r += Constants.NEWLINE;
		r += csv("Agrees to FUP", obsDataBean.getFup(), "Guardian Relation", obsDataBean.getGrel(), "Guardian Phone",
		    obsDataBean.getGphone(), "Age at init.", obsDataBean.getAgeInit(), "", "", "Last ARVs (drug, date)",
		    obsDataBean.getLastArv(), "2nd Line", obsDataBean.getSecondL(), obsDataBean.getSecondLDate(), "", "Unknown Obs",
		    obsDataBean.getUnknownObs());
		r += Constants.NEWLINE;
		
		return r;
	}
	
}
