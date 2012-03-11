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
import java.util.List;
import java.util.Properties;

import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientcardexchange.model.IPatient;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Importer {
	
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
			
			new Importer().run();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void run() throws Exception {
		importMe(Context.getPatientService().getPatientIdentifierTypeByName("HCC Number"),
		    "/Users/xian/projects/pih/openmrs-mastercard-export-import/src/Pre-ART_PatientCard.csv", "LWAN ", " HCC",
		    "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/", "");
		//			List<String[]> csvPatient = new CsvSerializer().serialize(patient, csvTemplate);
		//			writeCsvPatient(
		//			    csvPatient,
		//			    "/Users/xian/projects/pih/openmrs-mastercard-export-import/export/"
		//			            + patient.identifier(identifierType.getPatientIdentifierTypeId()).identifier + " new.csv");
	}
	
	private void importMe(PatientIdentifierType identifierType, String template, String identifierPrefix,
	                      String identifierPostfix, String exportPath, String importFilePostfix) throws Exception,
	                                                                                            IOException {
		List<String[]> csvTemplate = readCsvTemplate(template);
		List<IPatient> patients = new ArrayList<IPatient>();
		
		for (int i = 1; i < 2; i++) {
			try {
				List<String[]> csvPatient = readCsvPatient(exportPath + identifierPrefix + +i + identifierPostfix
				        + importFilePostfix + ".csv");
				IPatient srcPatient = new CsvDeserializer().deserialize(csvPatient, csvTemplate);
				patients.add(srcPatient);
			}
			catch (Exception e) {
				System.out.println("Something went wrong with " + i + ".csv");
			}
		}
		for (IPatient patient : patients) {
			new ConverterToPatient().convert(patient, jdbcConnection);
		}
	}
	
	private void writeCsvPatient(List<String[]> csvPatient, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		CSVWriter w = new CSVWriter(fw, ';');
		w.writeAll(csvPatient);
		w.close();
	}
	
	private List<String[]> readCsvPatient(String filename) throws IOException {
		FileReader fr = new FileReader(filename);
		CSVReader r = new CSVReader(fr, ';');
		List<String[]> p = r.readAll();
		r.close();
		return p;
	}
	
	private List<String[]> readCsvTemplate(String csvTemplateFile) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(csvTemplateFile), ';', '"', 0);
		List<String[]> csvTemplate = reader.readAll();
		return csvTemplate;
	}
}
