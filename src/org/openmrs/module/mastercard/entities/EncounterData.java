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
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientState;

/**
 *
 */
public class EncounterData extends AbstractData {
	
	/**
	 * @param e
	 */
	public EncounterData(Encounter e) {
		super(e);
	}
	
	static Logger logger = Logger.getLogger(EncounterData.class);
	
	public static String getHeaderSerialized() {
		return csv("Visit loc", "Vist Date", "Hgt", "Wt", "Outcome Enrollment", "Adverse Outcome", "Outcome date",
		    "Regimen", "Side Effects", "TB status", "current Pill count", "Doses missed", "ARVs given #", "To", "CPT #",
		    "Comment", "Next appointment", "Unknown Obs");
	}
	
	protected String extractEncounterData(Encounter encounter) {
		logger.info("exportFollowup(Encounter " + encounter.getId() + ")");
		
		ObservationDataBean obsDataBean = new ObservationDataBean();
		
		extractObservations(encounter, obsDataBean);
		
		return renderEncounterToCsv(encounter, obsDataBean);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounter
	 * @param obsDataBean
	 * @return
	 */
	private String renderEncounterToCsv(Encounter encounter, ObservationDataBean obsDataBean) {
		
		String loc = map(encounter.getLocation().getName());
		String date = date(encounter.getEncounterDatetime());
		
		//PatientState s = currentProgramWorkflowStatus(1, encounter.getPatient(), encounter.getEncounterDatetime());
		
		String outcomeEnrollment = Constants.NOT_AVAILABLE;
		; //outcomeEnrollment(s);
		
		String outcomeDate = Constants.NOT_AVAILABLE;//("".equals(outcomeEnrollment) ? "" : date(s.getStartDate()));
		
		String comments = Constants.NOT_AVAILABLE;
		String nextAppt = Constants.NOT_AVAILABLE;
		String unknownObs = Constants.NOT_AVAILABLE;
		
		return csv(loc, date, obsDataBean.getHgt(), obsDataBean.getWgt(), outcomeEnrollment,
		    //obsDataBean.getOutcome(),
		    outcomeDate,
		    //obsDataBean.getArvRegimen(), 
		    obsDataBean.getSideEffectsYesNo(), obsDataBean.getTbStat(), obsDataBean.getPillCountAsString(),
		    obsDataBean.getDosesMissed(), obsDataBean.getNoOfArvGivenAsString(), /* doses missed?*/
		    obsDataBean.getNoOfArvGivenAsString(), obsDataBean.getCp4tGivenAsString(), comments, nextAppt, unknownObs); //unknownObs
	}
}
