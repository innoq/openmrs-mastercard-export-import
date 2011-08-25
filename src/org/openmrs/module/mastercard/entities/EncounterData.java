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
	
	protected String extractEncounterData(Encounter e) {
		logger.info("exportFollowup(Encounter " + e.getId() + ")");
		Set<Obs> obss = e.getAllObs();
		String loc = map(e.getLocation().getName());
		String date = date(e.getEncounterDatetime());
		String hgt = Constants.NOT_AVAILABLE;
		String wgt = Constants.NOT_AVAILABLE;
		PatientState s = currentProgramWorkflowStatus(1, e.getPatient(), e.getEncounterDatetime());
		String outcomeEnrollment = outcomeEnrollment(s);
		String outcome = Constants.NOT_AVAILABLE;
		String outcomeDate = ("".equals(outcomeEnrollment) ? "" : date(s.getStartDate()));
		String regimen = Constants.NOT_AVAILABLE;
		String sideEffects = "";
		String tb = Constants.NOT_AVAILABLE;
		String pillCount = Constants.NOT_AVAILABLE;
		String dosesMissed = Constants.NOT_AVAILABLE;
		String noOfArvGiven = Constants.NOT_AVAILABLE;
		String arvsGivenTo = Constants.NOT_AVAILABLE;
		String cptNo = Constants.NOT_AVAILABLE;
		String comments = Constants.NOT_AVAILABLE;
		String nextAppt = Constants.NOT_AVAILABLE;
		String unknownObs = "";
		for (Obs o : obss) {
			switch (o.getConcept().getConceptId()) {
				case 5090:
					hgt = numeric(o.getValueNumeric());
					break;
				case 5089:
					wgt = numeric(o.getValueNumeric());
					break;
				case 2530:
					outcome = map(valueCoded(o.getValueCodedName()));
					break;
				case 2538:
					regimen = map(valueCoded(o.getValueCodedName()));
					break;
				case 2589:
					// new regimen
					break;
				case 2146:
					if (o.getValueNumeric() == null || o.getValueNumeric() == 0) {
						sideEffects += "No ";
					}
					break;
				case 1297:
					sideEffects += map(valueCoded(o.getValueCoded().getName())) + " ";
					break;
				case 7459:
					// tbstatus
					break;
				case 2540:
					pillCount = numeric(o.getValueNumeric());
					break;
				// doses missed
				case 2929:
					noOfArvGiven = numeric(o.getValueNumeric());
					break;
				// cpt no
				// comment
				case 5096:
					nextAppt = date(o.getValueDatetime());
					break;
				case 1620:
				case 1623:
				case 6784:
				case 6785:
				case 2541:
				case 2922:
				case 968:
				case 2542:
				case 2972:
				case 1662: // really?
				case 2536: // really?
				case 2539: // really?
				case 5272: // really?
				case 2122: // really?
					break;
				default:
					logger.warn("Found unknown Observation: " + o.getConcept().getName().getName() + " ("
					        + o.getConcept().getId() + ")");
					unknownObs += o.getConcept().getName().getName() + " (" + o.getConcept().getId() + ") " + " | ";
			}
		}
		
		return csv(loc, date, hgt, wgt, outcomeEnrollment, outcome, outcomeDate, regimen, sideEffects, tb, pillCount,
		    dosesMissed, noOfArvGiven, arvsGivenTo, cptNo, comments, nextAppt, unknownObs);
		
	}
	
}
