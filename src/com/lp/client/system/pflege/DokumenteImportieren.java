/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.system.pflege;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeJCR;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.util.Helper;

public class DokumenteImportieren extends PflegefunktionSupportsProgressBar {
	
	private static final String Dok2HVFile = "Dok2HV.txt";
	private static final String TabAndSpace = "[\\s\\t]";
	private static final String Dok2HVRegex = "^" +
			TabAndSpace + "*([\\d]+)" +
			TabAndSpace + "+([\\w]+)" +
			TabAndSpace + "+([\\d]+)" +
			TabAndSpace + "+([\\d]+)" +
			TabAndSpace + "*([\\d]*)" +
			TabAndSpace + "*$";
	private static final int Nummer = 1;
	private static final int Art = 2;
	private static final int ProjektNr = 3;
	private static final int LfdNr = 4;
	private static final int ReNr = 5;
	private JTextField pfad;
	private PanelConsole console;
	private PrintWriter out;
	private PrintWriter err;
	private WrapperComboBox sicherheitsstufe;
	private boolean startable = false;
	private boolean isRunning = false;
	private boolean cancled = false;
	private File pfadFile;
	private String[] files;
	private Map<String, Map<Integer, String>> map;
	private JPanel panel;
	
	private static Map<String, String> belegarten = new HashMap<String, String>() {
		private static final long serialVersionUID = 594496473615344079L;

		{
			put("AB", LocaleFac.BELEGART_AUFTRAG);
			put("AN", LocaleFac.BELEGART_ANGEBOT);
			put("LS", LocaleFac.BELEGART_LIEFERSCHEIN);
			put("RG", LocaleFac.BELEGART_RECHNUNG);
			put("GU", LocaleFac.BELEGART_GUTSCHRIFT);
		}
	};
	
	private static Map<String, IDokumenteImportDocPathGenerator> generators = new HashMap<String, IDokumenteImportDocPathGenerator>() {
		private static final long serialVersionUID = 594496473615344077L;

		{
			put("AB", new ABDocPathGenerator());
			put("AN", new ANDocPathGenerator());
			put("LS", new LSDocPathGenerator());
			put("RG", new RGDocPathGenerator());
			put("GU", new GSDocPathGenerator());
		}
	};

	@Override
	public String getKategorie() {
		return KATEGORIE_DOKUMENTENABLAGE;
	}

	@Override
	public String getName() {
		return "Dokumente importieren 18208";
	}

	@Override
	public String getBeschreibung() {
		return "Kundespezifischer Import von Dokumenten (siehe Projekt 18208). " +
				"Nicht als Importfunktion f&uuml;r andere Kunden verwendbar!<br>" +
				"Im Pfad muss sich das File '" + Dok2HVFile + "' befinden, " +
				"welche die Belegzuordnung beinhaltet, und die PFDs.";
	}

