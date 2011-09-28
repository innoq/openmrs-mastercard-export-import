package org.openmrs.module.mastercard;

import org.apache.log4j.Logger;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.mastercard.exceptions.WrongFormatException;
import org.openmrs.module.mastercard.entities.ArvMastercardBean;
import org.openmrs.module.mastercard.entities.EncounterData;
import org.openmrs.module.mastercard.entities.HeaderData;

import java.io.*;
import java.util.*;

public class ArtImporter {
	
	static Logger logger = Logger.getLogger(ArtImporter.class);
	
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
		String connectionUser = prop.getProperty("connectionImport.username");
		String conncetionPw = prop.getProperty("connectionImport.password");
		String conncetionUrl = prop.getProperty("connectionImport.url");
		
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
				//getMastercardFromFile(file);
				importPatientsData(es, ps, null, file);
			}
		}
		
		logger.info("Import finished");
	}
	
	private void importPatientsData(EncounterService es, PatientService ps, Location nno, File file)
	                                                                                                throws FileNotFoundException,
	                                                                                                IOException,
	                                                                                                WrongFormatException {
		ArvMastercardBean mastercard = getMastercardFromFile(file);
		writeMastercardToDatabase(es, ps, nno, mastercard);
	}
	
	private ArvMastercardBean getMastercardFromFile(File file) throws IOException, WrongFormatException {
		ArvMastercardBean masterCardBean = null;
		try {
			String[] headerStringArray = new String[10];
			String[] encounterStringArray = null;
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			HashMap<Integer, String> encounterStringMap = new HashMap<Integer, String>();
			
			int i = 0;
			while ((str = in.readLine()) != null) {
				logger.info(i + " " + str);
				if (i < 10) {
					logger.info("Adding headerStringArray[" + i + "] " + str);
					headerStringArray[i] = str;
				}
				if (i > 10) {
					logger.info("Adding encounterStringMap[" + (i - 11) + "] " + str);
					encounterStringMap.put(new Integer(i - 11), str);
				}
				i++;
			}
			in.close();
			
			List<Integer> sortedHashKeys = new ArrayList<Integer>();
			sortedHashKeys.addAll(encounterStringMap.keySet());
			
			Collections.sort(sortedHashKeys);
			
			i = 0;
			encounterStringArray = new String[encounterStringMap.size()];
			Iterator<Integer> iter = sortedHashKeys.iterator();
			while (iter.hasNext()) {
				encounterStringArray[i] = encounterStringMap.get(iter.next());
				logger.info("files to encounterStringArray[" + i + "]" + encounterStringArray[i]);
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
	
	private void writeMastercardToDatabase(EncounterService es, PatientService ps, Location nno, ArvMastercardBean mastercard)
	                                                                                                                          throws FileNotFoundException,
	                                                                                                                          IOException {
		Patient p = new Patient();
		p.setBirthdate(mastercard.getHeaderData().getObservations().getDateOfBirth());
		PersonName pName = new PersonName();
		pName.setFamilyName(mastercard.getHeaderData().getObservations().getPatientFamilyName());
		pName.setGivenName(mastercard.getHeaderData().getObservations().getPatientGivenName());
		p.addName(pName);
		p.setDateChanged(new Date(System.currentTimeMillis()));
		p.setGender(mastercard.getHeaderData().getObservations().getSex());
		
		ps.createPatient(p);
		//TODO mild finish method
		
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounterStringArray
	 * @return
	 * @throws WrongFormatException
	 */
	protected EncounterData[] parseArrayForEncounterData(String[] encounterStringArray) throws WrongFormatException {
		
		EncounterData[] encounterDataArray = new EncounterData[encounterStringArray.length - 1];
		
		for (int i = 0; i < encounterStringArray.length; i++) {
			
			if (!encounterStringArray[0]
			        .equals("Visit loc;Vist Date;Hgt;Wt;Outcome Enrollment;Adverse Outcome;Outcome date;Regimen;Side Effects;TB status;current Pill count;Doses missed;ARVs given #;To;CPT #;Comment;Next appointment;Unknown Obs;"))
				throw new WrongFormatException("Header Line 0 expected to be Encounter Data Header /'/'");
			
			if (i > 0) {
				EncounterData encounterData = (EncounterData) new EncounterData(encounterStringArray[i]);
				encounterDataArray[i - 1] = encounterData;
			}
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
		
		//To keep signatures symetric and allow abstraction we assemble the different
		// mastercard-headerlines to a single string and disassemble those in HeaderData.demarshalData()
		String assembledHelperString = new String();
		assembledHelperString = assembledHelperString + headerStringArray[0];
		
		for (int i = 1; i < headerStringArray.length; i++) {
			assembledHelperString = assembledHelperString + "#;;#";
			assembledHelperString = assembledHelperString + headerStringArray[i];
		}
		
		//That is what we want to call with a single string parameter, see HeaderData.demarshalData()
		HeaderData headerData = (HeaderData) new HeaderData(assembledHelperString);
		return headerData;
	}
	
}
