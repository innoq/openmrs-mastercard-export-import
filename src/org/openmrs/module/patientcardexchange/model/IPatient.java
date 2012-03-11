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
package org.openmrs.module.patientcardexchange.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IPatient extends IBaseData {
	
	public List<IPatientAddress> addresses = new ArrayList<IPatientAddress>();;
	
	@DeserializingHint(fieldName = "addresses")
	public IPatientAddress preferredAddress() {
		for (IPatientAddress adr : addresses) {
			if (adr.preferred) {
				return adr;
			}
		}
		if (!addresses.isEmpty()) {
			return addresses.get(0);
		}
		IPatientAddress adr = new IPatientAddress();
		adr.preferred = true;
		addresses.add(adr);
		return adr;
	}
	
	public List<IPatientName> names = new ArrayList<IPatientName>();
	
	@DeserializingHint(fieldName = "names")
	public IPatientName preferredName() {
		for (IPatientName name : names) {
			if (name.preferred) {
				return name;
			}
		}
		if (!names.isEmpty()) {
			return names.get(0);
		}
		IPatientName name = new IPatientName();
		name.preferred = true;
		names.add(name);
		return name;
	}
	
	// todo, needed?
	//	Set<PersonAttribute> attributes = null;
	
	public String gender = "";
	
	public Date birthdate = null;
	
	public Boolean birthdateEstimated = false;
	
	public String birthdateWithEstimation() {
		return formatDate(birthdate) + (birthdateEstimated ? " (Est.)" : "");
	}
	
	public void birthdateWithEstimation(String date) {
		if (isNotEmpty(date) && date.endsWith(" (Est.)")) {
			birthdateEstimated = true;
			birthdate = parseDate(date.substring(0, date.length() - " (Est.)".length()));
		} else if (isNotEmpty(date)) {
			birthdate = parseDate(date);
			birthdateEstimated = false;
		}
	}
	
	public Boolean dead = false;
	
	public Date deathDate = null;
	
	public Integer causeOfDeathConceptId = null;
	
	public List<IPatientIdentifier> identifiers = new ArrayList<IPatientIdentifier>();
	
	@DeserializingHint(fieldName = "identifiers")
	public IPatientIdentifier identifier(@DeserializingHint(fieldName = "identifierTypeId") Integer identifierTypeId) {
		for (IPatientIdentifier pi : identifiers) {
			if (pi.identifierTypeId.equals(identifierTypeId)) {
				// todo, find how how to get most recent number across all locations
				return pi;
			}
		}
		IPatientIdentifier pi = new IPatientIdentifier();
		pi.identifierTypeId = identifierTypeId;
		identifiers.add(pi);
		return pi;
	}
	
	public String identifiersAsString() {
		String result = "";
		for (IPatientIdentifier pi : identifiers) {
			result += pi.identifier + ", " + pi.locationId + ", " + pi.identifierTypeId + ", " + pi.uuid + "|";
		}
		return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
	}
	
	public List<IEncounter> encounters = new ArrayList<IEncounter>();
	
	@DeserializingHint(fieldName = "encounters")
	public IEncounter encounter(@DeserializingHint(fieldName = "encounterTypeId") Integer encounterTypeId) {
		for (IEncounter e : encounters) {
			if (e.encounterTypeId.equals(encounterTypeId)) {
				// todo, figure out what do to if there are more than 1 encounters of this type
				return e;
			}
		}
		IEncounter e = new IEncounter();
		e.encounterTypeId = encounterTypeId;
		encounters.add(e);
		return e;
	}
	
	@DeserializingHint(fieldName = "encounters")
	public List<IEncounter> encounters(@DeserializingHint(fieldName = "encounterTypeId") Integer encounterTypeId) {
		List<IEncounter> es = new ArrayList<IEncounter>();
		for (IEncounter e : encounters) {
			if (e.encounterTypeId.equals(encounterTypeId)) {
				es.add(e);
			}
		}
		return es;
	}
	
	public List<IPatientProgram> patientPrograms = new ArrayList<IPatientProgram>();
}
