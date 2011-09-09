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
package org.openmrs.module.mastercard;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.mastercard.exceptions.WrongFormatException;
import org.openmrs.module.mastercard.entities.EncounterData;
import org.openmrs.module.mastercard.entities.HeaderData;
import org.openmrs.module.mastercard.entities.ObservationDataBean;

/**
 *
 */
public class ArtImporterTest {
	
	String[] stringArray1 = null;
	
	String[] stringArray2 = null;
	
	@Before
	public void setUp() throws Exception {
		stringArray1 = new String[10];
		stringArray1[0] = "";
		stringArray1[1] = "Outcome NNO;ON ANTIRETROVIRALS;at location;todo";
		stringArray1[2] = "ART no;NNO 1;OpenMRS ID;15889";
		stringArray1[3] = "";
		stringArray1[4] = "Patient Guardian details;;;;;;Status at ART initiation;;;;;;First positive HIV test;";
		stringArray1[5] = "Patient name;Dambudzo/Njolomole;;;;;Clin Stage;3;;;TB Status at initiation;-;Date, Place;01 Jul 2005/QECH;Type;-;";
		stringArray1[6] = "Sex;M;DOB;09 Jun 1999;Patient phone;-;CD4 count;-;%;-;KS;-;ART Regimen;;Start date;-;";
		stringArray1[7] = "Phys. Address;-;;;;;CD4 date;-;;;Pregnant at initiation;-;1st Line;d4T 3TC NVP;-";
		stringArray1[8] = "Guardian Name;Njolomole / Watson;;;;;Height;-;Weight;17.8;Ever taken ARVs;-;Alt 1st Line;-;-";
		stringArray1[9] = "Agrees to FUP;YES;Guardian Relation;-;Guardian Phone;-;Age at init.;-;;;Last ARVs (drug, date);-;2nd Line;-;-;;Unknown Obs;-";
		
		stringArray2 = new String[10];
		stringArray2[0] = "Visit loc;Vist Date;Hgt;Wt;Outcome Enrollment;Adverse Outcome;Outcome date;Regimen;Side Effects;TB status;current Pill count;Doses missed;ARVs given #;To;CPT #;Comment;Next appointment;Unknown Obs;";
		stringArray2[1] = "";
		stringArray2[2] = "NNO;29 Jun 2006;-;17.8;-;outcome;-;arvReg;-;-;-;-;60.0;60.0;-;-;-;-;";
		stringArray2[3] = "NNO;03 Aug 2006;-;18.3;-;outcome;-;arvReg;-;-;-;-;60.0;60.0;-;-;-;-;";
		stringArray2[4] = "NNO;15 Feb 2007;110.0;19.1;-;outcome;-;arvReg;-;-;8.0;-;39.0;39.0;-;-;-;-;";
		stringArray2[5] = "NNO;27 Nov 2007;118.0;20.9;-;outcome;-;arvReg;-;-;4.0;-;39.0;39.0;-;-;-;-;";
		stringArray2[6] = "NNO;22 May 2008;122.0;22.9;-;outcome;-;arvReg;-;-;-;-;120.0;120.0;-;-;19 Jun 2008;-;";
		stringArray2[7] = "NNO;28 Apr 2009;-;25.2;-;outcome;-;arvReg;No;-;-;-;165.0;165.0;-;-;21 Jul 2009;-;";
		stringArray2[8] = "NNO;20 Jan 2011;132.0;28.4;-;outcome;-;arvReg;-;-;8.0;-;165.0;165.0;-;-;14 Apr 2011;-;";
		stringArray2[9] = "NNO;26 Apr 2011;138.0;30.2;-;outcome;-;arvReg;-;TB NOT SUSPECTED;0.0;-;180.0;180.0;-;-;18 Jul 2011;-;";
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.mastercard.ArtImporter#parseArrayForHeaderData(java.lang.String[])}
	 * .
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void testParseArrayForHeaderData1() throws WrongFormatException {
		ArtImporter artImp = new ArtImporter();
		HeaderData headerData = artImp.parseArrayForHeaderData(stringArray1);
		
		ObservationDataBean obs = headerData.getObservations();
		assertEquals("ON ANTIRETROVIRALS", obs.getOutcome());
		assertEquals("QECH", obs.getLocationWhereTestTookPlace());
		assertEquals("YES", obs.getFup());
		
		assertEquals("-", obs.getAlt1stLDate());
		
		assertEquals("01 Jul 2005", obs.getDateOfHiVDiagnosis());
		assertEquals("QECH", obs.getLocationWhereTestTookPlace());
	}
	
	/**
	 * Test method for
	 * {@link org.openmrs.module.mastercard.ArtImporter#parseArrayForHeaderData(java.lang.String[])}
	 * .
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void testParseArrayForEncounterData() throws WrongFormatException {
		ArtImporter artImp = new ArtImporter();
		EncounterData[] encounterDataArray = artImp.parseArrayForEncounterData(stringArray2);
		
		EncounterData encounterData = encounterDataArray[0];
		//"Visit loc;Vist Date;Hgt;Wt;Outcome Enrollment;Adverse Outcome;Outcome date;Regimen;Side Effects;TB status;current Pill count;Doses missed;ARVs given #;To;CPT #;Comment;Next appointment;Unknown Obs;";
		//"NNO;29 Jun 2006;-;17.8;-;outcome;-;arvReg;-;-;-;-;60.0;60.0;-;-;-;-;";
		ObservationDataBean obs = encounterData.getObservations();
		assertEquals("29 Jun 2006", encounterData.getDateOfEncounter());
		assertEquals("-", obs.getHgt());
		assertEquals("17.8", obs.getWgt());
		assertEquals("outcome", obs.getOutcome());
	}
}
