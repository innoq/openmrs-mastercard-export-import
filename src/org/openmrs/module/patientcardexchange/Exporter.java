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

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientcardexchange.model.IPatient;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Exporter {
	
	private static Connection jdbcConnection = null;
	
	public static void main(String[] args) {
		try {
			// parameters
			String openmrsRuntimeProperties = args[0];
			String openmrsUser = args[1];
			String openmrsPw = args[2];
			
			// properties
			Properties prop = new Properties();
			prop.load(new FileInputStream(openmrsRuntimeProperties));
			String connectionUser = prop.getProperty("connection.username");
			String conncetionPw = prop.getProperty("connection.password");
			String conncetionUrl = prop.getProperty("connection.url");
			
			// connection init
			Context.startup(conncetionUrl, connectionUser, conncetionPw, prop);
			Context.openSession();
			Context.authenticate(openmrsUser, openmrsPw);
			
			try {
				Properties connectionProps = new Properties();
				connectionProps.put("user", connectionUser);
				connectionProps.put("password", conncetionPw);
				jdbcConnection = DriverManager.getConnection(conncetionUrl, connectionProps);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			new Exporter().run();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void run() throws Exception {
		EncounterType exposedInitial = Context.getEncounterService().getEncounterType("EXPOSED_CHILD_INITIAL");
		EncounterType exposedFollowup = Context.getEncounterService().getEncounterType("EXPOSED_CHILD_FOLLOWUP");
		EncounterType partInitial = Context.getEncounterService().getEncounterType("PART_INITIAL");
		EncounterType partFollowup = Context.getEncounterService().getEncounterType("PART_FOLLOWUP");
		EncounterType artInitial = Context.getEncounterService().getEncounterType("ART_INITIAL");
		EncounterType artFollowup = Context.getEncounterService().getEncounterType("ART_FOLLOWUP");
		PatientIdentifierType arvIdentifierType = Context.getPatientService().getPatientIdentifierTypeByName("ARV Number");
		PatientIdentifierType hccIdentifierType = Context.getPatientService().getPatientIdentifierTypeByName("HCC Number");
		
		Map<Integer, Patient> patients = new HashMap<Integer, Patient>();
		
		// collect all patients that ever have been registered in LWAN for HCC or ART 
//		String identifierPrefix = "LWAN .*";
//		List<Patient> potentialPatient = Context.getPatientService().getPatients(null, identifierPrefix,
//		    Arrays.asList(hccIdentifierType), false);
//		for (Patient p : potentialPatient) {
//			patients.put(p.getId(), p);
//		}
//		potentialPatient = Context.getPatientService().getPatients(null, identifierPrefix, Arrays.asList(arvIdentifierType),
//		    false);
//		for (Patient p : potentialPatient) {
//			patients.put(p.getId(), p);
//		}

		// get patients from a list of patient ids (e.g. created from sync_record entries)
		// 1. Dump sync_record with encounters
		// select * from sync_record where contained_classes like '%ncounter%';
		// 2. Filter MySQL export of sync_record table
		// grep "<SyncItem containedType=\"org.openmrs.Encounter\"" a | sed "s/^.*<SyncItem containedType=\"org.openmrs.Encounter\"//g" | sed "s/state.*$//g"
		List<Integer> patientIds = Arrays.asList(22885, 18671, 22936, 22599, 22665, 18039, 21411, 21727, 22724, 18427, 22993, 22784, 21671, 20931, 22179, 21785, 20839, 23023, 21780, 18080, 21941, 17853, 22664, 21942, 18186, 22881, 22782, 22149, 21192, 22181, 22886, 21607, 22883, 22886, 21409, 21717, 23023, 22725, 19115, 18831, 23024, 18231, 22847, 22788, 22298, 17853, 22602, 18097, 21783, 21845, 22751, 22663, 22749, 22466, 21341, 22256, 19206, 22722, 22884, 21726, 21943, 22280, 18617, 22722, 22782, 23024, 22847);
		for (int patientId : patientIds) {
			patients.put(patientId, Context.getPatientService().getPatient(patientId));
		}
		
		// get all exposed, pre-art, and art history for all these patients
		for (Patient p : patients.values()) {
			// Exposed encounters
			List<Encounter> encounters = Context.getEncounterService().getEncounters(p, null, null, null, null,
			    Arrays.asList(exposedFollowup, exposedInitial), null, false);
			if (encounters.size() > 0) {
				export(Arrays.asList(p), exposedInitial, exposedFollowup, hccIdentifierType,
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/src/Exposed-Child_PatientCard.csv",
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/exposed/", "");
			}
			// part encounters
			encounters = Context.getEncounterService().getEncounters(p, null, null, null, null,
			    Arrays.asList(partFollowup, partInitial), null, false);
			if (encounters.size() > 0) {
				export(Arrays.asList(p), partInitial, partFollowup, hccIdentifierType,
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/src/Pre-ART_PatientCard.csv",
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/part/", "");
			}
			// art encounters
			encounters = Context.getEncounterService().getEncounters(p, null, null, null, null,
			    Arrays.asList(artFollowup, artInitial), null, false);
			if (encounters.size() > 0) {
				export(Arrays.asList(p), artInitial, artFollowup, arvIdentifierType,
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/src/ART_PatientCard.csv",
				    "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/art/", "");
			}
		}
	}
	
	private void export(List<Patient> patients, EncounterType initial, EncounterType followup,
	                    PatientIdentifierType identifierType, String template, String exportPath, String exportFilePostfix)
	                                                                                                                       throws Exception,
	                                                                                                                       IOException {
		List<String[]> csvTemplate = readCsvTemplate(template);
		for (Patient srcPatient : patients) {
			IPatient patient = convertPatient(srcPatient, initial, followup);
			List<String[]> csvPatient = new CsvSerializer().serialize(patient, csvTemplate);
			writeCsvPatient(csvPatient, exportPath
			        + patient.identifier(identifierType.getPatientIdentifierTypeId()).identifier + exportFilePostfix
			        + ".csv");
		}
	}
	
	private IPatient convertPatient(Patient patient, EncounterType initialEncounterType, EncounterType followupEncounterType) {
		return new ConverterFromPatient().convert(patient, initialEncounterType, followupEncounterType, jdbcConnection);
	}
	
	private void writeCsvPatient(List<String[]> csvPatient, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		CSVWriter w = new CSVWriter(fw, ';');
		w.writeAll(csvPatient);
		w.close();
	}
	
	private List<String[]> readCsvTemplate(String csvTemplateFile) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(csvTemplateFile), ';', '"', 0);
		List<String[]> csvTemplate = reader.readAll();
		return csvTemplate;
	}
	
}
