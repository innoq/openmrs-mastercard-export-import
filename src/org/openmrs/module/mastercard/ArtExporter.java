package org.openmrs.module.mastercard;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.PersonAddress;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mastercard.entities.EncounterData;
import org.openmrs.module.mastercard.entities.HeaderData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArtExporter {
	
	static Logger logger = Logger.getLogger(ArtExporter.class);
	
	private static final String NOT_AVAILABLE = "-";
	
	private static final String NEWLINE = "\n";
	
	private Helper h = new Helper();
	
	public ArtExporter() {
	}
	
	public static void main(String[] args) throws Exception {
		// parameters
		String importFile = args[0];
		String openmrsRuntimeProperties = args[1];
		String openmrsUser = args[2];
		String openmrsPw = args[3];
		
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
		
		new ArtExporter().run();
	}
	
	public void run() throws Exception {
		
		export();
	}
	
	public void export() throws Exception {
		EncounterService es = Context.getEncounterService();
		PatientService ps = Context.getPatientService();
		
		logger.debug("Start exporting");
		Location nno = null; // location("Neno District Hospital"); dont filter
		// as mobile clinics also count
		
		// List<Patient> patients = Arrays.asList(ps.getPatient(16466));
		
		List<PatientIdentifierType> identifierTypes = Arrays.asList(ps.getPatientIdentifierTypeByName("ARV Number"));
		List<EncounterType> artInitials = Arrays.asList(es.getEncounterType("ART_INITIAL"));
		List<EncounterType> artFollowups = Arrays.asList(es.getEncounterType("ART_FOLLOWUP"));
		
		List<Patient> patients = getPatientArrayFromDB(ps, identifierTypes);
		
		(new File("export")).mkdir();
		
		for (Patient p : patients) {
			exportPatientsData(es, nno, artInitials, artFollowups, p);
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param es
	 * @param nno
	 * @param artInitials
	 * @param artFollowups
	 * @param p
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void exportPatientsData(EncounterService es, Location nno, List<EncounterType> artInitials,
	                                List<EncounterType> artFollowups, Patient p) throws FileNotFoundException, IOException {
		String nnoArv = p.getPatientId() + "";
		logger.info("  handling:" + nnoArv);
		
		nnoArv = checkForPIIdentifier(p, nnoArv);
		
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export/" + nnoArv + ".csv")));
		Encounter initial = null;
		w.newLine();
		logger.info("  Patient " + p.getId());
		// create new file
		// BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream("export/" + p.getId() + ".csv")));
		List<Encounter> el = es.getEncounters(p, nno, null, null, null, artInitials, null, false);
		
		HeaderData headerData;
		if (el.size() == 0) {
			// missing initial
			logger.warn("  Missing initial, trying to use another encounter");
			List<Encounter> anyEncounter = es.getEncounters(p, nno, null, null, null, null, null, false);
			
			if (anyEncounter.size() > 0) {
				logger.info("    Going to write exportInitial to BufferedWriter");
				headerData = new HeaderData(anyEncounter.get(0));
				w.write(headerData.getCsvSerialized());
				w.newLine();
			}
		} else {
			// take last initial (assuming list is ordered by date of entry)
			logger.info("  Last initial encounter");
			
			logger.info("    Going to write exportInitial to BufferedWriter");
			
			headerData = new HeaderData(el.get(el.size() - 1));
			w.write(headerData.getCsvSerialized());
			initial = el.get(el.size() - 1);
			w.newLine();
			
		}
		logger.info("  Getting encounters for Patient " + p.getId());
		el = es.getEncounters(p, nno, null, null, null, artFollowups, null, false);
		// Visit Date Hgt Wt Adverse Outcome Outcome date 1st L Alt 1st Line
		// 2nd L NS Side effects TB status current Pill count Doses missed
		// ARVs given # To CPT # Comment Next appointment#
		
		//TODO mild: Check header writing and neaderDataWriting.. maybe fucked up?
		logger.info("  Writing Header headers ?");
		w.write(EncounterData.getHeaderSerialized());
		w.newLine();
		
		EncounterData encounterDataInitial;
		if (initial != null) {
			logger.info("   Initial != null, writing exportFollowup for initial " + initial.getId());
			encounterDataInitial = new EncounterData(initial);
			w.write(encounterDataInitial.getCsvSerialized());
		}
		w.newLine();
		
		logger.info("  Iterating through encounters. # of encounters: " + el.size());
		
		logger.info("  Writing Encounters header ");
		w.write(EncounterData.getHeaderSerialized());
		w.newLine();
		
		for (Encounter e : el) {
			
			EncounterData encounterDate;
			
			encounterDate = new EncounterData(e);
			w.write(encounterDate.getCsvSerialized());
			w.newLine();
			
		}
		w.close();
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param p
	 * @param nnoArv
	 * @return
	 */
	private String checkForPIIdentifier(Patient p, String nnoArv) {
		for (PatientIdentifier pi : p.getPatientIdentifiers(Context.getPatientService().getPatientIdentifierType(
		    "ARV Number"))) {
			if (pi.getIdentifier().startsWith("NNO ")) {
				nnoArv = pi.getIdentifier();
				logger.info("    itterating pi-identifiert: " + nnoArv);
			}
		}
		return nnoArv;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param ps
	 * @param identifierTypes
	 * @return
	 */
	private List<Patient> getPatientArrayFromDB(PatientService ps, List<PatientIdentifierType> identifierTypes) {
		List<Patient> patients = new ArrayList<Patient>();
		for (int i = 1; i < 5; i++) {
			patients.addAll(ps.getPatients(null, "NNO " + i, identifierTypes, true));
			//patients.addAll(ps.getAllPatients(false));
			logger.info("added patients - Iteration: " + i);
		}
		// as substitute for aboves loop
		//patients.addAll(ps.getAllPatients(false));
		logger.info("added patients: " + patients.size());
		// patients.addAll(ps.getPatients(null, "NNO 1", identifierTypes,
		// true));
		// doesn't work ???
		// List<Patient> patients = ps.getPatients(null, "NNO%",
		// Arrays.asList(ps.getPatientIdentifierTypeByName("ARV Number"))),
		// false);
		return patients;
	}
	
	private SessionFactory sessionFactory() {
		return null;
		//		return ((HibernatePihMalawiQueryDao) Context.getRegisteredComponents(
		//			HibernatePihMalawiQueryDao.class).get(0)).getSessionFactory();
	}
	
}
