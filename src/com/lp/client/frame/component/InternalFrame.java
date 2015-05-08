/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
package com.lp.client.frame.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Stack;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.frame.Command;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.assistent.view.AssistentView;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.editor.PanelEditor;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportIfJRDSZweiDrucker;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.PanelReportKriterienOptions;
import com.lp.client.frame.report.PanelReportKriterienZweiDrucker;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.projekt.TabbedPaneProjekt;
import com.lp.client.util.ClientConfiguration;
import com.lp.client.util.dtable.DistributedTableModelImpl;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.BelegartdokumentDto;
import com.lp.server.system.service.DokumentDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><b>frame</b></p> <p> Diese Klasse ist die Basisklasse aller
 * Internalframes.</br> Hier werden alle Panels verwaltet.</br> Will ein Panel
 * einem anderen etwas mitteilen so laeuft dies hierueber. </p> <p>Copyright:
 * Copyright (c) 2004</p>
 * 
 * <p>Organisation: </p>
 * 
 * @author $Author: robert $
 * 
 * @version $Revision: 1.25 $
 */
abstract public class InternalFrame extends JInternalFrame implements
		ActionListener, ChangeListener, ItemChangedListener, IInternalFrame,
		ComponentListener, ICommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<ItemChangedListener> listeners = null;
	private final String belegartCNr;
	private String[] sTitle = { "", "", "", "" };
	private Stack<PanelDialogStackElement> vPanelDialog = null;
	private ProgressTimer frameProgress = null;

	// SP1600
	private Integer iLetzteGewaehlteArtikelgruppenIId;

	public Integer getILetzteGewaehlteArtikelgruppenIId() {
		return iLetzteGewaehlteArtikelgruppenIId;
	}

	public void setILetzteGewaehlteArtikelgruppenIId(
			Integer iLetzteGewaehlteArtikelgruppenIId) {
		this.iLetzteGewaehlteArtikelgruppenIId = iLetzteGewaehlteArtikelgruppenIId;
	}

	// PJ17960
	private boolean bNullpreiswarnungAnzeigen = true;

	public boolean isBNullpreiswarnungAnzeigen() {
		return bNullpreiswarnungAnzeigen;
	}

	public void setBNullpreiswarnungAnzeigen(boolean bNullpreiswarnungAnzeigen) {
		this.bNullpreiswarnungAnzeigen = bNullpreiswarnungAnzeigen;
	}

	protected final JTabbedPane tabbedPaneRoot;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private java.awt.event.MouseListener mpq = null;

	public static final int TITLE_IDX_MODUL = 0;
	public static final int TITLE_IDX_OHRWASCHLUNTEN = 1;
	public static final int TITLE_IDX_OHRWASCHLOBEN = 2;
	public static final int TITLE_IDX_AS_I_LIKE = 3;

	// der key der gelockt wird; zz. fuer ein ganzen internalframe!
	private String keyWasForLockMe = null;

	// der alte key der gelockt wird; zz. fuer ein ganzen internalframe!
	private String keyWasForLockMeOld = null;

	private String sRechtModulweit = null;

	// Das Recht "Darf Einkaufs-Preise sehen" gilt clientseitig fuer die
	// Lebensdauer des InternalFrames.
	public final boolean bRechtDarfPreiseSehenEinkauf;

	// Das Recht "Darf Verkaufs-Preise sehen" gilt clientseitig fuer die
	// Lebensdauer des InternalFrames.
	public final boolean bRechtDarfPreiseSehenVerkauf;

	public final boolean bRechtDarfPreiseAendernVerkauf;
	public final boolean bRechtDarfPreiseAendernEinkauf;

	public Integer letzteKostentraegerIId = null;

	private boolean modulLocked ;
	
	public InternalFrame(String sAddTitleI, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		// titlp: 1 hier keinen Titel setzen!
		super("", true, true, true, true);
		this.belegartCNr = belegartCNr;
		//
		if (sRechtModulweitI == null) {
			// Wenn kein Recht angegeben wurde -> Automatisch nur READ
			sRechtModulweit = RechteFac.RECHT_MODULWEIT_READ;
		} else {
			sRechtModulweit = sRechtModulweitI;
		}

		// Benutzerrecht "Darf Einkaufs-Preise sehen" holen.
		bRechtDarfPreiseSehenEinkauf = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF);

		// Benutzerrecht "Darf Verkaufs-Preise sehen" holen.
		bRechtDarfPreiseSehenVerkauf = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF);

		bRechtDarfPreiseAendernVerkauf = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF);

		bRechtDarfPreiseAendernEinkauf = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

		tabbedPaneRoot = new JTabbedPane();
		try {
			jbInitIF();
			setLpTitle(TITLE_IDX_MODUL, sAddTitleI); // titlp: 2 hier wird der
			// 0.te Titel gesetzt;
			// meist: Modulname
		} catch (Throwable t) {
			handleException(t, true);
		}
		initComponents();
	}

	public void scanAndSave(String belegartCNr, Integer belegartIId)
			throws Throwable {

		try {
			java.io.File f = File.createTempFile("hvd", ".pdf");
			String filename = f.getAbsolutePath();
			String filenameKurz = f.getName();
			f.delete();

			String line;

			String logpScan = ClientConfiguration.getLogpScan() ;

			ArbeitsplatzparameterDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_PFAD_MIT_PARAMETER_SCANTOOL);

			if (parameter == null) {
				DialogFactory
						.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.error"),
								"Bitte Arbeitsplatzparameter 'PFAD_MIT_PARAMETER_SCANTOOL' setzen.");
				return;
			}

			logpScan = parameter.getCWert();

			String command = logpScan + " " + filename;

			Process p = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String output = "";
			while ((line = input.readLine()) != null) {
				output = line + "\n";
			}
			if (output.length() > 0) {
				DialogFactory.showModalDialog("Fehler", output);
			}
			input.close();

			FileInputStream inFile = new FileInputStream(filename);
			byte[] datei = new byte[inFile.available()];
			inFile.read(datei);
			inFile.close();

			String s = JOptionPane.showInternalInputDialog(this,
					"Bitte geben Sie eine Kurzbeschreibung an:");

			if (s != null) {
				BelegartdokumentDto belegartdokumentdto = new BelegartdokumentDto();

				belegartdokumentdto.setIBelegartid(belegartIId);
				belegartdokumentdto.setBelegartCNr(belegartCNr);
				DokumentDto dokumentDto = new DokumentDto();
				dokumentDto.setCBez(s);
				dokumentDto.setCDateiname(filenameKurz);
				dokumentDto
						.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
				dokumentDto.setOInhalt(datei);
				belegartdokumentdto.setDokumentDto(dokumentDto);
				DelegateFactory.getInstance().getDokumenteDelegate()
						.createBelegartdokument(belegartdokumentdto);
			}
			f.delete();

		} catch (IOException ex) {
			DialogFactory.showModalDialog("Fehler", ex.getMessage());
		}

	}

	public File scanFile() throws Throwable {

		try {
			java.io.File f = File.createTempFile("hvd", ".pdf");
			String filename = f.getAbsolutePath();
			// String filenameKurz = f.getName();
			f.delete();

			String line;

			String logpScan = ClientConfiguration.getLogpScan() ;

			ArbeitsplatzparameterDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_PFAD_MIT_PARAMETER_SCANTOOL);

			if (parameter == null) {
				DialogFactory
						.showModalDialog(LPMain.getInstance()
								.getTextRespectUISPr("lp.error"),
								"Bitte Arbeitsplatzparameter 'PFAD_MIT_PARAMETER_SCANTOOL' setzen.");
				return null;
			}

			logpScan = parameter.getCWert();

			String command = logpScan + " " + filename;

			Process p = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String output = "";
			while ((line = input.readLine()) != null) {
				output = line + "\n";
			}
			if (output.length() > 0) {
				DialogFactory.showModalDialog("Fehler", output);
			}
			input.close();

			FileInputStream inFile = new FileInputStream(filename);
			byte[] datei = new byte[inFile.available()];
			inFile.read(datei);
			inFile.close();

			return f;

		} catch (IOException ex) {
			DialogFactory.showModalDialog("Fehler", ex.getMessage());
			return null;
		}

	}

	public void setRechtModulweit(String sRechtModulweit) {
		this.sRechtModulweit = sRechtModulweit;
	}

	public String getRechtModulweit() {
		return sRechtModulweit;
	}

	/**
	 * Hole die selektierte Tabbedpane.
	 * 
	 * @return JTabbedPaneKomponente
	 */
	public JTabbedPane getSelectedTabbedPane() {
		return null;
	}

	protected void registerChangeListeners() {
		tabbedPaneRoot.addChangeListener(this);
	}

	private void jbInitIF() throws Throwable {
		// hier kommt alles drauf
		tabbedPaneRoot.setTabPlacement(SwingConstants.BOTTOM);
		getContentPane().add(tabbedPaneRoot, BorderLayout.CENTER);

		// setJMenuBar(getLpMenuBar());
		frameProgress = new ProgressTimer();
		this.addComponentListener(this);
	}

	/**
	 * Setze einen Titelteil. titlp: 3
	 * 
	 * @param iWhichI
	 *            int
	 * @param titleI
	 *            String
	 * @throws Exception
	 */
	public final void setLpTitle(int iWhichI, String titleI) {
		// precondition
		if (iWhichI > TITLE_IDX_AS_I_LIKE) {
			iWhichI = 0;
			titleI = "iWhichI > TITLE_IDX_ASYOULIKE";
		}
		if (titleI == null) {
			// MB nicht an index 0 schreiben, sonst wird das nie wieder
			// korrigiert
			// iWhichI = 0;
			titleI = "Neu";
		}
		sTitle[iWhichI] = titleI;
		if (!(sTitle[iWhichI].equals("") || iWhichI == 0)) {
			sTitle[iWhichI] = " | " + sTitle[iWhichI];
		}

		super.setTitle(sTitle[TITLE_IDX_MODUL]
				+ sTitle[TITLE_IDX_OHRWASCHLUNTEN]
				+ sTitle[TITLE_IDX_OHRWASCHLOBEN] + sTitle[TITLE_IDX_AS_I_LIKE]);
	}

	public final String getBelegartCNr() {
		return belegartCNr;
	}

	/**
	 * Registriere eine Listener. itemevt: 1 der Internalframe ist der Sammler
	 * der Listener
	 * 
	 * @param l
	 *            ItemChangedListener
	 */
	synchronized public void addItemChangedListener(ItemChangedListener l) {
		if (listeners == null) {
			listeners = new Vector<ItemChangedListener>();
		}
		listeners.addElement(l);
	}

	/**
	 * Entferne Listener.
	 * 
	 * @param listener
	 *            ItemChangedListener (zB. Panel).
	 */
	synchronized public void removeItemChangedListener(
			ItemChangedListener listener) {
		if (listeners == null) {
			listeners = new Vector<ItemChangedListener>();
		}
		listeners.removeElement(listener);
	}

	public final void fireItemChanged(Object source, int id, MouseEvent e) {
		if (listeners != null && !listeners.isEmpty()) {
			ItemChangedEvent event = new ItemChangedEvent(source, id);
			event.setMouseEvent(e);
			fireItemChangedImpl(event);
		}
	}

	/**
	 * Feuere einen Event an alle registrierten Listener (zB. Panels).
	 * 
	 * @param source
	 *            Object
	 * @param idI
	 *            int
	 */
	public final void fireItemChanged(Object source, int idI) {
		// if we have no listeners, do nothing...
		if (listeners != null && !listeners.isEmpty()) {
			// create the event object to send
			ItemChangedEvent event = new ItemChangedEvent(source, idI);
			fireItemChangedImpl(event);

			// // make a copy of the listener list in case
			// // anyone adds/removes listeners
			// Vector<ItemChangedListener> targets = new
			// Vector<ItemChangedListener>();
			// synchronized (this) {
			// targets.setSize(listeners.size());
			// Collections.copy(targets, listeners);
			// }
			//
			// // walk through the listener list and
			// // call the sunMoved method in each
			// Enumeration<ItemChangedListener> e = targets.elements();
			// while (e.hasMoreElements()) {
			// ItemChangedListener listener = (ItemChangedListener) e
			// .nextElement();
			// // itemevt: 2 jeder Listener wird jetzt via changed
			// // verstaendigt.
			//
			// // CK: optimiert, sodass nur mehr das Panel mit derselben
			// // Use-Case-ID verstaendigt wird
			// if (event.getSource() instanceof PanelQuery) {
			// PanelQuery pqSource = (PanelQuery) event.getSource();
			// if (pqSource.getTable().getModel() instanceof
			// DistributedTableModelImpl) {
			// DistributedTableModelImpl tm = (DistributedTableModelImpl)
			// pqSource
			// .getTable().getModel();
			// try {
			// if (tm.getDataSource() != null
			// && tm.getDataSource().getUseCaseId() != null) {
			//
			// if (listener instanceof PanelQuery) {
			// PanelQuery pq = (PanelQuery) listener;
			//
			// DistributedTableModelImpl tm2 = (DistributedTableModelImpl) pq
			// .getTable().getModel();
			// if (tm2.getDataSource().getUseCaseId()
			// .intValue() != tm.getDataSource()
			// .getUseCaseId().intValue()) {
			// continue;
			// }
			// }
			// }
			// } catch (Throwable t) {
			// handleException(t, true);
			// }
			// }
			// }
			// if (event.getSource() instanceof WrapperTable) {
			// WrapperTable wtSource = (WrapperTable) event.getSource() ;
			// if (wtSource.getModel() instanceof DistributedTableModelImpl) {
			// DistributedTableModelImpl tm = (DistributedTableModelImpl)
			// wtSource
			// .getModel();
			// try {
			// if (tm.getDataSource() != null
			// && tm.getDataSource().getUseCaseId() != null) {
			//
			// if (listener instanceof PanelQuery) {
			// PanelQuery pq = (PanelQuery) listener;
			//
			// DistributedTableModelImpl tm2 = (DistributedTableModelImpl) pq
			// .getTable().getModel();
			// if (tm2.getDataSource().getUseCaseId()
			// .intValue() != tm.getDataSource()
			// .getUseCaseId().intValue()) {
			// continue;
			// }
			// }
			// }
			// } catch (Throwable t) {
			// handleException(t, true);
			// }
			// }
			// }
			//
			// listener.changed(event);
			// // AD: System.out.println(listener.getClass() + " " +
			// // event.toString());
			// }
		}
	}

	public final void fireItemChanged(ItemChangedEvent event) {
		if (listeners != null && !listeners.isEmpty()) {
			fireItemChangedImpl(event);
		}
	}

	protected void fireItemChangedImpl(ItemChangedEvent event) {
		// make a copy of the listener list in case
		// anyone adds/removes listeners
		Vector<ItemChangedListener> targets = new Vector<ItemChangedListener>();
		synchronized (this) {
			targets.setSize(listeners.size());
			Collections.copy(targets, listeners);
		}

		// walk through the listener list and
		// call the sunMoved method in each
		Enumeration<ItemChangedListener> e = targets.elements();
		while (e.hasMoreElements()) {
			ItemChangedListener listener = (ItemChangedListener) e
					.nextElement();
			// itemevt: 2 jeder Listener wird jetzt via changed
			// verstaendigt.

			// CK: optimiert, sodass nur mehr das Panel mit derselben
			// Use-Case-ID verstaendigt wird
			if (event.getSource() instanceof PanelQuery) {
				PanelQuery pqSource = (PanelQuery) event.getSource();
				if (pqSource.getTable().getModel() instanceof DistributedTableModelImpl) {
					DistributedTableModelImpl tm = (DistributedTableModelImpl) pqSource
							.getTable().getModel();
					try {
						if (tm.getDataSource() != null
								&& tm.getDataSource().getUseCaseId() != null) {

							if (listener instanceof PanelQuery) {
								PanelQuery pq = (PanelQuery) listener;

								DistributedTableModelImpl tm2 = (DistributedTableModelImpl) pq
										.getTable().getModel();
								if (tm2.getDataSource().getUseCaseId()
										.intValue() != tm.getDataSource()
										.getUseCaseId().intValue()) {
									continue;
								}
							}
						}
					} catch (Throwable t) {
						handleException(t, true);
					}
				}
			}
			if (event.getSource() instanceof WrapperTable) {
				WrapperTable wtSource = (WrapperTable) event.getSource();
				if (wtSource.getModel() instanceof DistributedTableModelImpl) {
					DistributedTableModelImpl tm = (DistributedTableModelImpl) wtSource
							.getModel();
					try {
						if (tm.getDataSource() != null
								&& tm.getDataSource().getUseCaseId() != null) {

							if (listener instanceof PanelQuery) {
								PanelQuery pq = (PanelQuery) listener;

								DistributedTableModelImpl tm2 = (DistributedTableModelImpl) pq
										.getTable().getModel();
								if (tm2.getDataSource().getUseCaseId()
										.intValue() != tm.getDataSource()
										.getUseCaseId().intValue()) {
									continue;
								}
							}
						}
					} catch (Throwable t) {
						handleException(t, true);
					}
				}
			}

			listener.changed(event);
			// AD: System.out.println(listener.getClass() + " " +
			// event.toString());
		}
	}

	/**
	 * Setze, wenn der Internalframe untere "Ohrwaschl" hat, hier das unterer
	 * als Defaultpanel, sonst ein oberes.
	 * 
	 * @param e
	 *            das untere oder obere Defaultpanel. / // public void
	 *            setPanelDetailUnteresOhrwaschl(PanelBasis panelDetailI) { //
	 *            this.panelDefaultUnteresOhrwaschl = panelDetailI; // }
	 */
	final public void stateChanged(javax.swing.event.ChangeEvent e) {
		long tStart = System.currentTimeMillis();
		try {
			// housekeeping
			myLogger.info("action start: "
					+ Helper.cutString(e.toString(), Defaults.LOG_LENGTH));
			frameProgress.start(LPMain.getTextRespectUISPr("lp.working"));
			setEnabled(false);
			lPStateChanged(e);
		} catch (Throwable t) {
			// housekeeping
			frameProgress.stop();
			// setEnabled(true);
			// long tEnd = System.currentTimeMillis();
			// myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
			handleException(t, true);
		} finally {
			// housekeeping
			frameProgress.pause();
			setEnabled(true);
			long tEnd = System.currentTimeMillis();
			myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
		}
	}

	public abstract void lPStateChanged(EventObject e) throws Throwable;

	/**
	 * actionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	final public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem
				&& e.getActionCommand().equals(WrapperMenuBar.ACTION_BEENDEN)) {

			// Modul schliessen
			PropertyVetoException pve = null;
			try {
				// Veto wenn gelockt und Daten sind geaendert!
				pve = vetoableChangeLP();
			} catch (Throwable ex) {
				myLogger.error(ex.getMessage(), ex);
			}

			if (pve == null) {
				// kein Veto: schliessen.
				LPMain.getInstance().getDesktop().closeFrame(this);
			}
		}

		else if (e.getSource() instanceof JMenuItem) {
			// modulspezifische Events werden an die subklasse weitergeleitet
			try {
				menuActionPerformed(e);
			} catch (Throwable t) {
				handleException(t, true);
			}
		}
	}

	/**
	 * Events aus dem menu verarbeiten
	 * 
	 * @param e
	 *            ausloesendes ActionEvent
	 * @throws Throwable
	 */
	protected void menuActionPerformed(ActionEvent e) throws Throwable {
		throw new Throwable("menuActionPerformed Not implemented yet!");
	}

	public ProgressTimer getFrameProgress() {
		return frameProgress;
	}

	private void disableAllAndShowOtherComponent(PanelDialog pbPanel) {
		PanelDialogStackElement e = (PanelDialogStackElement) getStack().peek();
		getStack().push(
				new PanelDialogStackElement(pbPanel, pbPanel.getOldTitle()));
		// wird aus PanelBasis gesetzt
		// this.setLpTitle(TITLE_IDX_OHRWASCHLOBEN, pbPanel.getAdd2Title());
		// fuer Reports und Dokumente nicht ueberschreiben
		if (pbPanel instanceof AssistentView) {
			this.setLpTitle(TITLE_IDX_OHRWASCHLOBEN, pbPanel.getAdd2Title());
		} else if (!(pbPanel instanceof PanelReportKriterien)
				&& !(pbPanel instanceof PanelDokumentenablage)) {
			this.setLpTitle(TITLE_IDX_AS_I_LIKE, "");
		}
		e.getJComponent().setVisible(false);
		getContentPane().remove(e.getJComponent());
		getContentPane().add(pbPanel, BorderLayout.CENTER);
		if (getJMenuBar() != null) {
			getJMenuBar().setVisible(false);
		}
	}

	private void enableAllAndRemoveComponent(boolean bKeepTitle) {
		if (getStack().size() >= 2) { // sicherheitshalber
			PanelDialogStackElement e = (PanelDialogStackElement) getStack()
					.pop();
			getContentPane().remove(e.getJComponent());
			getContentPane()
					.add(((PanelDialogStackElement) getStack().peek())
							.getJComponent(),
							BorderLayout.CENTER);
			(((PanelDialogStackElement) getStack().peek())).getJComponent()
					.setVisible(true);
			if (getJMenuBar() != null
					&& ((PanelDialogStackElement) getStack().peek())
							.getJComponent() == tabbedPaneRoot) {
				getJMenuBar().setVisible(true);
			}
			if (!bKeepTitle) {
				this.setTitle(e.getSTitle());
			}
		}
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien,
			boolean bNurVorschau) throws Throwable {
		showReportKriterien(panelKriterien, null, null, bNurVorschau);
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien)
			throws Throwable {
		showReportKriterien(panelKriterien, null, null, false);
	}

	public void componentMoved(java.awt.event.ComponentEvent arg0) {
		if (this.getLocation().getY() < 0) {
			this.setLocation(new Point((int) this.getLocation().getX(), 0));
		}
	}

	public void componentResized(java.awt.event.ComponentEvent arg0) {
		// int u = 0;
	}

	public void componentShown(java.awt.event.ComponentEvent arg0) {
		// int u = 0;
	}

	public void componentHidden(java.awt.event.ComponentEvent arg0) {
		// int u = 0;
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId)
			throws Throwable {
		showReportKriterien(panelKriterien, partnerDtoEmpfaenger,
				ansprechpartnerIId, false);
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bNurVorschau) throws Throwable {
		showReportKriterien(panelKriterien, partnerDtoEmpfaenger,
				ansprechpartnerIId, false, bNurVorschau);
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bDirekt, boolean bNurVorschau) throws Throwable {
		showReportKriterien(panelKriterien, partnerDtoEmpfaenger,
				ansprechpartnerIId, bDirekt, true, bNurVorschau);
	}

	public void showReportKriterien(PanelReportIfJRDS panelKriterien,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bDirekt, boolean bMitEmailFax, boolean bNurVorschau)
			throws Throwable {
		String sAdd2Title = null;
		if (panelKriterien instanceof PanelBasis) {
			sAdd2Title = ((PanelBasis) panelKriterien).getAdd2Title();
		}
		showPanelDialog(new PanelReportKriterien(this, panelKriterien,
				sAdd2Title, partnerDtoEmpfaenger, ansprechpartnerIId, bDirekt,
				bMitEmailFax, bNurVorschau));
	}

	// ***** wp & ghp *****
	// *****

	PanelDialogFehlmengen panelDialogFehlmengen;

	protected void showPanelDialogFehlmengen(
			PanelReportKriterien panelReportKriterien) {
		panelDialogFehlmengen = new PanelDialogFehlmengen(LPMain.getInstance()
				.getDesktop(),
				LPMain.getTextRespectUISPr("fert.report.fehlteilliste"),
				panelReportKriterien, this);
		panelDialogFehlmengen.setVisible(true);
	}

	public void showReportKriterienDialog(PanelReportIfJRDS panelKriterien,
			PanelReportKriterienOptions options) throws Throwable {

		String sAdd2Title = null;
		if (panelKriterien instanceof PanelBasis) {
			sAdd2Title = ((PanelBasis) panelKriterien).getAdd2Title();
		}

		options.setAddTitleI(sAdd2Title);

		// deaktiviere default exit button, da action aus panel reportkriterien
		// fuer internal frame nicht erreichbar
		options.setMitExitButton(false);
		PanelReportKriterien panelReportKriterienInDialog = new PanelReportKriterien(
				panelKriterien, options);

		// erzeuge neuen exit button mit lokaler action
		JButton buttonExit = new JButton(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/exit.png")));
		buttonExit.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonExit
				.setPreferredSize(HelperClient.getToolsPanelButtonDimension());

		buttonExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelDialogFehlmengen.dispose();

			}
		});

		panelReportKriterienInDialog.getToolBar().getToolsPanelRight()
				.add(buttonExit);

		showPanelDialogFehlmengen(panelReportKriterienInDialog);

	}

	// *****
	// **** wp & ghp ****

	public void showReportKriterienZweiDrucker(
			PanelReportIfJRDSZweiDrucker panelKriterien,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId,
			boolean bDirekt, boolean bMitEmailFax, boolean bNurVorschau,
			boolean bDruckbestaetigung) throws Throwable {
		String sAdd2Title = null;
		if (panelKriterien instanceof PanelBasis) {
			sAdd2Title = ((PanelBasis) panelKriterien).getAdd2Title();
		}
		showPanelDialog(new PanelReportKriterienZweiDrucker(this,
				panelKriterien, sAdd2Title, partnerDtoEmpfaenger,
				ansprechpartnerIId, bDirekt, bMitEmailFax, bNurVorschau,
				bDruckbestaetigung));
	}

	/**
	 * Einen modul-modalen Dialog anzeigen. modmod: 2 hier werden alle anderen
	 * Komponenten des IF's ausgeblendet.
	 * 
	 * @param panelDialog
	 *            PanelBasis
	 */
	public void showPanelDialog(PanelDialog panelDialog) {
		disableAllAndShowOtherComponent(panelDialog);

		// fuer die tstaturbedienung

		// panelDialog.revalidate() ;
		panelDialog.requestFocusInWindow();

		// panelDialog.grabFocus();
	}

	/**
	 * Einen modul-modalen Dialog schliessen.
	 * 
	 * modmod: 3 hier werden die anderen Komponenten wieder eingeblendet (das
	 * wird durch klick auf den abbruch-knopf automatisch erledigt)
	 */
	public void closePanelDialog() {
		boolean bKeepTitle = false;
		if (((PanelDialogStackElement) getStack().peek()).getJComponent() instanceof PanelDialogKriterien) {
			bKeepTitle = true;
		}
		enableAllAndRemoveComponent(bKeepTitle);
		// fuer die tstaturbedienung
		this.grabFocus();
	}

	/**
	 * Einen Panel mit Editor anzeigen.
	 * 
	 * @param target
	 *            JTextComponent
	 * @param addTitleI
	 *            String
	 * @param text
	 *            String
	 * @throws Throwable
	 */
	public void showPanelEditor(WrapperEditorField target, String addTitleI,
			String text) throws Throwable {
		showPanelEditor(target, addTitleI, text, PanelBasis.LOCK_NO_LOCKING);
	}

	public void showPanelEditor(WrapperEditorField target, String addTitleI,
			String text, int iLockState) throws Throwable {
		// modmod: 2 hier werden alle anderen Komponenten des IF's ausgeblendet.
		showPanelDialog(new PanelEditor(this, target, addTitleI, text,
				iLockState));
		getContentPane().validate(); // @sz um alle elemente entsprechend
		// ihrer visibility richtig anzuzeigen
	}

	/**
	 * Behandle ItemChangedEvent; zB. in QP auf einen anderen gewechselt.
	 * 
	 * @param eI
	 *            ChangeEvent
	 * @throws Throwable
	 */
	protected void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		throw new Throwable(
				"InternalFrame eventItemchanged event not supported: "
						+ eI.paramString());
	}

	/**
	 * Behandle ChangeEvent; zB. Laschenwechsel oben.
	 * 
	 * @param eI
	 *            ChangeEvent
	 * @throws Throwable
	 */
	protected void lPEventObjectChanged(ChangeEvent eI) throws Throwable {
		throw new Throwable("eventStateChanged event not supported: "
				+ Helper.cutString(eI.toString(), Defaults.LOG_LENGTH));
	}

	/**
	 * Eine Itemevent kommt herein.
	 * 
	 * @param e
	 *            ItemChangedEvent
	 */
	final public void changed(EventObject e) {
		long tStart = System.currentTimeMillis();

		try {
			// housekeeping
			myLogger.info("action start: "
					+ Helper.cutString(e.toString(), Defaults.LOG_LENGTH));
			getFrameProgress().start(LPMain.getTextRespectUISPr("lp.working"));
			setEnabled(false);

			if (e instanceof ItemChangedEvent) {
				lPEventItemChanged((ItemChangedEvent) e);
			} else if (e instanceof EventObject) {
				lPEventObjectChanged((ChangeEvent) e);
			}
		} catch (Throwable t) {
			// housekeeping
			getFrameProgress().stop();
			handleException(t, true);
		} finally {
			// housekeeping
			getFrameProgress().pause();
			setEnabled(true);
			long tEnd = System.currentTimeMillis();
			myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
		}
	}

	/**
	 * Deaktiviert/aktiviert alle Panels und TabbedPanes in einem Internalframe,
	 * ausser dem selektieren. Wenn das selektierte Panel ein SplitPane ist,
	 * dann wird ausserdem das QueryPanel mit deaktiviert.
	 * 
	 * @param enableI
	 *            boolean
	 */
	public void enableAllPanelsExcept(boolean enableI) {
		if (tabbedPaneRoot.getComponentCount() > 0) {
			if (tabbedPaneRoot.getComponents()[0] instanceof TabbedPane) {
				for (int i = 0; i < tabbedPaneRoot.getTabCount(); i++) {
					if (i != tabbedPaneRoot.getSelectedIndex()) {
						tabbedPaneRoot.setEnabledAt(i, enableI);
					} else {
						TabbedPane tabbedpane = (TabbedPane) tabbedPaneRoot
								.getSelectedComponent();
						for (int j = 0; j < tabbedpane.getTabCount(); j++) {
							if (j != tabbedpane.getSelectedIndex()) {
								tabbedpane.setEnabledAt(j, enableI);
							} else {
								enableTable(tabbedpane, enableI);
							}
						}
					}
				}
			}
			if (tabbedPaneRoot.getComponents()[0] instanceof PanelBasis) {
				for (int i = 0; i < tabbedPaneRoot.getTabCount(); i++) {
					if (i != tabbedPaneRoot.getSelectedIndex()) {
						tabbedPaneRoot.setEnabledAt(i, enableI);
					} else {
						enableTable(tabbedPaneRoot, enableI);
					}
				}
			}
		}
	}

	private void enableTable(JTabbedPane tabbedpane, boolean enableI) {
		if (tabbedpane.getComponentAt(tabbedpane.getSelectedIndex()) instanceof PanelSplit) {
			PanelSplit ps = (PanelSplit) tabbedpane.getComponentAt(tabbedpane
					.getSelectedIndex());
			PanelQuery pq = ps.getPanelQuery();
			pq.setEnabled(enableI);
			pq.getTable().setEnabled(enableI);
			if (enableI) {
				pq.getTable().setBackground(Color.WHITE);
				pq.getTable().setForeground(Color.BLACK);
				/** @todo JO bitte nicht loeschen PJ 4700 */
				if (mpq != null) {
					pq.getTable().addMouseListener(mpq);
				}
				mpq = null;
			} else {
				MouseListener[] ml = pq.getTable().getMouseListeners();
				for (int i = 0; i < ml.length; i++) {
					if (ml[i] instanceof PanelQuery) {
						mpq = ml[i];
					}
				}
				pq.getTable().removeMouseListener(mpq);
				pq.getTable().setBackground(Color.lightGray);
				pq.getTable().setForeground(Color.darkGray);
			}
		}
	}

	/**
	 * Aktiviert deaktiviere alle (oberen) Laschen ausser eine bestimmte, von
	 * einer (unteren) Lasche.
	 * 
	 * @param tpUnteresOhrI
	 *            TabbedPane
	 * @param iOberesOhrI
	 *            int
	 * @param enableI
	 *            boolean
	 */
	public void enableAllOberePanelsExceptMe(TabbedPane tpUnteresOhrI,
			int iOberesOhrI, boolean enableI) {

		if (tpUnteresOhrI.getComponentCount() > 0) {
			for (int i = 0; i < tpUnteresOhrI.getTabCount(); i++) {
				if (i != iOberesOhrI) {
					tpUnteresOhrI.setEnabledAt(i, enableI);
				}
			}
		}
	}

	public void geheZu(int iUnteresTab, int iOberesTab, Object key,
			Object keyFuerOberesPanel, FilterKriterium[] kritKey)
			throws Throwable {
		// Zuerst nachsehen, ob gelockt

		if (tabbedPaneRoot.getSelectedComponent() instanceof TabbedPane) {
			TabbedPane tp = (TabbedPane) tabbedPaneRoot.getSelectedComponent();
			if (tp.getSelectedComponent() instanceof PanelBasis) {
			}
		}

		if (tabbedPaneRoot.getTabCount() >= iUnteresTab) {

			tabbedPaneRoot.setSelectedIndex(iUnteresTab);
			if (tabbedPaneRoot.getSelectedComponent() instanceof TabbedPane) {
				TabbedPane tpComponent = (TabbedPane) tabbedPaneRoot
						.getSelectedComponent();
				if (tpComponent.getTabCount() >= iOberesTab) {

					tpComponent.setSelectedIndex(0);

					Component cp = tpComponent.getSelectedComponent();
					if (cp instanceof PanelQuery) {
						PanelQuery pq = (PanelQuery) cp;
						if (pq.isFilterActive()) {
							// Wenn in den Filtern was drinsteht, kann der
							// Datensatz evtl. nicht selektiert werden.
							// -> Filter leeren und refresh
							pq.clearAllFilters();
							pq.eventYouAreSelected(false);
						}

						// PJ18469 fkcombobox und schnellansicht leeren
						if (pq.getCbSchnellansicht() != null) {
							pq.getCbSchnellansicht().setSelected(false);
						}
						pq.setKeyOfFilterComboBox(null);

						if (DelegateFactory
								.getInstance()
								.getTheJudgeDelegate()
								.hatRecht(
										com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
							if (pq.getWcbVersteckteFelderAnzeigen() != null) {
								// Wenn Recht DARF_VERSTECKTE_SEHEN, dann

								pq.getWcbVersteckteFelderAnzeigen()
										.setSelected(true);
							}
						}

						pq.befuelleFilterkriteriumKey(kritKey);

						// Bereich in Projekt setzen
						if (this instanceof InternalFrameProjekt
								&& iUnteresTab == InternalFrameProjekt.IDX_TABBED_PANE_PROJEKT
								&& pq.fkComboBox != null) {
							ProjektDto projektDto = DelegateFactory
									.getInstance().getProjektDelegate()
									.projektFindByPrimaryKey((Integer) key);
							pq.setKeyOfFilterComboBox(projektDto
									.getBereichIId());

							if (tpComponent instanceof TabbedPaneProjekt) {

								TabbedPaneProjekt tpProjekt = (TabbedPaneProjekt) tpComponent;
								ActionEvent e = new ActionEvent(
										this,
										-1,
										TabbedPaneProjekt.MENU_ANSICHT_PROJEKT_ALLE);
								tpProjekt.lPActionEvent(e);
							}

						}

						pq.setSelectedId(key);
						pq.eventYouAreSelected(false);

						if (key != null && !key.equals(pq.getSelectedId())) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("lp.error.datensatznichtgefunden"));
						}

						tpComponent.setSelectedIndex(iOberesTab);

						if (keyFuerOberesPanel != null) {
							Component cpSub = tpComponent
									.getSelectedComponent();

							if (cpSub instanceof PanelSplit) {
								PanelSplit split = (PanelSplit) cpSub;
								if (split.getPanelQuery().isFilterActive()) {
									// Wenn in den Filtern was drinsteht, kann
									// der Datensatz evtl. nicht selektiert
									// werden.
									// -> Filter leeren und refresh
									split.getPanelQuery().clearAllFilters();
									split.getPanelQuery().eventYouAreSelected(
											false);
								}
								split.getPanelQuery().setSelectedId(
										keyFuerOberesPanel);
								split.eventYouAreSelected(false);
								// orig split.getPanelQuery()
								// .eventYouAreSelected(false);
							}

						}
						pq.befuelleFilterkriteriumKey(null);
					} else if (cp instanceof PanelSplit) {
						PanelSplit split = (PanelSplit) cp;

						split.getPanelQuery().befuelleFilterkriteriumKey(
								kritKey);

						if (split.getPanelQuery().isFilterActive()) {
							// Wenn in den Filtern was drinsteht, kann der
							// Datensatz evtl. nicht selektiert werden.
							// -> Filter leeren und refresh
							split.getPanelQuery().clearAllFilters();
							split.getPanelQuery().eventYouAreSelected(false);
						}
						split.getPanelQuery().setSelectedId(key);
						split.getPanelQuery().eventYouAreSelected(false);
						split.getPanelQuery().befuelleFilterkriteriumKey(null);
					}
				}
			}
		}

	}

	/**
	 * Aktiviert deaktiviere alle (oberen) Laschen ausser eine bestimmte.
	 * 
	 * @param iOberesOhrI
	 *            int
	 * @param enableI
	 *            boolean
	 */
	public void enableAllMyKidPanelsExceptMe(int iOberesOhrI, boolean enableI) {
		if (tabbedPaneRoot.getComponentCount() > 0) {
			for (int i = 0; i < tabbedPaneRoot.getTabCount(); i++) {
				if (i != iOberesOhrI) {
					tabbedPaneRoot.setEnabledAt(i, enableI);
				}
			}
		}
	}

	public String getKeyWasForLockMe() {
		return keyWasForLockMe;
	}

	public void setKeyWasForLockMe(String keyWasForLockMeI) {
		this.keyWasForLockMeOld = keyWasForLockMe;
		this.keyWasForLockMe = keyWasForLockMeI;
	}

	public String getKeyWasForLockMeOld() {
		return keyWasForLockMeOld;
	}

	/**
	 * @todo JO passt der platz da? PJ 4708 Workaraound f&uuml;r WordWrap : http
	 *       : //java.sun.com/developer/JDCTechTips/2004/tt0122.html
	 * 
	 * @param maxCharactersPerLineCount
	 *            int
	 * @return JOptionPane
	 * */
	public static JOptionPane getNarrowOptionPane(int maxCharactersPerLineCount) {
		// Our inner class definition
		class NarrowOptionPane extends JOptionPane {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;
			private int maxCharactersPerLineCount;

			NarrowOptionPane(int maxCharactersPerLineCount) {
				this.maxCharactersPerLineCount = maxCharactersPerLineCount;
			}

			public int getMaxCharactersPerLineCount() {
				return maxCharactersPerLineCount;
			}
		}

		return new NarrowOptionPane(maxCharactersPerLineCount);
	}

	/**
	 * handleex: Behandle Expection t; Meldung fuer den Benutzer; evtl. close
	 * Frame. Dies ist die zentrale Methode um allgemeine (frameweite)
	 * Exceptions abzuhandeln.
	 * 
	 * @param t
	 *            Throwable
	 * @param bHandleHardI
	 * <br/>
	 *            true ... Wird die Exception nicht gefunden kommt eine allg.
	 *            Errormeldung und der Internalframe wird geschlossen.<br/>
	 *            false ... Es wird versucht die Exception abzuhandeln, wenn
	 *            nicht moeglich, wird false retourniert; es wird keine Meldung
	 *            angezeigt
	 * @return boolean
	 */
	public boolean handleException(Throwable t, boolean bHandleHardI) {

		boolean bErrorBekannt = false;

		// Alles wird geloggt.
		if (t != null) {
			String sLog = t.getClass().getName() + ": "
					+ t.getLocalizedMessage();
			StackTraceElement[] ste = t.getStackTrace();
			if (ste.length > 0) {
				sLog = sLog + "\n" + ste[0].toString();
			}
			myLogger.error(sLog, t);
		}
		ExceptionLP efc = null;

		// myexception: 0 versuche, sie in der Unterklasse zu verarbeiten.
		if (t instanceof ExceptionLP) {
			efc = (ExceptionLP) t;
			if (handleOwnException(efc)) {
				return true;
			}
		}

		// Alle EJBExceptionLP behandeln.
		String sMsg = null;
		if (efc != null) {
			sMsg = LPMain.getInstance().getMsg(efc);
			bErrorBekannt = (sMsg != null);
			if (sMsg == null) {
				// exhc4: Fehlercode wird noch nicht abgefangen
				sMsg = "EJBExceptionLP, Fehlercode unbekannt: "
						+ efc.getICode();
			}
		}

		if (bErrorBekannt) {
			JOptionPane pane = getNarrowOptionPane(com.lp.client.pc.Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
			pane.setMessage(sMsg);
			pane.setMessageType(JOptionPane.ERROR_MESSAGE);
			JDialog dialog = pane.createDialog(this, "");
			dialog.setVisible(true);
		}

		if (!bErrorBekannt && bHandleHardI) {
			// exhc5: Meldung mit code und Meldung anzeigen
			// Internalframe schliessen.
			LPMain.getInstance().exitFrame(this);
		}

		return bErrorBekannt;
	}

	/**
	 * Eigene EJBExceptionLP's verarbeiten.
	 * 
	 * myexception: 1
	 * 
	 * @param exfc
	 *            EJBExceptionLP
	 * @return boolean
	 */
	protected boolean handleOwnException(ExceptionLP exfc) {
		return false;
	}

	/**
	 * evtvet: Event "Vetoable Window close"; wird null zurueckgegeben, so wird
	 * das Modul via dicard beendet, wird ein PropertyVetoException
	 * zurueckgegeben, bleibt das Modul "erhalten".
	 * 
	 * @throws Throwable
	 * @return PropertyVetoException
	 */
	public PropertyVetoException vetoableChangeLP() throws Throwable {
		PropertyVetoException exc = null;

		JComponent topComponent = getStack().peek().getJComponent();
		if (topComponent != null && topComponent != tabbedPaneRoot
				&& topComponent instanceof PanelBasis) {
			exc = ((PanelBasis) topComponent).vetoableChangeLP();
			if (exc != null)
				return exc;
		}

		TabbedPane tp = (TabbedPane) tabbedPaneRoot.getSelectedComponent();
		if (tp == null)
			return exc;

		// ***** wp & ghp *****
		// *****

		if (exc == null) {
			LPMain.getInstance().getDesktopController().behandleOffeneFehlmengen(this);			
//			if (FehlmengenAufloesen.getAufgeloesteFehlmengen().size() > 0) {
//				boolean bOption = DialogFactory
//						.showModalJaNeinDialog(
//								this,
//								LPMain.getTextRespectUISPr("lp.frage.fehlmengenaufloesendrucken"),
//								LPMain.getTextRespectUISPr("lp.hint"));
//				if (bOption) {
//					PanelReportKriterienOptions options = new PanelReportKriterienOptions();
//					options.setInternalFrame(this);
//					options.setMitEmailFax(true);
//					showReportKriterienDialog(
//							new ReportAufgeloestefehlmengen(this,
//									FehlmengenAufloesen
//											.getAufgeloesteFehlmengen()),
//							options);
//
//					exc = new PropertyVetoException("", null);
//				}
//				FehlmengenAufloesen.loescheAufgeloesteFehlmengen();
//			}
		}
		
		// *****
		// ***** wp & ghp *****

		exc = tp.vetoableChangeLP();
		if (getFrameProgress() != null) {
			try {
				getFrameProgress().stop();
			} catch (NullPointerException e) {
				// Wenn Nullpointer, dann ist bereits gestoppt worden
			}
		}
		return exc;
	}

	private Stack<PanelDialogStackElement> getStack() {
		if (vPanelDialog == null) {
			vPanelDialog = new Stack<PanelDialogStackElement>();
			vPanelDialog.push(new PanelDialogStackElement(tabbedPaneRoot,
					getTitle()));
		}
		return vPanelDialog;
	}

	/**
	 * Setze den Einbgabefocus auf die erste Component.
	 * 
	 * @throws Exception
	 */
	public final void setFirstFocusableComponent() throws Exception {

		try {
			// CK: Workaround fuer Kundenmodul (Noch kein TabbedPane):
			if (tabbedPaneRoot.getSelectedComponent() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) tabbedPaneRoot
						.getSelectedComponent();
				pq.setFirstFocusableComponent();
			}

			TabbedPane tp = (TabbedPane) tabbedPaneRoot.getSelectedComponent();
			tp.setFirstFocusableComponent();
		} catch (Exception ex) {
			myLogger.error("setFirstFocusableComponent(): noch keine TP! ", ex);
		}
	}

	public final JTabbedPane getTabbedPaneRoot() {
		return this.tabbedPaneRoot;
	}

	/**
	 * Initialierungen fuer die Komponenten. Muss zu einem Zeitpunkt aufgerufen
	 * werden, wenn die Komponenten nicht mehr null sind.
	 * 
	 * @throws Throwable
	 */
	protected final void initComponents() throws Throwable {
		// den childs namen geben
		HelperClient.setComponentNames(this);
		// dem internalFrame einen Namen geben.
		setName();
	}

	/**
	 * Setzt den Namen der Instanz auf "internalFrame...".
	 */
	private void setName() {
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			String className = this.getClass().getSimpleName();
			String sClassPraefix = "InternalFrame";

			if (!className.startsWith(sClassPraefix)) {
				myLogger.warn(className
						+ " entspricht nicht den Codierrichtlinien. Muss mit \""
						+ sClassPraefix + "\" beginnen");
			}

			setName(className.replaceFirst(sClassPraefix, "internalFrame"));
		}
	}

	public int execute(Command command) throws Throwable {
		return ICommand.COMMAND_NOT_DONE;
	}

	public void finalize() throws Throwable {
		// Listener ausraeumen.
		if (listeners != null) {
			listeners.removeAllElements();
		}
		// if (vPanelDialog != null) {
		// vPanelDialog.removeAllElements();
		// }
		// if (tabbedPaneRoot != null) {
		// tabbedPaneRoot.removeAll();
		// }
		// this.getContentPane().removeAll();
		super.finalize();
	}
	
	
	/**
	 * Irgendein Datensatz dieses Moduls ist gesperrt
	 */
	public void lock() {
		modulLocked = true ;
	}

	/**
	 * Keine gesperrte Datens&auml;tze dieses Moduls
	 */
	public void unlock() {
		modulLocked = false ;
	}
	
	public boolean isLocked() {
		return modulLocked ;
	}
}
