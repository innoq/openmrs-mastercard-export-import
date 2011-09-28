package org.openmrs.module.mastercard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.context.Context;
import org.openmrs.module.mastercard.entities.Constants;

public class Helper {
	
	protected static final Log logger = LogFactory.getLog(Helper.class);
	
	public ProgramWorkflowState workflowState(String program, String workflow, String state) {
		ProgramWorkflowState s = Context.getProgramWorkflowService().getProgramByName(program).getWorkflowByName(workflow)
		        .getStateByName(state);
		if (s == null) {
			throw new RuntimeException("Couldn't find ProgramWorkflowState " + state);
		}
		return s;
	}
	
	public Program program(String program) {
		Program s = Context.getProgramWorkflowService().getProgramByName(program);
		;
		if (s == null) {
			throw new RuntimeException("Couldn't find Program " + s);
		}
		return s;
	}
	
	public Location location(String location) {
		Location s = Context.getLocationService().getLocation(location);
		if (s == null) {
			throw new RuntimeException("Couldn't find Location " + location);
		}
		return s;
	}
	
	public PatientState getMostRecentStateAtLocation(Patient p, List<ProgramWorkflowState> programWorkflowStates,
	                                                 Location enrollmentLocation, Session hibernateSession) {
		PatientState state = null;
		List<PatientProgram> pps = Context.getProgramWorkflowService().getPatientPrograms(p,
		    programWorkflowStates.get(0).getProgramWorkflow().getProgram(), null, null, null, null, false);
		List<Integer> programWorkflowStateIds = new ArrayList<Integer>();
		for (ProgramWorkflowState pws : programWorkflowStates) {
			programWorkflowStateIds.add(pws.getId());
		}
		for (PatientProgram pp : pps) {
			// hope that the first found pp is also first in time
			Location location = getEnrollmentLocation(pp, hibernateSession);
			if (!pp.isVoided() && location != null && location.getId().equals(enrollmentLocation.getId())) {
				HashMap<Long, PatientState> validPatientStates = new HashMap<Long, PatientState>();
				ArrayList<Long> stupidListConverter = new ArrayList<Long>();
				for (PatientState ps : pp.getStates()) {
					if (!ps.isVoided() && programWorkflowStateIds.contains(ps.getState().getId())
					        && ps.getStartDate() != null) {
						validPatientStates.put(ps.getStartDate().getTime(), ps);
						stupidListConverter.add(ps.getStartDate().getTime());
					}
				}
				Collections.<Long> sort(stupidListConverter);
				
				for (Long key : stupidListConverter) {
					// just take the last one and hope it is the most recent one
					state = (PatientState) validPatientStates.get(key);
				}
			}
		}
		return state;
	}
	
	public PatientState getMostRecentStateAtLocation(Patient p, Program program, Location enrollmentLocation,
	                                                 Session hibernateSession) {
		PatientState state = null;
		List<PatientProgram> pps = Context.getProgramWorkflowService().getPatientPrograms(p, program, null, null, null,
		    null, false);
		for (PatientProgram pp : pps) {
			// hope that the first found pp is also first in time
			Location location = getEnrollmentLocation(pp, hibernateSession);
			if (!pp.isVoided() && location != null && location.getId().equals(enrollmentLocation.getId())) {
				HashMap<Long, PatientState> validPatientStates = new HashMap<Long, PatientState>();
				ArrayList<Long> stupidListConverter = new ArrayList<Long>();
				for (PatientState ps : pp.getStates()) {
					if (!ps.isVoided() && ps.getStartDate() != null) {
						validPatientStates.put(ps.getStartDate().getTime(), ps);
						stupidListConverter.add(ps.getStartDate().getTime());
					}
				}
				Collections.<Long> sort(stupidListConverter);
				
				for (Long key : stupidListConverter) {
					// just take the last one and hope it is the most recent one
					state = (PatientState) validPatientStates.get(key);
				}
			}
		}
		return state;
	}
	
	public PatientState getMostRecentStateAtDate(Patient p, Program program, Date endDate,
	                                             org.hibernate.classic.Session hibernateSession) {
		// wrong assumption that there is only one programworkflow for a program 
		PatientState state = null;
		List<PatientProgram> pps = Context.getProgramWorkflowService().getPatientPrograms(p, program, null, null, null,
		    null, false);
		
		HashMap<Long, PatientState> validPatientStates = new HashMap<Long, PatientState>();
		ArrayList<Long> sortedByStartDate = new ArrayList<Long>();
		
		for (PatientProgram pp : pps) {
			if (!pp.isVoided()) {
				for (PatientState ps : pp.getStates()) {
					if (!ps.isVoided() && ps.getStartDate() != null) {
						validPatientStates.put(ps.getStartDate().getTime(), ps);
						sortedByStartDate.add(ps.getStartDate().getTime());
					}
				}
			}
		}
		Collections.<Long> sort(sortedByStartDate);
		
		for (Long key : sortedByStartDate) {
			// take the one with the start date on or before endDate
			if (key <= endDate.getTime()) {
				state = (PatientState) validPatientStates.get(key);
			}
		}
		return state;
	}
	
