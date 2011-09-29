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

/**
 *
 */
public class ArvMastercardBean {
	
	private String identifier = null;
	
	InitialEncounter headerData = null;
	
	FollowerEncounter[] encounterData = null;
	
	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * @return the headerData
	 */
	public InitialEncounter getHeaderData() {
		return headerData;
	}
	
	/**
	 * @param headerData the headerData to set
	 */
	public void setHeaderData(InitialEncounter headerData) {
		this.headerData = headerData;
	}
	
	/**
	 * @return the encounterData
	 */
	public FollowerEncounter[] getEncounterData() {
		return encounterData;
	}
	
	/**
	 * @param encounterData the encounterData to set
	 */
	public void setEncounterData(FollowerEncounter[] encounterData) {
		this.encounterData = encounterData;
	}
}
