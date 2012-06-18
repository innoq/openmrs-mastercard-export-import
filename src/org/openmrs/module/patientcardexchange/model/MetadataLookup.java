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

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.context.Context;

public class MetadataLookup {
	
	Map<Integer, String> concepts = new HashMap<Integer, String>();
	
	Map<Integer, String> programs = new HashMap<Integer, String>();
	
	Map<Integer, String> locations = new HashMap<Integer, String>();
	
	Map<Integer, String> programWorkflows = new HashMap<Integer, String>();
	
	Map<Integer, String> programWorkflowStates = new HashMap<Integer, String>();

    public static Concept lookupConcept(Integer conceptId) {
    	return (conceptId != null ? Context.getConceptService().getConcept(conceptId) : null);
    }

    public static EncounterType lookupEncounterType(Integer encounterTypeId) {
    	return (encounterTypeId != null ? Context.getEncounterService().getEncounterType(encounterTypeId) : null);
    }

    public static Location lookupLocation(Integer locationId) {
    	return (locationId != null ? Context.getLocationService().getLocation(locationId) : null);
    }

    public static PatientIdentifierType lookupPatientIdentifierType(Integer identifierTypeId) {
    	return (identifierTypeId != null ? Context.getPatientService().getPatientIdentifierType(identifierTypeId) : null);
    }

    public static Program lookupProgram(Integer programId) {
    	return (programId != null ? Context.getProgramWorkflowService().getProgram(programId) : null);
    }

    public static ProgramWorkflowState lookupProgramWorkflowState(Integer programWorkflowStateId) {
    	return (programWorkflowStateId != null ? Context.getProgramWorkflowService().getState(programWorkflowStateId) : null);  
    }
    
    public static Form formForEncounterType(Integer encounterTypeId) {
    	int formId = -1;
    	switch (encounterTypeId) {
       		case 9: // ART_INITIAL
    			formId = 64;
    			break;
    		case 10: // ART_FOLLOWUP
    			formId = 65;
    			break;
       		case 11: // PART_INITIAL
    			formId = 66;
    			break;
    		case 12: // PART_FOLLOWUP
    			formId = 67;
    			break;
    		case 92: // EXPOSED_INITIAL
    			formId = 68;
    			break;
    		case 93: // EXPOSED_FOLLOWUP
    			formId = 69;
    			break;
    	}
	    return Context.getFormService().getForm(formId);
    }

}
