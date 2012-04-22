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
import java.util.Date;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Encounter;
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
import org.openmrs.module.patientcardexchange.model.MetadataLookup;

public class ConverterToPatient {
	
	public void convert(IPatient src, Connection jdbcConnection) {
		Patient target = Context.getPatientService().getPatientByUuid(src.uuid);
		boolean newPatient = false;
		if (target == null) {
			// assume that for existing patients we already have the stuff
			newPatient = true;
			target = new Patient();
			convertBaseData(src, target);
			target.setBirthdate(src.birthdate);
			target.setBirthdateEstimated(src.birthdateEstimated);
			target.setCauseOfDeath(MetadataLookup.lookupConcept(src.causeOfDeathConceptId));
			target.setDead(src.dead);
			target.setDeathDate(src.deathDate);
			target.setGender(src.gender);
			for (IPatientIdentifier srcPi : src.identifiers) {
				PatientIdentifier targetPi = new PatientIdentifier();
				convertBaseData(srcPi, targetPi);
				targetPi.setIdentifier(srcPi.identifier);
				targetPi.setIdentifierType(MetadataLookup.lookupPatientIdentifierType(srcPi.identifierTypeId));
				targetPi.setLocation(MetadataLookup.lookupLocation(srcPi.locationId));
				targetPi.setPreferred(srcPi.preferred);
				targetPi.setPatient(target);
				// todo, hack to convert old pre-art identifiers to make them unique
				if (srcPi.identifierTypeId == 5) {
					targetPi.setIdentifier(srcPi.identifier + "-LSI");
				}
				target.addIdentifier(targetPi);
			}
			for (IPatientAddress srcPa : src.addresses) {
				PersonAddress targetPa = new PersonAddress();
				//			convertBaseData(srcPa, targetPa);
				targetPa.setAddress1(srcPa.address1);
				targetPa.setAddress2(srcPa.address2);
				targetPa.setCityVillage(srcPa.cityVillage);
				targetPa.setCountyDistrict(srcPa.countyDistrict);
				targetPa.setPreferred(srcPa.preferred);
				target.addAddress(targetPa);
			}
			for (IPatientName srcName : src.names) {
				PersonName targetName = new PersonName();
				//			convertBaseData(srcName, targetName);
				targetName.setFamilyName(srcName.familyName);
				targetName.setGivenName(srcName.givenName);
				targetName.setMiddleName(srcName.middleName);
				targetName.setPreferred(srcName.preferred);
				targetName.setPerson(target);
				target.addName(targetName);
			}
			Context.getPatientService().savePatient(target);
			Obs notesObs = new Obs();
			notesObs.setObsDatetime(new Date());
			notesObs.setConcept(MetadataLookup.lookupConcept(7756));
			notesObs.setValueText("Imported through openmrs-mastercard-export-import");
			notesObs.setPerson(target);
			Context.getObsService().saveObs(notesObs, null);
		}
		if (newPatient) {
			// assume that for existing patients we already have the stuff
			for (IPatientProgram srcPp : src.patientPrograms) {
				PatientProgram targetPp = new PatientProgram();
				//			convertBaseData(srcPp, targetPp);
				targetPp.setDateCompleted(srcPp.dateCompleted);
				targetPp.setDateEnrolled(srcPp.dateEnrolled);
				targetPp.setProgram(MetadataLookup.lookupProgram(srcPp.programId));
				targetPp.setPatient(target);
				for (IPatientState srcState : srcPp.states) {
					// TODO TODO TODO
					System.err.println("Dont mess around with the order of patient states like we currently do here. Otherwise there can be a more recent patient_state with a lower patient_state number. This can mess around at least with some data quality reports.");
					PatientState targetState = new PatientState();
					//				convertBaseData(srcState, targetState);
					targetState.setEndDate(srcState.endDate);
					targetState.setState(MetadataLookup.lookupProgramWorkflowState(srcState.programWorkflowStateId));
					targetState.setStartDate(srcState.startDate);
					targetPp.getStates().add(targetState);
				}
				targetPp = Context.getProgramWorkflowService().savePatientProgram(targetPp);
				Util.setEnrollmentLocation(targetPp, MetadataLookup.lookupLocation(srcPp.locationId), jdbcConnection);
			}
		}
		
		for (IEncounter srcEncounter : src.encounters) {
			Encounter targetEncounter = new Encounter();
			convertBaseData(srcEncounter, targetEncounter);
			targetEncounter.setEncounterDatetime(srcEncounter.encounterDatetime);
			targetEncounter.setEncounterType(MetadataLookup.lookupEncounterType(srcEncounter.encounterTypeId));
			targetEncounter.setLocation(MetadataLookup.lookupLocation(srcEncounter.locationId));
			targetEncounter.setPatient(target);
			// for now simply set a default
			targetEncounter.setProvider(Util.unknownProvider());
			targetEncounter.setForm(MetadataLookup.formForEncounterType(srcEncounter.encounterTypeId));
			for (IObs srcObs : srcEncounter.obses) {
				Obs targetObs = new Obs();
				convertBaseData(srcObs, targetObs);
				targetObs.setConcept(MetadataLookup.lookupConcept(srcObs.conceptId));
				targetObs.setDateStarted(srcObs.dateStarted);
				targetObs.setDateStopped(srcObs.dateStopped);
				targetObs.setLocation(MetadataLookup.lookupLocation(srcObs.locationId));
				targetObs.setObsDatetime(srcObs.obsDatetime);
				targetObs.setValueCoded(MetadataLookup.lookupConcept(srcObs.valueCodedConceptId));
				targetObs.setValueComplex(srcObs.valueComplex);
				targetObs.setValueDatetime(srcObs.valueDatetime);
				targetObs.setValueModifier(srcObs.valueModifier);
				targetObs.setValueNumeric(srcObs.valueNumeric);
				targetObs.setValueText(srcObs.valueText);
				targetEncounter.addObs(targetObs);
			}
			Obs notesObs = new Obs();
			notesObs.setObsDatetime(new Date());
			notesObs.setConcept(MetadataLookup.lookupConcept(7756));
			notesObs.setValueText("Imported through openmrs-mastercard-export-import");
			targetEncounter.addObs(notesObs);
			Context.getEncounterService().saveEncounter(targetEncounter);
		}
		
		System.out.println("Imported patient: " + target.getId());
	}
	
	private void convertBaseData(IBaseData src, BaseOpenmrsData target) {
		//		target.changedById = (src.getChangedBy() != null ? src.getChangedBy().getUserId() : null);
		//		target.creatorId = (src.getCreator() != null ? src.getCreator().getUserId() : null);
		//		target.dateChanged = src.getDateChanged();
		//		target.dateCreated = src.getDateCreated();
		//		target.dateVoided = src.getDateVoided();
		target.setUuid(src.uuid);
		//		target.voided = src.getVoided();
		//		target.voidedById = (src.getVoidedBy() != null ? src.getVoidedBy().getUserId() : null);
		//		target.voidReason = src.getVoidReason();
	}
}
