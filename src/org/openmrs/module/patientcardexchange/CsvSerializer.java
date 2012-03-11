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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.openmrs.module.patientcardexchange.model.IBaseData;
import org.openmrs.module.patientcardexchange.model.IEncounter;
import org.openmrs.module.patientcardexchange.model.IPatient;
import org.openmrs.module.patientcardexchange.model.IPatientIdentifier;
import org.openmrs.module.patientcardexchange.model.IPatientProgram;
import org.openmrs.module.patientcardexchange.model.IPatientState;

public class CsvSerializer {
	
	public List<String[]> serialize(IPatient patient, List<String[]> csvTemplate) {
		List<String[]> csv = new ArrayList<String[]>();
		
		for (String[] row : csvTemplate) {
			if (isRepeatProgramEnrollments(row)) {
				// todo, little hack to get the data at least somehow; ideally it should be part of the encounters themselves
				for (IPatientProgram program : patient.patientPrograms) {
					List<String> rowColumns = serializeProgramEnrollment(row, program);
					csv.add((String[]) rowColumns.toArray(new String[rowColumns.size()]));
				}
			} else if (isRepeatPatientIdentifiers(row)) {
				// todo, little hack to get the data at least somehow
				for (IPatientIdentifier identifier : patient.identifiers) {
					List<String> rowColumns = serializePatientIdentifier(row, identifier);
					csv.add((String[]) rowColumns.toArray(new String[rowColumns.size()]));
				}
			} else if (isRepeatEncounterRow(row)) {
				// scope of followup encounter
				// i know there is a better way to handle repeating encounters
				for (IEncounter encounter : patient.encounters(Integer.parseInt("" + parseArgs(row[0])[0]))) {
					List<String> rowColumns = serializeFollowupEncounter(row, encounter);
					csv.add((String[]) rowColumns.toArray(new String[rowColumns.size()]));
				}
			} else {
				// scope of the current patient
				List<String> rowColumns = new ArrayList<String>();
				for (String col : row) {
					String cell = col;
					if (isExpression(col)) {
						String expression = col.substring(1, col.length() - 1);
						cell = evaluateExpression(patient, expression);
					}
					rowColumns.add(cell);
				}
				csv.add((String[]) rowColumns.toArray(new String[rowColumns.size()]));
			}
		}
		return csv;
	}

	private List<String> serializeFollowupEncounter(String[] row, IEncounter encounter) {
	    List<String> rowColumns = new ArrayList<String>();
	    for (String col : row) {
	    	String cell = col;
	    	if (isExpression(col)) {
	    		String expression = col.substring(1, col.length() - 1);
	    		cell = evaluateExpression(encounter, expression);
	    	}
	    	rowColumns.add(cell);
	    }
	    return rowColumns;
    }

	private List<String> serializeProgramEnrollment(String[] row, IPatientProgram program) {
	    List<String> rowColumns = new ArrayList<String>();
	    // for the first cell put in the tag again
	    rowColumns.add(row[0]);
	    // now the program details
	    rowColumns.add("" + program.programId);
	    rowColumns.add(formatDate(program.dateEnrolled));
	    rowColumns.add(formatDate(program.dateCompleted));
	    rowColumns.add("" + program.locationId);
	    // group states by workflow
	    Map<Integer, List<IPatientState>> workflowsWithStates = new HashMap<Integer, List<IPatientState>>();
	    for (IPatientState state : program.states) {
	    	if (workflowsWithStates.containsKey(state.programWorkflowId)) {
	    		workflowsWithStates.get(state.programWorkflowId).add(state);
	    	} else {
	    		ArrayList<IPatientState> stateList = new ArrayList<IPatientState>();
	    		stateList.add(state);
	    		workflowsWithStates.put(state.programWorkflowId, stateList);
	    	}
	    }
	    for (Integer programWorkflowId : workflowsWithStates.keySet()) {
	    	rowColumns.add("" + programWorkflowId);
	    	for (IPatientState state : workflowsWithStates.get(programWorkflowId)) {
	    		rowColumns.add("" + state.programWorkflowStateId);
	    		rowColumns.add(formatDate(state.startDate));
	    		rowColumns.add(formatDate(state.endDate));
	    	}
	    }
	    return rowColumns;
    }
	
