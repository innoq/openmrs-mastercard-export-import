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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientcardexchange.model.IPatient;
import org.openmrs.module.patientcardexchange.model.IPatientName;

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
				jdbcConnection  = DriverManager
				        .getConnection(
				        	conncetionUrl,
				            connectionProps);
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
	
	public void run() throws Exception {
		EncounterType initial = Context.getEncounterService().getEncounterType("ART_INITIAL");
		EncounterType followup = Context.getEncounterService().getEncounterType("ART_FOLLOWUP");
		PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierTypeByName("ARV Number");
		
		List<String[]> csvTemplate = readCsvTemplate("/Users/xian/projects/pih/openmrs-mastercard-export-import/src/ART_PatientCard.csv");
		List<Patient> patients = new ArrayList<Patient>();
		for (int i = 1; i < 3; i++) {
//			convertPatient(/*44961*/15889, initial, followup);
			patients.addAll(Context.getPatientService().getPatients(null, "LWAN " + i, Arrays.asList(identifierType), true));
		}
		for (Patient srcPatient : patients) {
			IPatient patient = convertPatient(srcPatient, initial, followup);
			List<String[]> csvPatient = new CsvSerializer().serialize(patient, csvTemplate);
			writeCsvPatient(csvPatient, "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/" + patient.identifier(identifierType.getPatientIdentifierTypeId()).identifier + ".csv");
		}
	}
	
    private IPatient convertPatient(Patient patient, EncounterType initialEncounterType, EncounterType followupEncounterType) {
    	return new Converter().convert(patient, initialEncounterType, followupEncounterType, jdbcConnection);
    }

    private IPatient convertPatient(int patientId, EncounterType initialEncounterType, EncounterType followupEncounterType) {
    	return new Converter().convert(Context.getPatientService().getPatient(patientId), initialEncounterType, followupEncounterType, jdbcConnection);
    }

	private void writeCsvPatient(List<String[]> csvPatient, String filename) throws IOException {
    	FileWriter fw = new FileWriter(filename);
    	CSVWriter w = new CSVWriter(fw, ';');
    	w.writeAll(csvPatient);
    	w.close();
    }

	private IPatient fillTestPatient(int i) {
		IPatient p = new IPatient();
		IPatientName name = new IPatientName();
		name.givenName = "given";
		name.familyName = "family";
		p.names.add(name);
		p.birthdate = new Date();
		return p;
	}
	
	private List<String[]> readCsvTemplate(String csvTemplateFile) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(csvTemplateFile), ';', '"', 0);
		List<String[]> csvTemplate = reader.readAll();
		return csvTemplate;
		//return new String[][] { { "Firstname", "#patient.names(0)#", "\n" }, { "Birthdate", "#patient.birthdate#", "\n" } };
	}
	
}
