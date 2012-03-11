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

import java.util.Date;

public class IObs extends IBaseData {
	
	public Integer obsId = null;
	
	public Integer conceptId = null;
	
	public Date obsDatetime = null;
	
	public String valueAsString = null;
	
	public Integer valueCodedConceptId = null;

	public String valueCodedConceptName = null;

	public Date valueDatetime = null;
	
	public Double valueNumeric = null;
	
	public String valueModifier = null;
	
	public String valueText = null;
	
	public String valueComplex = null;
	
	public IPatient person = null;
	
	public Integer locationId = null;
	
	public Date dateStarted = null;
	
	public Date dateStopped = null;
}
