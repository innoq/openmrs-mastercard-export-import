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
package org.openmrs.module.patientcardexchange;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientcardexchange.model.IBaseData;
import org.openmrs.module.patientcardexchange.model.IEncounter;
import org.openmrs.module.patientcardexchange.model.IObs;
import org.openmrs.module.patientcardexchange.model.IPatient;
import org.openmrs.module.patientcardexchange.model.IPatientAddress;
import org.openmrs.module.patientcardexchange.model.IPatientIdentifier;
import org.openmrs.module.patientcardexchange.model.IPatientName;
import org.openmrs.module.patientcardexchange.model.IPatientProgram;
import org.openmrs.module.patientcardexchange.model.IPatientState;
import org.openmrs.util.OpenmrsUtil;

public class ConverterFromPatient {
	
	public IPatient convert(Patient src, EncounterType initialEncounterType, EncounterType followupEncounterType, Connection jdbcConnection) {
		IPatient target = new IPatient();
		
		convertBaseData(src, target);
		for (PersonAddress srcPa : src.getAddresses()) {
			IPatientAddress targetPa = new IPatientAddress();
			convertBaseData(srcPa, targetPa);
			targetPa.address1 = srcPa.getAddress1();
			targetPa.address2 = srcPa.getAddress2();
			targetPa.cityVillage = srcPa.getCityVillage();
			targetPa.countyDistrict = srcPa.getCountyDistrict();
			targetPa.id = srcPa.getPersonAddressId();
			targetPa.preferred = srcPa.getPreferred();
			target.addresses.add(targetPa);
		}
		target.birthdate = src.getBirthdate();
		target.birthdateEstimated = src.getBirthdateEstimated();
		target.causeOfDeathConceptId = (src.getDead() && src.getCauseOfDeath() != null ? src.getCauseOfDeath().getConceptId() : null);
		target.dead = src.getDead();
		target.deathDate = src.getDeathDate();
		for (Encounter srcEncounter : Context.getEncounterService().getEncounters(src, null, null, null, null,
		    Arrays.asList(followupEncounterType), null, false)) {
			IEncounter targetEncounter = new IEncounter();
			convertBaseData(srcEncounter, targetEncounter);
			targetEncounter.encounterDatetime = srcEncounter.getEncounterDatetime();
			targetEncounter.encounterTypeId = srcEncounter.getEncounterType().getEncounterTypeId();
			targetEncounter.id = srcEncounter.getEncounterId();
			targetEncounter.locationId = srcEncounter.getLocation().getLocationId();
			targetEncounter.obses = convertObses(srcEncounter.getAllObs());
			//			targetEncounter.providerName
			target.encounters.add(targetEncounter);
		}
		target.gender = src.getGender();
		target.id = src.getPatientId();
		for (Encounter srcEncounter : Context.getEncounterService().getEncounters(src, null, null, null, null,
		    Arrays.asList(initialEncounterType), null, false)) {
			IEncounter targetEncounter = new IEncounter();
			convertBaseData(srcEncounter, targetEncounter);
			targetEncounter.encounterDatetime = srcEncounter.getEncounterDatetime();
			targetEncounter.encounterTypeId = srcEncounter.getEncounterType().getEncounterTypeId();
			targetEncounter.id = srcEncounter.getEncounterId();
			targetEncounter.locationId = srcEncounter.getLocation().getLocationId();
			targetEncounter.obses = convertObses(srcEncounter.getAllObs());
			//			targetEncounter.providerName
			target.encounters.add(targetEncounter);
			break; // should only be one
		}
		for (PatientIdentifier srcPi : src.getIdentifiers()) {
			IPatientIdentifier targetPi = new IPatientIdentifier();
			convertBaseData(srcPi, targetPi);
			targetPi.id = srcPi.getPatientIdentifierId();
			targetPi.identifier = srcPi.getIdentifier();
			targetPi.identifierTypeId = srcPi.getIdentifierType().getId();
			targetPi.locationId = (srcPi.getLocation() != null ? srcPi.getLocation().getLocationId() : null);
			targetPi.preferred = srcPi.getPreferred();
			target.identifiers.add(targetPi);
		}
		for (PersonName srcName : src.getNames()) {
			IPatientName targetName = new IPatientName();
			convertBaseData(srcName, targetName);
			targetName.familyName = srcName.getFamilyName();
			targetName.givenName = srcName.getGivenName();
			targetName.id = srcName.getId();
			targetName.middleName = srcName.getMiddleName();
			targetName.preferred = srcName.getPreferred();
			target.names.add(targetName);
		}
		for (PatientProgram srcPp : Context.getProgramWorkflowService().getPatientPrograms(src, null, null, null, null,
		    null, false)) {
			IPatientProgram targetPp = new IPatientProgram();
			convertBaseData(srcPp, targetPp);
			targetPp.dateCompleted = srcPp.getDateCompleted();
			targetPp.dateEnrolled = srcPp.getDateEnrolled();
			targetPp.locationId = Util.getEnrollmentLocation(srcPp, jdbcConnection).getLocationId();
			targetPp.programId = srcPp.getProgram().getProgramId();
			// first sort states that that they are coming in the correct order
			List<PatientState> states = new ArrayList<PatientState>();
			for (PatientState st : srcPp.getStates()) {
				if (!st.getVoided()) {
					// todo, won't work with multiple workflows
					states.add(st);
				}
			}
			Collections.sort(states, new Comparator<PatientState>() {
				public int compare(PatientState left, PatientState right) {
					// check if one of the states is active 
					if (left.getActive()) {
						return 1;
					}
					if (right.getActive()) {
						return -1;
					}
					return OpenmrsUtil.compareWithNullAsEarliest(left.getStartDate(), right.getStartDate());
				}
			});
			for (PatientState srcState : states) {
				IPatientState targetState = new IPatientState();
				convertBaseData(srcState, targetState);
				targetState.endDate = srcState.getEndDate();
				targetState.programWorkflowId = srcState.getState().getProgramWorkflow().getProgramWorkflowId();
				targetState.programWorkflowStateId = srcState.getState().getProgramWorkflowStateId();
				targetState.startDate = srcState.getStartDate();
				targetPp.states.add(targetState);
			}
			target.patientPrograms.add(targetPp);
		}
		
		return target;
	}
	