	private List<String> serializePatientIdentifier(String[] row, IBaseData base) {
	    List<String> rowColumns = new ArrayList<String>();
	    for (String col : row) {
	    	String cell = col;
	    	if (isExpression(col)) {
	    		String expression = col.substring(1, col.length() - 1);
	    		cell = evaluateExpression(base, expression);
	    	}
	    	rowColumns.add(cell);
	    }
	    return rowColumns;
//	    List<String> rowColumns = new ArrayList<String>();
//	    // for the first cell put in the tag again
//	    rowColumns.add(row[0]);
//	    // now the identifier details
//		rowColumns.add(pi.identifier);
//		rowColumns.add("" + pi.locationId);
//		rowColumns.add("" + pi.identifierTypeId);
//		rowColumns.add(pi.uuid);
//	    return rowColumns;
    }
	
	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("dd-MMM-yyyy").format(date);
	}
	
	private boolean isEmpty(String cell) {
		return cell == null || "".equals(cell);
	}
	
	private String evaluateExpression(Object baseData, String expression) {
		String v = "";
		try {
			if (expression.startsWith("patient.")) {
				expression = expression.substring("patient.".length(), expression.length());
			}
			Class aClass = baseData.getClass();
			int fieldLength = expression.length();
			if (expression.indexOf(".") > 0) {
				fieldLength = expression.indexOf(".");
			}
			String fieldName = expression.substring(0, fieldLength);
			String remainingExpression = "";
			if (expression.indexOf(".", fieldLength) > 0) {
				remainingExpression = expression.substring(fieldLength + 1, expression.length());
			}
			if (fieldName.indexOf("(") > 0) {
				// a method
				String methodName = expression.substring(0, expression.indexOf("("));
				boolean methodFound = false;
				for (Method m : aClass.getMethods()) {
					if (m.getName().equals(methodName)) {
						Object[] args = parseArgs(expression);
						if ((args == null && m.getParameterTypes().length == 0) || 
								(args != null && m.getParameterTypes().length > 0)) {
							// todo, wrong assumption that there will only be one method with this name...
							// todo, and not only integer values are passed in as params
							Object value = m.invoke(baseData, args);
							if (isNotEmpty(remainingExpression) && value != null) {
								v = evaluateExpression(value, remainingExpression);
							} else {
								if (value instanceof Date) {
									v = formatDate((Date) value);
								} else {
									v = (value != null ? value.toString() : "");
								}
							}
							methodFound = true;
							break;
						}
					}
				}
				if (!methodFound) {
					throw new RuntimeException("Method not found: " + methodName);
				}
			} else {
				// a field
				Field field = aClass.getField(fieldName);
				
				Object value = field.get(baseData);
				if (isNotEmpty(remainingExpression)) {
					v = evaluateExpression(value, remainingExpression);
				} else if (value instanceof Date) {
					v = formatDate((Date) value);
				} else {
					v = (value != null ? value.toString() : "");
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass() + ": " + baseData + " - " + expression);
		}
		return v;
	}
	
	private Object[] parseArgs(String expression) {
		if (expression.indexOf("(") + 1 == expression.indexOf(")")) {
			// no args provided
			return null;
		}
		String args = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
		StringTokenizer st = new StringTokenizer(args);
		List<Object> t = new ArrayList<Object>();
		while (st.hasMoreTokens()) {
			t.add(new Integer(st.nextToken(",")));
		}
		return t.toArray();
	}
	
	private boolean isNotEmpty(String remainingExpression) {
		return remainingExpression != null && !remainingExpression.equals("");
	}
	
	private boolean isExpression(String cell) {
		return (cell != null && cell.startsWith("#") && cell.endsWith("#") && !cell.startsWith("#forEachEncounter(") && !cell.equals("#forEachPatientIdentifier#"));
	}
	
	private boolean isRepeatEncounterRow(String[] row) {
		return (row != null & row[0] != null && row[0].startsWith("#forEachEncounter("));
	}
	
	private boolean isRepeatProgramEnrollments(String[] row) {
		return (row != null & row[0] != null && "#printEachProgramEnrollment#".equals(row[0]));
	}
	
	private boolean isRepeatPatientIdentifiers(String[] row) {
		return (row != null & row[0] != null && "#forEachPatientIdentifier#".equals(row[0]));
	}
	
}
