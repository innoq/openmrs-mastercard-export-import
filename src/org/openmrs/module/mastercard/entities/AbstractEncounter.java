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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.mastercard.exceptions.WrongFormatException;
import org.openmrs.module.mastercard.Helper;

/**
 *
 */
public abstract class AbstractEncounter {
	
	GeneralEncounterDataContainer obsDataBean = null;
	
	Encounter encounter = null;
	
	public AbstractEncounter(Encounter e) {
		super();
		init();
		encounter = e;
		marshalEncounterData();
	}
	
	public AbstractEncounter(String string) throws WrongFormatException {
		super();
		init();
		demarshalData(string);
	}
	
	static Logger logger = Logger.getLogger(AbstractEncounter.class);
	
	private void init() {
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
	}
	
	public GeneralEncounterDataContainer getObservations() {
		return obsDataBean;
	}
	
	protected abstract void demarshalData(String stringArray) throws WrongFormatException;
	
	public abstract String getCsvSerialized();
	
	protected abstract void marshalEncounterData();
	
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
	
	public String reverseMap(String value) {
		// hm, good that cpu power is cheap...
		for (String key : mapper.keySet()) {
			if (mapper.get(key).equals(value)) {
				return key;
			}
		}
		return null;
	}
	
	protected String valueCoded(ConceptName valueCodedName) {
		return valueCodedName == null ? "" : valueCodedName.getName();
	}
	
