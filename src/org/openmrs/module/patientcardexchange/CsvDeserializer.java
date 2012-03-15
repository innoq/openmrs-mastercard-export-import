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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.openmrs.module.patientcardexchange.model.DeserializingHint;
import org.openmrs.module.patientcardexchange.model.IEncounter;
import org.openmrs.module.patientcardexchange.model.IPatient;
import org.openmrs.module.patientcardexchange.model.IPatientIdentifier;
import org.openmrs.module.patientcardexchange.model.IPatientProgram;
import org.openmrs.module.patientcardexchange.model.IPatientState;

public class CsvDeserializer {
	
	public IPatient deserialize(List<String[]> patient, List<String[]> csvTemplate) {
		try {
			int rowOffset = 0;
			IPatient p = new IPatient();
			
			for (int rowCount = 0; rowCount < patient.size(); rowCount++) {
				String[] row = patient.get(rowCount);
				if (isRepeatProgramEnrollments(row)) {
					// todo, little hack to get the data at least somehow; ideally it should be part of the encounters themselves
					IPatientProgram pp = new IPatientProgram();
					pp.programId = Integer.parseInt(row[1]);
					pp.dateEnrolled = Util.isNotEmpty(row[2]) ? Util.parseDate(row[2]) : null;
					pp.dateCompleted = Util.isNotEmpty(row[3]) ? Util.parseDate(row[3]) : null;
					pp.locationId = Util.isNotEmpty(row[4]) ? Integer.parseInt(row[4]) : null;
					for (int colCount = 5; colCount < row.length; colCount += 4) {
						if (Util.isNotEmpty(row[colCount]) && Util.isNotEmpty(row[colCount+1])) {
							IPatientState ps = new IPatientState();
							ps.programWorkflowId = Integer.parseInt(row[colCount]);
							ps.programWorkflowStateId = Integer.parseInt(row[colCount + 1]);
							ps.startDate = Util.isNotEmpty(row[colCount + 2]) ? Util.parseDate(row[colCount + 2]) : null;
							ps.endDate = Util.isNotEmpty(row[colCount + 3]) ? Util.parseDate(row[colCount + 3]) : null;
							pp.states.add(ps);
						}
					}
					p.patientPrograms.add(pp);
					if (rowCount < patient.size() - 1) {
						String[] nextRow = patient.get(rowCount + 1);
						if (isRepeatProgramEnrollments(nextRow)) {
							rowOffset++;
						}
					}
				} else if (isRepeatPatientIdentifiers(row)) {
					IPatientIdentifier pi = new IPatientIdentifier();
					for (int colCount = 1; colCount < row.length; colCount++) {
						if ((rowCount - rowOffset) < csvTemplate.size()
						        && colCount < csvTemplate.get(rowCount - rowOffset).length) {
							String templateCell = csvTemplate.get(rowCount - rowOffset)[colCount];
							if (isExpression(templateCell)) {
								String expression = templateCell.substring(1, templateCell.length() - 1);
								setValue(pi, expression, row[colCount]);
							}
						}
					}
					p.identifiers.add(pi);
					if (rowCount < patient.size() - 1) {
						String[] nextRow = patient.get(rowCount + 1);
						if (isRepeatPatientIdentifiers(nextRow)) {
							rowOffset++;
						}
					}
				} else if (isRepeatEncounterRow(row)) {
					// scope of followup encounter
					IEncounter encounter = new IEncounter();
					encounter.encounterTypeId = Integer.parseInt("" + parseArgs(row[0])[0]);
					for (int colCount = 1; colCount < row.length; colCount++) {
						if ((rowCount - rowOffset) < csvTemplate.size()
						        && colCount < csvTemplate.get(rowCount - rowOffset).length) {
							String templateCell = csvTemplate.get(rowCount - rowOffset)[colCount];
							if (isExpression(templateCell)) {
								String expression = templateCell.substring(1, templateCell.length() - 1);
								setValue(encounter, expression, row[colCount]);
							}
						}
					}
					p.encounters.add(encounter);
					if (rowCount < patient.size() - 1) {
						String[] nextRow = patient.get(rowCount + 1);
						if (isRepeatEncounterRow(nextRow)) {
							rowOffset++;
						}
					}
				} else {
					// scope of the current patient
					int colCount = 0;
					for (String col : row) {
						if (Util.isNotEmpty(col)) {
							String templateCell = csvTemplate.get(rowCount - rowOffset)[colCount];
							if (isExpression(templateCell)) {
								String expression = templateCell.substring(1, templateCell.length() - 1);
								setValue(p, expression, col);
							}
						}
						colCount++;
					}
				}
			}
			return p;
		}
		catch (Exception e) {
			System.out.println("Error deserializing patient: " + e.getMessage());
		}
		return null;
	}
	