	public List<PatientState> getPatientStatesByWorkflowAtLocation(Patient p, ProgramWorkflowState programWorkflowState,
	                                                               Location enrollmentLocation, Session hibernateSession) {
		
		Integer programWorkflowStateId = programWorkflowState.getId();
		
		List<PatientProgram> pps = Context.getProgramWorkflowService().getPatientPrograms(p,
		    programWorkflowState.getProgramWorkflow().getProgram(), null, null, null, null, false);
		
		// list of patientstates (patient, workflow)
		List<PatientState> patientStateList = new ArrayList<PatientState>();
		// hope that the first found pp is also first in time
		for (PatientProgram pp : pps) {
			if (enrollmentLocation == null) {
				if (!pp.isVoided()) {
					for (PatientState ps : pp.getStates()) {
						if (!ps.isVoided() && programWorkflowStateId.equals(ps.getState().getId())
						        && ps.getStartDate() != null) {
							
							patientStateList.add(ps);
						}
					}
				}
			} else {
				Location location = getEnrollmentLocation(pp, hibernateSession);
				if (!pp.isVoided() && location != null && location.getId().equals(enrollmentLocation.getId())) {
					for (PatientState ps : pp.getStates()) {
						if (!ps.isVoided() && programWorkflowStateId.equals(ps.getState().getId())
						        && ps.getStartDate() != null) {
							
							patientStateList.add(ps);
						}
					}
				}
			}
		}
		
		return patientStateList;
	}
	
	public Location getEnrollmentLocation(PatientProgram pp, Session hibernateSession) {
		String sql = "select location_id from patient_program where patient_program_id = " + pp.getId();
		
		Query query = hibernateSession.createSQLQuery(sql.toString());
		// assume there is only one
		if (!query.list().isEmpty() && query.list().get(0) != null) {
			return Context.getLocationService().getLocation(((Integer) (query.list().get(0))).intValue());
		}
		return null;
	}
	
	public EncounterType encounterType(String string) {
		return Context.getEncounterService().getEncounterType(string);
	}
	
	public Set<PatientState> getMostRecentStates(Patient p, Session currentSession) {
		List<PatientProgram> pps = Context.getProgramWorkflowService().getPatientPrograms(p, null, null, null, null, null,
		    false);
		Set<PatientState> allStates = new HashSet<PatientState>();
		for (PatientProgram pp : pps) {
			allStates.addAll(pp.getCurrentStates());
		}
		return allStates;
	}
	
	public Concept concept(String string) {
		return Context.getConceptService().getConcept(string);
	}
	
	public static Date getDateFromString(String dateString) {
		logger.info("Transforming String to Date:" + dateString);
		
		if ((dateString != null && !dateString.equals(Constants.NOT_AVAILABLE))) {
			String[] dateValues = dateString.split("\\s");
			for (String string : dateValues) {
				logger.info(string);
			}
			
			int day = new Integer(dateValues[0]).intValue();
			int month = new Integer(dateValues[1]).intValue();
			int year = new Integer(dateValues[2]).intValue();
			
			logger.info("Transforming String to Date got y, m, d:" + year + ", " + month + ", " + day);
			
			Calendar calendar = new GregorianCalendar();
			calendar.set(year, month, day);
			
			//TODO mild: check on calendar correctness
			return calendar.getTime();
		} else
			return null;
	}
	
	public static String getStringFromDate(Date date) {
		return new SimpleDateFormat("dd MM yyyy").format(date);
	}
<<<<<<< HEAD
	
	public static Double getDoubleFromString(String s) {
		logger.info("Transforming String to Double:" + s);
		
		if ((s != null && !s.equals(Constants.NOT_AVAILABLE))) {
			return new Double(s);
		} else
			return null;
	}
=======

    public static Double getNumericFromString(String value) {
    	try {
    		return new Double(value);
    	} catch (NumberFormatException nfe) {
    		return null;
    	}
    }
>>>>>>> 2841ac09e766dfc4b3194b3412f67790fb1c7646
}