	private List<IObs> convertObses(Set<Obs> srcObses) {
		List<IObs> iobses = new ArrayList<IObs>();
		for (Obs srcObs : srcObses) {
			IObs targetObs = new IObs();
			convertBaseData(srcObs, targetObs);
			targetObs.conceptId = srcObs.getConcept().getConceptId();
			targetObs.dateStarted = srcObs.getDateStarted();
			targetObs.dateStopped = srcObs.getDateStopped();
			targetObs.locationId = (srcObs.getLocation() != null ? srcObs.getLocation().getLocationId() : null);
			targetObs.obsDatetime = srcObs.getObsDatetime();
			targetObs.obsId = srcObs.getObsId();
			//				targetObs.person
			targetObs.valueAsString = srcObs.getValueAsString(Context.getLocale());
			targetObs.valueCodedConceptId = (srcObs.getValueCoded() != null ? srcObs.getValueCoded().getConceptId() : null);
			targetObs.valueCodedConceptName = (srcObs.getValueCoded() != null && srcObs.getValueCoded().getName() != null ? srcObs
			        .getValueCoded().getName().getName()
			        : null);
			targetObs.valueComplex = srcObs.getValueComplex();
			targetObs.valueDatetime = srcObs.getValueDatetime();
			targetObs.valueModifier = srcObs.getValueModifier();
			targetObs.valueNumeric = srcObs.getValueNumeric();
			targetObs.valueText = srcObs.getValueText();
			iobses.add(targetObs);
		}
		return iobses;
	}
	
	private void convertBaseData(BaseOpenmrsData src, IBaseData target) {
		target.changedById = (src.getChangedBy() != null ? src.getChangedBy().getUserId() : null);
		target.creatorId = (src.getCreator() != null ? src.getCreator().getUserId() : null);
		target.dateChanged = src.getDateChanged();
		target.dateCreated = src.getDateCreated();
		target.dateVoided = src.getDateVoided();
		target.uuid = src.getUuid();
		target.voided = src.getVoided();
		target.voidedById = (src.getVoidedBy() != null ? src.getVoidedBy().getUserId() : null);
		target.voidReason = src.getVoidReason();
	}	
}
