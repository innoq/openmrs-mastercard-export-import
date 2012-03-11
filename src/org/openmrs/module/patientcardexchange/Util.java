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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openmrs.Location;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.api.context.Context;

public class Util {

	public static boolean isNotEmpty(String string) {
		return string != null && !string.equals("");
	}
	
	public static boolean isEmpty(String cell) {
		return cell == null || "".equals(cell);
	}
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("dd-MMM-yyyy").format(date);
	}
	
	public static Date parseDate(String date) {
		if (isEmpty(date)) {
			return null;
		}
		try {
	        return new SimpleDateFormat("dd-MMM-yyyy").parse(date);
        }
        catch (ParseException e) {
        	e.printStackTrace();
        }
        return null;
	}

	public static Location getEnrollmentLocation(PatientProgram pp, Connection con) {
		// you gained the right to kill me once you see this...
		String sql = "select location_id from patient_program where patient_program_id = " + pp.getId();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return Context.getLocationService().getLocation(rs.getInt(1));
			}
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}

	public static void setEnrollmentLocation(PatientProgram pp, Location loc, Connection con) {
		// you gained the right to kill me once you see this...
		String sql = "update patient_program set location_id = " + loc.getId() + " where patient_program_id = " + pp.getId();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

    public static Person unknownProvider() {
    	return Context.getPersonService().getPerson(16576);
    }


}
