package org.openmrs.module.mastercard;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
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
import org.openmrs.mastercard.exceptions.WrongFormatException;
import org.openmrs.module.mastercard.entities.ArvMastercardBean;
import org.openmrs.module.mastercard.entities.EncounterData;
import org.openmrs.module.mastercard.entities.HeaderData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArtImporter {
	
	static Logger logger = Logger.getLogger(ArtImporter.class);
	
	private Helper h = new Helper();
	
	public ArtImporter() {
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
		
		new ArtImporter().run();
	}
	
	public void run() throws Exception {
		
		try {
			executeImport();
		}
		catch (WrongFormatException e) {
			logger.error("Error generated", e);
			throw new Exception(e);
		}
		
	}
	
	public void executeImport() throws Exception, WrongFormatException {
		EncounterService es = Context.getEncounterService();
		PatientService ps = Context.getPatientService();
		
		logger.debug("Start importing");
		
		File dir = new File("export");
		
		FilenameFilter filter = new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		};
		
		File[] children = dir.listFiles(filter);
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				File file = children[i];
				logger.info("Found files in export: " + file);
				getMastercardFromFile(file);
			}
		}
		
		//for (Patient p : patients) {
		//	importPatientsData(es, nno, artInitials, artFollowups, p);
		//}
	}
	
	private ArvMastercardBean getMastercardFromFile(File file) throws IOException, WrongFormatException {
		ArvMastercardBean masterCardBean = null;
		try {
			String[] headerStringArray = new String[8];
			String[] encounterStringArray = null;
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			Set<String> encounterStringSet = new HashSet<String>();
			
			int i = 0;
			while ((str = in.readLine()) != null) {
				logger.info(i + " " + str);
				if (i == 1)
					headerStringArray[0] = str;
				if (i == 2)
					headerStringArray[1] = str;
				if (i == 4)
					headerStringArray[2] = str;
				if (i == 5)
					headerStringArray[3] = str;
				if (i == 6)
					headerStringArray[4] = str;
				if (i == 7)
					headerStringArray[5] = str;
				if (i == 8)
					headerStringArray[6] = str;
				if (i == 9)
					headerStringArray[7] = str;
				if (i > 12)
					logger.info("add to array");
				encounterStringSet.add(str);
				i++;
			}
			in.close();
			
			i = 0;
			encounterStringArray = new String[encounterStringSet.size() - 12];
			for (String es : encounterStringSet) {
				encounterStringArray[i] = es;
				logger.info("filed to array[" + i + "]" + encounterStringArray[i]);
				i++;
			}
			
			HeaderData headerBean = parseArrayForHeaderData(headerStringArray);
			EncounterData[] encounterBeanArray = parseArrayForEncounterData(encounterStringArray);
			
			masterCardBean = new ArvMastercardBean();
			masterCardBean.setHeaderData(headerBean);
			masterCardBean.setEncounterData(encounterBeanArray);
		}
		catch (IOException e) {
			logger.error(e.fillInStackTrace());
		}
		
		return masterCardBean;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounterStringArray
	 * @return
	 * @throws WrongFormatException
	 */
	protected EncounterData[] parseArrayForEncounterData(String[] encounterStringArray) throws WrongFormatException {
		
		EncounterData[] encounterDataArray = new EncounterData[encounterStringArray.length];
		
		for (int i = 0; i < encounterStringArray.length; i++) {
			EncounterData encounterData = (EncounterData) new EncounterData(encounterStringArray);
			encounterDataArray[i] = encounterData;
		}
		return encounterDataArray;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param headerStringArray
	 * @return
	 * @throws WrongFormatException
	 */
	protected HeaderData parseArrayForHeaderData(String[] headerStringArray) throws WrongFormatException {
		HeaderData headerData = (HeaderData) new HeaderData(headerStringArray);
		return headerData;
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
	private void importPatientsData(EncounterService es, Location nno, List<EncounterType> artInitials,
	                                List<EncounterType> artFollowups, Patient p) throws FileNotFoundException, IOException {
		
		ArvMastercardBean mastercard = collectMastercardData(es, nno, artInitials, artFollowups, p);
		
		writeMastercardToDatabase(mastercard);
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param es
	 * @param nno
	 * @param artInitials
	 * @param artFollowups
	 * @param p
	 * @return
	 */
	private ArvMastercardBean collectMastercardData(EncounterService es, Location nno, List<EncounterType> artInitials,
	                                                List<EncounterType> artFollowups, Patient p) {
		ArvMastercardBean mastercard = null;
		
		mastercard = new ArvMastercardBean();
		
		{//nnoArv to mastercard as identifier!
			mastercard.setIdentifier(checkForPIIdentifier(p, p.getPatientId() + ""));
			
			logger.info("Filing mastercard for Patient with id: " + p.getId());
			List<Encounter> el = es.getEncounters(p, nno, null, null, null, artInitials, null, false);
			
			HeaderData headerData = null;
			if (el.size() == 0) {
				// missing initial
				logger.warn("  Missing initial, trying to use another encounter");
				List<Encounter> anyEncounter = es.getEncounters(p, nno, null, null, null, null, null, false);
				
				if (anyEncounter.size() > 0) {
					logger.info("    Going to write exportInitial to BufferedWriter");
					headerData = new HeaderData(anyEncounter.get(0));
				}
			} else {
				// take last initial (assuming list is ordered by date of entry)
				logger.info("  Last initial encounter");
				headerData = new HeaderData(el.get(el.size() - 1));
			}
			mastercard.setHeaderData(headerData);
			
			logger.info("  Getting encounters for Patient " + p.getId());
			el = es.getEncounters(p, nno, null, null, null, artFollowups, null, false);
			
			logger.info("  Initializing Arrayof encounters. # of encounters: " + el.size());
			EncounterData[] encounterDataArray = new EncounterData[el.size()];
			int i = 0;
			for (Encounter e : el) {
				encounterDataArray[i] = new EncounterData(e);
				i++;
			}
			mastercard.setEncounterData(encounterDataArray);
		}
		return mastercard;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param mastercard
	 * @param headerData
	 * @param encounterDataArray
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeMastercardToDatabase(ArvMastercardBean mastercard) throws FileNotFoundException, IOException {
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export/"
		        + mastercard.getIdentifier() + ".csv")));
		
		w.newLine();
		//writing header data header to cvs
		w.write(mastercard.getHeaderData().getCsvSerialized());
		w.newLine();
		
		//writing encounter data to csv
		EncounterData[] encounterDataArray = mastercard.getEncounterData();
		
		logger.info("Iterating through " + encounterDataArray.length + " encounters");
		for (int t = 0; t < encounterDataArray.length - 1; t++) {
			logger.info("writing encounter " + t);
			w.write(encounterDataArray[t].getCsvSerialized());
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