	@Override
	public JPanel getPanel() {
		if(panel != null) return panel;
		sicherheitsstufe = new WrapperComboBox();
		sicherheitsstufe.addItem(JCRDocFac.SECURITY_NONE);
		sicherheitsstufe.addItem(JCRDocFac.SECURITY_LOW);
		sicherheitsstufe.addItem(JCRDocFac.SECURITY_MEDIUM);
		sicherheitsstufe.addItem(JCRDocFac.SECURITY_HIGH);
		sicherheitsstufe.addItem(JCRDocFac.SECURITY_ARCHIV);
		
		console = new PanelConsole();
		out = console.getOutputWriter();
		err = console.getErrorWriter();
		
		pfad = new WrapperTextField(300);
		pfad.setPreferredSize(new Dimension(300, 20));
		pfad.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updatePfadFile();
				File f = getDok2HVFile();
				startable = f != null && f.exists();
				if(startable)
					out.println(Dok2HVFile +  " gefunden!");
				else
					err.println(Dok2HVFile + " nicht gefunden!");
				if(startable)
					fireEnabledStartableEvent(new EventObject(this));
				else
					fireDisableStartableEvent(new EventObject(this));
			}
		});
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("Pfad:"),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(pfad,
				new GridBagConstraints(1, 0, 2, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(new JLabel("Sicherheitsstufe:"),
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(sicherheitsstufe,
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(new JScrollPane(console),
				new GridBagConstraints(0, 2, 3, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		return panel;
	}
	
	private File updatePfadFile() {
		pfadFile = pfad.getText()==null?null:new File(pfad.getText());
		return pfadFile;
	}
	
	private File getDok2HVFile() {
		if(pfadFile == null) return null;
		return new File(pfadFile, Dok2HVFile);
	}
	
	@Override
	public boolean isStartable() {
		return !isRunning && startable;
	}

	@Override
	public void run() {
		cancled = false;
		
		setProgressThis(new DefaultBoundedRangeModel(0, 0, 0, 100));
		
		SwingWorker<Void, Void> worker = new Worker();
		worker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					setProgressValue((Integer) evt.getNewValue());
					fireProgressEvent(null);
				}
			}
		});
		worker.execute();
	}
	
	protected void setProgressThis(BoundedRangeModel brm) {
		setProgress(brm);
	}

	@Override
	public void cancel() {
		cancled = true;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void init() {
	}

	@Override
	public void eventYouAreSelected() {
	}
	
	private class Worker extends SwingWorker<Void, Void> {
		
		@Override
		protected Void doInBackground() throws Exception {
			pfad.setEnabled(false);
			sicherheitsstufe.setEnabled(false);
			
			long startTime = System.currentTimeMillis();
			setProgress(0);
			isRunning = true;
			fireStartedEvent(new EventObject(this));

			countFilesAndWriteToOut();
			fillLaufendeNrMap();
			processFiles();
			setProgress(100);
			isRunning = false;
			fireDoneEvent(new EventObject(this));
			
			long endTime = System.currentTimeMillis() - startTime;
			long min = endTime / 60000;
			long sec = endTime % 60000 / 1000;
			out.println("Dauer: " + min +" min " + sec + "," + endTime%1000 + " sek");
			
			pfad.setEnabled(true);
			sicherheitsstufe.setEnabled(true);
			
			return null;
		}
		
		private void processFiles() {
			out.println("Ueber " + files.length + " Dateien iterieren...");
			int countWin = 0;
			int countFail = 0;
			
			File importedFolder = new File(pfadFile, "imported");
			if(!importedFolder.exists()) {
				importedFolder.mkdir();
			}
			Pattern p = Pattern.compile("([\\d]+)\\.pdf$");
			String mandantCNr;
			MandantDto mandantDto;
			try {
				mandantCNr = LPMain.getTheClient().getMandant();
				mandantDto = DelegateFactory.getInstance().getMandantDelegate().mandantFindByPrimaryKey(mandantCNr);
			} catch (Throwable e1) {
				err.println("Fehler bei mandantFindByPrimaryKey! Import abgebrochen!");
				e1.printStackTrace(err);
				return;
			}
			int i = 0;
			for(String file : files) {
				if(cancled) break;
				try {
					setProgress((int)(++i/(float)files.length*100));
				} catch(Exception e) {
					e.printStackTrace(out);
				}
				File f = new File(pfadFile, file);
				if(!f.exists()) {
					err.println("Auf Datei '" + file + "' konnte nicht zugegriffen werden.");
					countFail++;
					continue;
				}
				Matcher m = p.matcher(file.toLowerCase());
				if(!m.find()) {
					if(!f.exists()) {
						err.println("Dateiname '" + file + "' ist ungueltig.");
						countFail++;
						continue;
					}
				}
				Map<Integer, String> temp = map.get(m.group(1));
				if(temp == null) {
					err.println("Eindeutige Nummer '" + m.group(1) + "' der Datei '" + file + "' ist in '" + Dok2HVFile + "' nicht vorhanden.");
					countFail++;
					continue;
				}
				
				String art = temp.get(Art);
				String belegnr;
				if("RG".equals(art)) {
					belegnr = temp.get(ReNr);
				} else {
					belegnr = temp.get(ProjektNr)+String.format("%02d", new Integer(temp.get(LfdNr)));
				}
				Integer jahr = new Integer(belegnr.substring(0, 4));
				Integer nummer = new Integer(belegnr.substring(4));
				belegnr = String.format("%d/%06d", jahr, nummer);
				
				try {
					IDokumenteImportDocPathGenerator gen =  generators.get(art);
					if(gen == null) {
						err.println("Belegart '" + art + "' unbekannt. ("+ file + ")");
						countFail++;
						continue;
					}
					DocPath dpath = gen.getDocPathByBelegNr(belegnr);
					if(dpath == null) {
						err.println("Beleg " + art + " " + belegnr + " nicht gefunden.");
						countFail++;
						continue;
					}
					DocNodeJCR node = (DocNodeJCR)dpath.getLastDocNode();
					
					JCRDocDto jcr = new JCRDocDto();
					jcr.setsSchlagworte("Importiert");
					jcr.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
					jcr.setsFilename(file);
					jcr.setsMIME(Helper.getMime(file));
					jcr.setsName(Helper.getName(file));
					jcr.setsRow(""+node.getiId());
					jcr.setlAnleger(LPMain.getTheClient().getIDPersonal());
					jcr.setlPartner(mandantDto.getPartnerIId());
					jcr.setbVersteckt(false);
					jcr.setlSicherheitsstufe((Long)sicherheitsstufe.getSelectedItem());
					jcr.setsBelegnummer(belegnr);
					jcr.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
					jcr.setDocPath(dpath.add(new DocNodeFile(Helper.getName(file))));
					jcr.setsTable(belegarten.get(art));
					
					FileInputStream reader = new FileInputStream(f);
					if(f.length() > Integer.MAX_VALUE) {
						err.println("Datei '" + file + "' groesser als " + Integer.MAX_VALUE + ".");
						countFail++;
						continue;
					}
					byte[] data = new byte[(int)f.length()];
					reader.read(data);
					jcr.setbData(data);
					reader.close();
					DelegateFactory.getInstance().getJCRDocDelegate().addNewDocumentOrNewVersionOfDocument(jcr);
					f.renameTo(new File(importedFolder, file));
					countWin++;
					
				} catch (ExceptionLP e) {
					e.printStackTrace();
					err.println("Schwer Fehler:");
					err.println(e.getSMsg());
					err.println(e.getCause());
					countFail++;
					continue;
				} catch (Throwable e) {
					e.printStackTrace();
					err.println("Schwer Fehler:");
					err.println(e.toString());
					countFail++;
					continue;
				}
			}
			out.println(countWin + " erfolgreich.");
			out.println(countFail + " fehlgeschlagen.");
		}
		
		private void fillLaufendeNrMap() {
			try {
				map = new HashMap<String, Map<Integer,String>>();
				out.println(Dok2HVFile + " auslesen...");
				File f = getDok2HVFile();
				if(!f.canRead()) {
					err.println("Kann die Datei '" + f.getName() + "' nicht lesen.");
					return;
				}
				BufferedReader reader = new BufferedReader(new FileReader(f));
				Pattern p = Pattern.compile(Dok2HVRegex);

				//erste zeile auslassen
				if(reader.ready())
					reader.readLine();
				int i = 1;
				int j = 0;
				while(reader.ready() && !cancled) {
					i++;
					String line = reader.readLine();
					Matcher m = p.matcher(line);
					if(!m.find()) {
						err.println("Zeile " + i + " konnte nicht gelesen werden:");
						err.println(line);
						j++;
						continue;
					}
					Map<Integer, String> temp = new HashMap<Integer, String>();

					temp.put(Nummer, m.group(Nummer));
					temp.put(Art, m.group(Art));
					temp.put(ProjektNr, m.group(ProjektNr));
					temp.put(LfdNr, m.group(LfdNr));
					temp.put(ReNr, m.group(ReNr));
					
					if(map.containsKey(m.group(Nummer))) {
						err.println("Nummer " + m.group(Nummer) + " kommt mehrmals vor");
					}
					map.put(m.group(Nummer), temp);
				}
				out.println(i + " Zeilen gelesen");
				out.println(j + " davon nicht erkannt");
				reader.close();
			} catch (FileNotFoundException e) {
				err.println("Datei '" + Dok2HVFile + "' wurde nicht gefunden.\n");
				e.printStackTrace(err);
			} catch (IOException e) {
				e.printStackTrace(err);
			}
		}
		
		private void countFilesAndWriteToOut() {
			if(pfadFile == null) {
				err.println("Keine Dateien gefunden.");
				return;
			}
			files = pfadFile.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".pdf");
				}
			});
			int anzahl = files==null?0:files.length;
			out.println("Gefundene PDFs: " + anzahl);
		}
	}

}
