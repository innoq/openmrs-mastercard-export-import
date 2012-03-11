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
import java.util.StringTokenizer;

public class IEncounter extends IBaseData {
	
	public Date encounterDatetime = null;
	
	public Integer locationId = null;
	
	public Integer encounterTypeId = null;
	
	public String providerName = null;
	
	public List<IObs> obses = new ArrayList<IObs>();
	
	@DeserializingHint(fieldName = "obses")
	public IObs obs(@DeserializingHint(fieldName = "conceptId") Integer conceptId) {
		for (IObs obs : obses) {
			if (obs.conceptId.equals(conceptId)) {
				return obs;
			}
		}
		IObs obs = new IObs();
		obs.conceptId = conceptId;
		obses.add(obs);
		return obs;
	}
	
	public String obsesAsString(Integer conceptId) {
		String result = "";
		for (IObs obs : obses) {
			if (obs.conceptId.equals(conceptId)) {
				result += obs.valueAsString + "|";
			}
		}
		return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
	}
	
	public String obsesAsValueCodedList(Integer conceptId) {
		String result = "";
		for (IObs obs : obses) {
			if (obs.conceptId.equals(conceptId)) {
				result += obs.valueAsString + "|";
			}
		}
		return (result.lastIndexOf("|") > 0 ? result.substring(0, result.length() - 1) : result);
	}
	
	public void obsesAsValueCodedList(Integer conceptId, String valueCodedObses) {
		StringTokenizer st = new StringTokenizer(valueCodedObses, "|");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			IObs obs = new IObs();
			obs.conceptId = conceptId;
			obs.valueCodedConceptName = token;
			obses.add(obs);
		}
	}
}
