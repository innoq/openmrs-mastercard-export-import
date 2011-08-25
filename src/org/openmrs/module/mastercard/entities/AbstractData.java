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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mastercard.Helper;

/**
 *
 */
public abstract class AbstractData {
	
	String stringRepresentation = null;
	
	public AbstractData(Encounter e) {
		super();
		
		//TODO mild: put mapper data to a singleton or factory etc.
		String[] mappings = { "Neno District Hospital", "NNO", "Nsambe HC", "NSM", "Magaleta HC", "MGT",
		
		"ON ANTIRETROVIRALS", "", "ALIVE", "", "PATIENT TRANSFERRED OUT", "TO",
		
		"ALIVE AND ON FIRST LINE ANTIRETROVIRAL REGIMEN", "1L", "SUBSTITUTE ANOTHER 1ST LINE ARV DRUG", "",
		
		"NONE", "No", "PERIPHERAL NEUROPATHY", "PN", "OTHER NON-CODED", "Oth",
		
		"WHO STAGE I PEDS", "1", "WHO STAGE II PEDS", "2", "WHO STAGE III PEDS", "3", "WHO STAGE IV PEDS", "4",
		        "WHO STAGE I ADULT", "1", "WHO STAGE II ADULT", "2", "WHO STAGE III ADULT", "3", "WHO STAGE IV ADULT", "4",
		        "", "" };
		for (int i = 0; i < mappings.length; i += 2) {
			mapper.put(mappings[i], mappings[i + 1]);
		}
		//END TODO mild
		
		stringRepresentation = extractEncounterData(e);
	}
	
	public String getCsvSerialized() {
		return stringRepresentation;
	}
	
	protected abstract String extractEncounterData(Encounter e);
	
	protected static final String NOT_AVAILABLE = "-";
	
	protected static final String NEWLINE = "\n";
	
	private Helper h = new Helper();
	
	/* value mapping */
	private Map<String, String> mapper = new HashMap<String, String>();
	
	protected String outcomeEnrollment(PatientState s) {
		if (s != null) {
			return map(s.getState().getConcept().getName().getName());
		}
		return "";
	}
	
	protected String map(String key) {
		if (mapper.get(key) != null) {
			return mapper.get(key);
		}
		return key;
	}
	
	protected String valueCoded(ConceptName valueCodedName) {
		return valueCodedName == null ? "" : valueCodedName.getName();
	}
	
	protected String numeric(Double valueNumeric) {
		return "" + valueNumeric;
	}
	
	protected String date(Date date) {
		return new SimpleDateFormat("dd MMM yyyy").format(date);
	}
	
	protected ProgramWorkflowService programWorkflowService() {
		return Context.getProgramWorkflowService();
	}
	
	protected PatientState currentProgramWorkflowStatus(Integer programWorkflowId, Patient p, Date stateAtDate) {
		if (p == null || p.getId() == null) {
			return null;
		}
		ProgramWorkflow workflow = programWorkflowService().getWorkflow(programWorkflowId); // not sure if and how I want to reference
		                                                                                    // the UUID
		List<PatientProgram> pps = programWorkflowService().getPatientPrograms(p, workflow.getProgram(), null, null, null,
		    null, false);
		TreeMap<Date, PatientState> sortedStates = new TreeMap<Date, PatientState>();
		
		// collect all states ordered by start date
		for (PatientProgram pp : pps) {
			if (!pp.isVoided()) {
				for (PatientState ps : pp.getStates()) {
					if (!ps.isVoided()) {
						sortedStates.put(ps.getStartDate(), ps);
					}
				}
			}
		}
		// get the one with the closest startdate before stateAtDate
		PatientState mostRecentState = null;
		for (Date startDate : sortedStates.keySet()) {
			if (!startDate.after(stateAtDate)) {
				mostRecentState = sortedStates.get(startDate);
			}
		}
		
		if (mostRecentState != null && mostRecentState.getState() != null
		        && mostRecentState.getState().getConcept().getName() != null) {
			return mostRecentState;
		}
		return null;
	}
	
	protected static String csv(String... strings) {
		String result = "";
		for (String s : strings) {
			result += s.replaceAll("\\r|\\n|\\t|;", " ").replaceAll("   ", " ").replaceAll("  ", " ").trim() + ";";
		}
		return result;
	}
	
	protected static String csv(String string, List<String> strings) {
		String result = string;
		for (String s : strings) {
			result += s + ";";
		}
		return result;
	}
	
	protected List<String> identifiers(List<PatientIdentifier> patientIdentifiers) {
		List<String> ids = new ArrayList<String>();
		for (PatientIdentifier pi : patientIdentifiers) {
			ids.add(pi.getIdentifier());
		}
		return ids;
	}
	
	protected String identifierStrings(List<PatientIdentifier> patientIdentifiers) {
		String ids = "";
		for (PatientIdentifier pi : patientIdentifiers) {
			ids += pi.getIdentifier() + " ";
		}
		return ids;
	}
	
	protected String h(String string) {
		return string == null ? "" : string;
	}
}
