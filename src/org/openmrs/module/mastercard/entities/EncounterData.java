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

import org.apache.log4j.Logger;
import org.openmrs.Encounter;
import org.openmrs.mastercard.exceptions.WrongFormatException;

/**
 *
 */
public class EncounterData extends AbstractData {
	
	/**
	 * @return the locationOfEncounter
	 */
	public String getLocationOfEncounter() {
		return locationOfEncounter;
	}
	
	/**
	 * @return the dateOfEncounter
	 */
	public String getDateOfEncounter() {
		return dateOfEncounter;
	}
	
	private String locationOfEncounter;
	
	private String dateOfEncounter;
	
	/**
	 * @param e
	 */
	public EncounterData(Encounter e) {
		super(e);
	}
	
	public EncounterData(String string) throws WrongFormatException {
		super(string);
	}
	
	static Logger logger = Logger.getLogger(EncounterData.class);
	
	public static String getHeaderSerialized() {
		return csv("Visit loc", "Vist Date", "Hgt", "Wt", "Outcome Enrollment", "Adverse Outcome", "Outcome date",
		    "Regimen", "Side Effects", "TB status", "current Pill count", "Doses missed", "ARVs given #", "To", "CPT #",
		    "Comment", "Next appointment", "Unknown Obs");
	}
	
	protected void marshalEncounterData() {
		logger.info("exportFollowup(Encounter " + encounter.getId() + ")");
		
		ObservationDataBean obsDataBean = new ObservationDataBean();
		
		extractObservations();
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounter
	 * @param obsDataBean
	 * @return
	 */
	private String renderEncounterToCsv(Encounter encounter) {
		
		String loc = map(encounter.getLocation().getName());
		String date = date(encounter.getEncounterDatetime());
		
		//PatientState s = currentProgramWorkflowStatus(1, encounter.getPatient(), encounter.getEncounterDatetime());
		
		String outcomeEnrollment = Constants.NOT_AVAILABLE;
		; //outcomeEnrollment(s);
		
		String outcomeDate = Constants.NOT_AVAILABLE;//("".equals(outcomeEnrollment) ? "" : date(s.getStartDate()));
		
		String unknownObs = Constants.NOT_AVAILABLE;
		
		return csv(loc, date, obsDataBean.getHgt(), obsDataBean.getWgt(), outcomeEnrollment,
		    "outcome",
		    //obsDataBean.getOutcome(),
		    outcomeDate,
		    "arvReg",
		    //obsDataBean.getArvRegimen(), 
		    obsDataBean.getSideEffectsYesNo(), obsDataBean.getTbStat(), obsDataBean.getPillCountAsString(),
		    obsDataBean.getDosesMissed(), obsDataBean.getNoOfArvGivenAsString(), /* doses missed?*/
		    obsDataBean.getNoOfArvGivenAsString(), obsDataBean.getCp4tGivenAsString(), obsDataBean.getComment(),
		    obsDataBean.getNextAppointment(), unknownObs); //unknownObs
	}
	
	/**
	 * @throws WrongFormatException
	 * @see org.openmrs.module.mastercard.entities.AbstractData#demarshalData(java.lang.String[])
	 */
	@Override
	protected void demarshalData(String string) throws WrongFormatException {
		obsDataBean = new ObservationDataBean();
		
		handleLine(obsDataBean, parseLine(string));
	}
	
	/**
	 * "Visit loc;Vist Date;Hgt;Wt;Outcome Enrollment;Adverse Outcome;Outcome date;Regimen;Side Effects;TB status;current Pill count;Doses missed;ARVs given #;To;CPT #;Comment;Next appointment;Unknown Obs;";
 	 * NNO;26 Apr 2011;138.0;30.2;-;outcome;-;arvReg;-;TB NOT SUSPECTED;0.0;-;180.0;180.0;-;-;18 Jul 2011;-;
	 * 
	 * @param obsDataBean
	 * @param parseLine
	 */
	private void handleLine(ObservationDataBean obsDataBean, String[] parseLine) {
		locationOfEncounter = parseLine[0];
		dateOfEncounter = parseLine[1];
		obsDataBean.setHgt(parseLine[2]);
		obsDataBean.setWgt(parseLine[3]);
		
		//line 4 ??
		obsDataBean.setOutcome(parseLine[5]);
		
		//TODO cneumann check wether this matches outcome date?
		obsDataBean.setDateOfHiVDiagnosis(parseLine[6]);
		obsDataBean.setArvRegimen(parseLine[7]);
		obsDataBean.setSideEffectsYesNo(parseLine[8]);
		obsDataBean.setTbStat(parseLine[9]);
		obsDataBean.setPillCount(parseLine[10]);
		obsDataBean.setDosesMissed(parseLine[11]);
		obsDataBean.setArvDrugsReceived(parseLine[12]);
		obsDataBean.setCp4tGiven(parseLine[13]);
		obsDataBean.setComment(parseLine[14]);
		
	}
	
	/**
	 * @see org.openmrs.module.mastercard.entities.AbstractData#getCsvSerialized()
	 */
	@Override
	public String getCsvSerialized() {
		return renderEncounterToCsv(encounter);
	}
}