	protected String numeric(Double valueNumeric) {
		return "" + valueNumeric;
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
			if (s == null) {
				s = Constants.NOT_AVAILABLE;
			}
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
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounter
	 * @param obsDataBean
	 */
	protected void extractObservations() {
		obsDataBean = new GeneralEncounterDataContainer();
		
		// ART no Pre-ART no Pre-ART start date OpenMRS ID VHW
		obsDataBean.setArtNos(identifierStrings(encounter.getPatient().getPatientIdentifiers(
		    Context.getPatientService().getPatientIdentifierType("ARV Number"))));
		//		obsDataBean.setPartNos(identifierStrings(encounter.getPatient().getPatientIdentifiers(
		//		    Context.getPatientService().getPatientIdentifierType("PART Number"))));
		
		//obsDataBean.setPartStart();
		obsDataBean.setPatientId(encounter.getPatientId());
		
		obsDataBean.setPatientGivenName(h(encounter.getPatient().getGivenName()));
		
		obsDataBean.setPatientFamilyName(h(encounter.getPatient().getFamilyName()));
		
		obsDataBean.setSex(encounter.getPatient().getGender());
		obsDataBean.setDateOfBirth(encounter.getPatient().getBirthdate());
		
		for (Obs o : encounter.getAllObs()) {
			switch (o.getConcept().getConceptId().intValue()) {
			
				case GeneralEncounterDataContainer.vhwProgramConceptID:
					obsDataBean.setVhwName(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.phoneNumberCountConceptID:
					obsDataBean.setPhone(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.alt1stLineArvsConceptID:
					obsDataBean.setAlt1stL(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.altConceptID:
					logger.warn("altConceptID " + o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.arvRegimenTypConceptID:
					obsDataBean.setArvRegimen(o.getValueText());
					logger.warn("arvRegimenTypConceptID " + o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.commentsAtConclusionOfExaminationConceptID:
					obsDataBean.setComment(o.getValueText());
					logger.warn("commentConceptID " + o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.dateAntiretroviralsStartedConceptID:
					obsDataBean.setAlt1stLDate(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.guardianFirstNameConceptID:
					obsDataBean.setGuardianFirstName(o.getValueText());
					break;
				case GeneralEncounterDataContainer.guardianLastNameConceptID:
					obsDataBean.setGuardianLastName(o.getValueText());
					break;
				case GeneralEncounterDataContainer.fupConceptID:
					obsDataBean.setFup(valueCoded(o.getValueCoded().getName()));
					break;
				case GeneralEncounterDataContainer.locationWhereTestTookPlaceConceptID:
					obsDataBean.setLocationWhereTestTookPlace(h(o.getValueText()));
					break;
				case GeneralEncounterDataContainer.dateOfHivDiagnososConceptID:
					obsDataBean.setDateOfHiVDiagnosis(Helper.getStringFromDate((o.getValueDatetime())));
					break;
				case GeneralEncounterDataContainer.wgtConceptID:
					obsDataBean.setWgt(o.getValueNumeric());
					break;
				case GeneralEncounterDataContainer.hgtConceptID:
					obsDataBean.setHgt(o.getValueNumeric());
					break;
				case GeneralEncounterDataContainer.sideEffectsYesNoConceptID:
					if (o.getValueNumeric() == null || o.getValueNumeric() == 0) {
						obsDataBean.setSideEffectsYesNo("No");
					}
					break;
				case GeneralEncounterDataContainer.sideEffectsStringConceptID:
					obsDataBean.setSideEffectsOfTreatment(map(valueCoded(o.getValueCoded().getName())));
					break;
				case GeneralEncounterDataContainer.sideEffectsCommentsConceptID:
					obsDataBean.setSideEffectsComments(o.getValueText());
					break;
				case GeneralEncounterDataContainer.sideEffectsOfTreatmentConceptID:
					obsDataBean.setSideEffectsOfTreatment(o.getValueText());
					break;
				case GeneralEncounterDataContainer.stageConceptID:
					obsDataBean.setStage(map(valueCoded(o.getValueCoded().getName())));
					break;
				case GeneralEncounterDataContainer.pregConceptID:
					obsDataBean.setPreg(map(valueCoded(o.getValueCoded().getName())));
					break;
				case GeneralEncounterDataContainer.tbStatusConceptID:
					obsDataBean.setTbStat(map(valueCoded(o.getValueCoded().getName())));
					break;
				case GeneralEncounterDataContainer.typeConceptID:
					obsDataBean.setType(o.getValueText());
					break;
				case GeneralEncounterDataContainer.cd4CountConceptID:
					obsDataBean.setCd4(o.getValueText());
					break;
				//case ObservationDataBean.cd4DateConceptID:
				case GeneralEncounterDataContainer.dateofCd4CountConceptID:
					obsDataBean.setCd4Date(o.getValueText());
					break;
				case GeneralEncounterDataContainer.cd4PercentageConceptID:
					obsDataBean.setCd4Percentage(o.getValueText());
					break;
				case GeneralEncounterDataContainer.cd4PercentageDateTimeConceptID:
					obsDataBean.setCd4PercentageDateTime(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.dosesMissedConceptId:
					obsDataBean.setDosesMissed(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.ksConceptID:
					obsDataBean.setKs(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.statusOfArvRegimen:
					obsDataBean.setArvRegimen(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.arvDrugsReceivedConceptID:
					obsDataBean.setArvDrugsReceived(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.nextAppointmentConceptID:
					obsDataBean.setNextAppointment(Helper.getStringFromDate(o.getValueDatetime()));
					break;
				
				case GeneralEncounterDataContainer.noOfArvGivenConceptID:
					obsDataBean.setNoOfArvGiven(o.getValueNumeric());
					break;
				
				case GeneralEncounterDataContainer.pillCountConceptID:
					obsDataBean.setPillCount(o.getValueNumeric());
					break;
				
				case GeneralEncounterDataContainer.outcomeConceptID:
					obsDataBean.setOutcome(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.cptGivenConceptID:
					obsDataBean.setCp4tGiven(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.cptDateConceptID:
					obsDataBean.setCp4TDate(o.getValueText());
					break;
				
				case GeneralEncounterDataContainer.igno01DateOfLastMenstrualBlood2ConceptID:
				case GeneralEncounterDataContainer.igno02ReasonAntiretroviralsStarted2ConceptID:
					//case ObservationDataBean.igno03Name2ConceptID:
				case GeneralEncounterDataContainer.igno04CommentsAtConclustionOfExamination2ConceptID:
				case GeneralEncounterDataContainer.igno05IsOnCpt2ConceptID:
				case GeneralEncounterDataContainer.igno06GuardianPresent2ConceptID:
				case GeneralEncounterDataContainer.igno07LastNameCommunityHealthWorkerReportedConceptID:
				case GeneralEncounterDataContainer.igno07FirstNameCommunityHealthWorkerReportedConceptID:
				case GeneralEncounterDataContainer.igno09HiVDiagnososConstructConceptID:
				case GeneralEncounterDataContainer.igno10TransferInConceptID:
				case GeneralEncounterDataContainer.igno11GoesToWorkOrSchoolConceptID:
				case GeneralEncounterDataContainer.igno12Name2ConceptID:
				case GeneralEncounterDataContainer.igno13TinsOfAntiretroviralsGivenToPatientConceptID:
				case GeneralEncounterDataContainer.igno14WhoStageCriteriaPresentConceptID:
				case GeneralEncounterDataContainer.igno15DataClerkCommentsConceptID:
				case GeneralEncounterDataContainer.igno16LikuniPhalaGivenToPatientConceptID:
				case GeneralEncounterDataContainer.igno18AppointmentReasonOrTypeConceptID:
				case GeneralEncounterDataContainer.igno19AppointmentSetConceptID:
					try {
						logger.info("ignored Observation: " + o.getConcept().getName() + " (" + o.getConcept().getId() + ")");
					}
					catch (NullPointerException e) {}
					break;
				default:
					logger.warn("Found unknown Observation): " + o.getConcept().getName() + " (" + o.getConcept().getId()
					        + ")");
					
			}
		}
	}
	
	/**
	 * Splits a semicolon separated Line into single elements
	 * 
	 * @param string
	 * @return
	 */
	protected String[] parseLine(String string) {
		String[] lineElementArray = string.split(";");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Breaking line " + string + " into:");
			for (String lineElement : lineElementArray) {
				logger.debug("broken into: " + lineElement);
			}
		}
		return lineElementArray;
	}
}