	private void setValue(Object baseData, String expression, Object value) {
		try {
			if (expression.startsWith("patient.")) {
				expression = expression.substring("patient.".length(), expression.length());
			}
			Class<?> aClass = baseData.getClass();
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
						// todo, wrong assumption that there will only be one method with this name...
						// todo, and not only integer values are passed in as params
						if (isNotEmpty(remainingExpression) && !"void".equals(m.getReturnType().getName())) {
							Object newBaseData = m.invoke(baseData, parseArgs(expression));
							if (newBaseData == null) {
								// looks like we need to instantiate the bloody object first
								// take the return type and (!) params to prepopulate fields if necessary
								// caution: extreme coding happens here. and i always wanted to make use
								// of annotation. blessed be the rails metaprogramming...
								
								if (m.getParameterTypes().length == 1 && m.getParameterAnnotations().length == 1) {
									// assumes that there is one and only one param with one and only one annotation
									// first create the object
									Class<?> myClass = m.getReturnType();
									newBaseData = myClass.newInstance();
									// now preset the field specified by the param annotation with the value of the param
									//									m.getParameterTypes()[0];
									DeserializingHint a = (DeserializingHint) m.getParameterAnnotations()[0][0];
									Field field = myClass.getField(a.fieldName());
									field.set(newBaseData, parseArgs(fieldName)[0]);
									DeserializingHint a2 = (DeserializingHint) m.getAnnotations()[0];
									Field field2 = aClass.getField(a2.fieldName());
									if (field2.getType() == newBaseData.getClass()) {
										field2.set(baseData, newBaseData);
									} else if (field2.getType() == List.class) {
										((List) field2.get(baseData)).add(newBaseData);
									}
								}
								if (m.getAnnotation(DeserializingHint.class) != null) {
									Class<?> myClass = m.getReturnType();
									newBaseData = myClass.newInstance();
									DeserializingHint a2 = (DeserializingHint) m.getAnnotations()[0];
									Field field2 = aClass.getField(a2.fieldName());
									if (field2.getType() == newBaseData.getClass()) {
										field2.set(baseData, newBaseData);
									} else if (field2.getType() == List.class) {
										((List) field2.get(baseData)).add(newBaseData);
									}
								} else {
									throw new RuntimeException("Couldn't instanciate object");
								}
							}
							setValue(newBaseData, remainingExpression, value);
							methodFound = true;
							break;
						} else {
							Class<?> type = m.getReturnType();
							if ("void".equals(type.getName())) {
								Class<?>[] types = m.getParameterTypes();
								if (types != null && types.length == 1) {
									type = types[0];
									if (type == Integer.class && isNotEmpty("" + value)) {
										m.invoke(baseData, Integer.parseInt("" + value));
										methodFound = true;
										break;
									} else if (type == Date.class) {
										m.invoke(baseData, Util.parseDate("" + value));
										methodFound = true;
										break;
									} else if (type == String.class) {
										m.invoke(baseData, value);
										methodFound = true;
										break;
									} else {
										System.out.println("Unknown type: " + baseData + " - " + expression + " - " + value);
									}
								} else if (types != null && types.length == 2 /*&& isNotEmpty("" + value)*/) {
									type = types[0];
									if (type == Integer.class && isNotEmpty("" + parseArgs(expression)[0])) {
										m.invoke(baseData, Integer.parseInt("" + parseArgs(expression)[0]), "" + value);
										methodFound = true;
										break;
									} else {
										System.out.println("Unknown type: " + baseData + " - " + expression + " - " + value);
									}
								}
							}
						}
					}
				}
				if (!methodFound) {
					System.out.println("Method not found: " + baseData + " - " + expression);
				}
			} else {
				// a field
				Field field = aClass.getField(fieldName);
				if (isNotEmpty(remainingExpression)) {
					Object newBaseData = field.get(baseData);
					if (newBaseData == null)
						System.out.println();
					setValue(newBaseData, remainingExpression, value);
				} else {
					Class<?> type = field.getType();
					if (value == null || (value instanceof String && Util.isEmpty((String) value))) {} else if (type == Integer.class
					        && isNotEmpty("" + value)) {
						field.set(baseData, Integer.parseInt("" + value));
					} else if (type == Date.class) {
						field.set(baseData, Util.parseDate("" + value));
					} else if (type == String.class) {
						field.set(baseData, value);
					} else if (type == Double.class) {
						field.set(baseData, Double.parseDouble("" + value));
					} else if (type == Boolean.class) {
						field.set(baseData, Boolean.parseBoolean("" + value));
					} else {
						System.out.println("Unknown type: " + baseData + " - " + expression + " - " + value);
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass() + ": " + baseData + " - " + expression);
		}
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
		return (cell != null && cell.startsWith("#") && cell.endsWith("#") && !"#forEachFollowupEncounter(".equals(cell)
		        && !"#forEachPatientIdentifier#".equals(cell) && !"#forEachProgramEnrollment#".equals(cell));
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
