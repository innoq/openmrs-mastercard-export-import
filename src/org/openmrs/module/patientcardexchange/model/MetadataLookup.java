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

import java.util.HashMap;
import java.util.Map;

public class MetadataLookup {
	
	Map<Integer, String> concepts = new HashMap<Integer, String>();
	
	Map<Integer, String> programs = new HashMap<Integer, String>();
	
	Map<Integer, String> locations = new HashMap<Integer, String>();
	
	Map<Integer, String> programWorkflows = new HashMap<Integer, String>();
	
	Map<Integer, String> programWorkflowStates = new HashMap<Integer, String>();
}
