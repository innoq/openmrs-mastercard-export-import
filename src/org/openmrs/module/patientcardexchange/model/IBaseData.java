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

import org.openmrs.module.patientcardexchange.Util;

public abstract class IBaseData  {
	
	public Integer id = null;
	
	public String uuid = null;
	
	public Integer creatorId = null;
	
	public Date dateCreated = null;
	
	public Integer changedById = null;
	
	public Date dateChanged = null;
	
	public Boolean voided  = null;
	
	public Date dateVoided= null;
	
	public Integer voidedById = null;
	
	public String voidReason =null;
	
	protected String formatDate(Date date) {
		return Util.formatDate(date);
	}
	
	protected Date parseDate(String date) {
		return Util.parseDate(date);
	}
	
	protected boolean isEmpty(String cell) {
		return Util.isEmpty(cell);
	}
	
	protected boolean isNotEmpty(String remainingExpression) {
		return Util.isNotEmpty(remainingExpression);
	}
	
}
