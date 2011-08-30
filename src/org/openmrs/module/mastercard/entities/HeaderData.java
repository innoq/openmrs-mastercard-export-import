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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.PersonAddress;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mastercard.ArtExporter;
import org.openmrs.module.mastercard.Helper;

/**
 *
 */
public class HeaderData extends AbstractData {
	
	static Logger logger = Logger.getLogger(HeaderData.class);
	
	public HeaderData(Encounter e) {
		super(e);
	}
	
	protected String extractEncounterData(Encounter encounter) {
		logger.info("exportInitial(Encounter " + encounter.getId() + ")");
		String r = "";
		
		PatientState ps = currentProgramWorkflowStatus(1, encounter.getPatient(), new Date());
		//		ps = h.getMostRecentStateAtLocation(encounter.getPatient(), h.program("HIV PROGRAM"), h.location("Neno District Hospital"), sessionFactory().getCurrentSession());
		
		if (ps != null) {
			r += csv("Outcome NNO", ps.getState().getConcept().getName().getName(), "at location" /*, map(h.getEnrollmentLocation(ps.getPatientProgram(),
			                                                                                      sessionFactory().getCurrentSession()).getName())*/);
		} else {
			r += csv("Outcome NNO", "Unknown");
		}
		r += Constants.NEWLINE;
		
		ObservationDataBean obsDataBean = new ObservationDataBean();
		
		extractObservations(encounter, obsDataBean);
		

		
		String addr = "";
		Set<PersonAddress> addresses = encounter.getPatient().getAddresses();
		for (PersonAddress a : addresses) {
			addr += h(a.getCityVillage()) + " " + h(a.getCountyDistrict()) + ", ";
		}
		
		r += csv("ART no", obsDataBean.getArtNos(), "OpenMRS ID", obsDataBean.getPatientId().toString());
		r += Constants.NEWLINE + Constants.NEWLINE;
		r += csv("Patient Guardian details", "", "", "", "", "", "Status at ART initiation", "", "", "", "", "",
		    "First positive HIV test", "");
		r += Constants.NEWLINE;
		r += csv("Patient name", obsDataBean.getName(), "", "", "", "", "Clin Stage", obsDataBean.getStage(), "", "",
		    "TB Status at initiation", obsDataBean.getTbStat(), "Date, Place", obsDataBean.getDatePlace(), "Type",
		    obsDataBean.getType());
		r += Constants.NEWLINE;
		r += csv("Sex", obsDataBean.getSex(), "DOB", obsDataBean.getDateOfBirth(), "Patient phone", obsDataBean.getPhone(),
		    "CD4 count", obsDataBean.getCd4(), "%", obsDataBean.getCd4Percentage(), "KS", obsDataBean.getKs(),
		    "ART Regimen", "", "Start date");
		r += Constants.NEWLINE;
		r += csv("Phys. Address", obsDataBean.getAddr(), "", "", "", "", "CD4 date", obsDataBean.getCd4Date(), "", "",
		    "Pregnant at initiation", obsDataBean.getPreg(), "1st Line", "d4T 3TC NVP", obsDataBean.getD4TDate());
		r += Constants.NEWLINE;
		r += csv("Guardian Name", obsDataBean.getGuardianName(), "", "", "", "", "Height", obsDataBean.getHgt(), "Weight",
		    obsDataBean.getWgt(), "Ever taken ARVs", obsDataBean.getEverArv(), "Alt 1st Line", obsDataBean.getAlt1stL(),
		    obsDataBean.getAlt1stLDate());
		r += Constants.NEWLINE;
		r += csv("Agrees to FUP", obsDataBean.getFup(), "Guardian Relation", obsDataBean.getGrel(), "Guardian Phone",
		    obsDataBean.getGphone(), "Age at init.", obsDataBean.getAgeInit(), "", "", "Last ARVs (drug, date)",
		    obsDataBean.getLastArv(), "2nd Line", obsDataBean.getSecondL(), obsDataBean.getSecondLDate(), "", "Unknown Obs",
		    obsDataBean.getUnknownObs());
		r += Constants.NEWLINE;
		
		return r;
	}
	
}
