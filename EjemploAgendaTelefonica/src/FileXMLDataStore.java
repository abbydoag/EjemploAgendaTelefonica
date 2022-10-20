package edu.uvg.ej4.controller;
import java.io.BufferedReader;
/**
 * 
 */
import java.io.File;
import java.io.FileWriter;

import edu.uvg.ej4.controller.DataStore;
import edu.uvg.ej4.model.Contact;
/**
 * @author MA
 *
 */
public class FileXMLDataStore extends DataStore {


	private File file;
	private String path;
	
	public FileXMLDataStore(Agenda _agenda) {
		super();
		setAgenda(_agenda);
	}
	
	public FileXMLDataStore(Agenda _agenda, String _path) {
		super();
		setAgenda(_agenda);
		setPath(_path);
		setFile(new File(_path));
	}
	
	public FileXMLDataStore(Agenda _agenda, File _file, String _path) {
		super();
		setAgenda(_agenda);
		setFile(_file);
		setPath(_path);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	
	@Override
	public boolean initializeDS() throws Exception {
		// TODO Auto-generated method stub
		if (getFile()==null) {
			setFile(new File(getPath()));
		}
		if (getFile().createNewFile()) {
			System.out.println("New File Created");
		}else {
			System.out.println("File Already Exists");
		}
		return true;
	}

	@Override
	public boolean finalizeDS() throws Exception {
		// TODO Auto-generated method stub
		setFile(null);
		setPath(null);
		setAgenda(null);
		return true;
	}

	@Override
	public void saveData() throws Exception {
		// TODO Auto-generated method stub
		FileWriter writer;
		if(getFile().createNewFile()) {
			writer =new FileWriter(getFile());
		}else {
			if (getFile().exists()) {
				writer = new FileWriter(getFile(), false);
			}else {
				throw new Exception("Data could not be saved");
			}
		}
		
		if (writer!= null) {
			writer.write(convertAgendaToText(getAgenda()));
			writer.close();
		}
	}

	@Override
	public void getData() throws Exception {
		// TODO Auto-generated method stub
		if(getFile().isFile() && getFile().canRead()) {
			Agenda savedAgenda=null;
			BufferedReader reader = new BufferedReader(new FileReader(getFile()));
			try {
				String line;
				
				int counter =0;
				while ((line=reader.readLine()) != null) {
					if (counter==0) {
						savedAgenda = new Agenda(line);
					}else {
						if (savedAgenda != null) {
							if (line.split(",").length == 5) {
								String[] fields = line.split(",");
	                  			if (fields[DataStore.TYPE_FIELD].charAt(DataStore.TYPE_FIELD) == '1') { //it is a phone
	                				addPhoneToAgenda(savedAgenda, fields);
	                			} else if (fields[0].charAt(0) == '2') { //it is a email
	                				addEmailToAgenda(savedAgenda, fields);
	                			} else {
	                				throw new Exception("Incorrect line format at line: " + counter);
	                			}
							}else {
								throw new Exception("Incorrect file format");
							}
						}else {
							throw new Exception("Agenda couldn't be created");
						}
					}
					counter++;
				}
			} finally {
				reader.close();
			}
			setAgenda (savedAgenda);
		} else {
			throw new Exception("Is not a fille");
		}
	}
	/*
	 * Convsersión agenda a String para guardar
	 * @param_ lo que se necesita guardar (la agenda)
	 * @return en string el contendio del archivo 
	 */
	private String convertAgendaToText(Agenda _theAgenda) {
		String content ="";
		content=""+ _theAgenda.getOwnerID()+"\r\n";
		
		//guardar contacto
		for (Contact aContact:_theAgenda.getContacts()) {
			//num de cel
			String phones="";
			for (Phone aPhone: aContact.getPhoneNumbers()) {
				phones +="1"+aContact.getFirstName()+ "," + aContact.getLastName()+ "," + aPhone.getPhoneNumber()+ aPhone.getType() + "\r\n";
			}
			content+= phones;
			//correo
			String emails="";
			for (Email anEmail: aContact.getEmailAddresses()) {
				emails += "2," + aContact.getFirstName()+ "," + aContact.getLastName()+ "," + anEmail.getEmailAddress()+ "," + anEmail.getType() + "\r\n";
			}
			content+=emails;
		}
		return content;
	}
	/*
	 * Añade info de contacto a la agenda existente
	 */
	private void addEmailToAgenda(Agenda _theAgenda, String[] fileLine) {
		if (_theAgenda !=null) {
			Contact aContect=_theAgenda.searchByName(fileLine[DataStore.FIRSTNAME_FIELD], fileLine[DataStore.LASTNAME_FIELD]);
			boolean contactFound = aContact !=null;
			
			if (!contactFound) {
				aContact=new Contact (fileLine[DataStore.FIRSTNAME_FIELD], fileLine[DataStore.LASTNAME_FIELD]);
		
			if (!contactFound) {
				_theAgenda.getContacts().add(aContact);
      		}	
        	}
     	} 
	} 
}
