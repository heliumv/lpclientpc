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
package com.lp.client.frame.component.frameposition;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.Helper;

/**
 * K&uuml;mmert sich um das Speichern, Laden und Anwenden von Layouts.
 * Frames sollten mittels <code>addInternalFrame(InternalFrame)</code> hinzugef&uuml;gt werden,
 * bevor sie sichtbar gemacht werden (zB. mit <code>frame.setVisible(true)</code>). Somit wird die
 * Position und Gr&ouml;&szlig;e des Frames beim sichtbar machen automatisch aus den gespeicherten
 * Einstellungen gelesen und darauf angewendet.
 * Nach einem Benutzerwechsel muss(!) <code>reinit()</code> aufgerufen werden, sonst
 * werden die Layouteinstellungen dem Falschen user zugeordnet.
 * @author robert
 */

public class ClientPerspectiveManager implements InternalFrameListener {	

	private static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private static ClientPerspectiveManager singleton;

	private PrintStream output = null;
	private PrintStream errOutput = null;
	private ClientPerspectiveLoaderAndSaver cpLoaderAndSaver;
	
	protected ClientPerspectiveManager(IClientPerspectiveIO cpIO) {
		cpLoaderAndSaver = new ClientPerspectiveLoaderAndSaver(cpIO);
		
//		setOutput(System.out);
//		setErrorOut(System.err);
	}
	
	/**
	 * Gibt das Singleton <code>ClientPespectiveManager</code> zur&uuml;ck.
	 * @return singleton ClientPerspectiveManager
	 */
	public static ClientPerspectiveManager getInstance() {
		if(singleton == null)
			try {
				singleton = ClientPerspectiveBuilder.createDefaultManager();
			} catch (Throwable e) {
				return null;
			}
		return singleton;
	}
	
	/**
	 * Instanziert das Singleton <code>ClientPerspectiveManager</code> beim n&auml;chsten
	 * <code>getInstance</code>.<br>
	 * <b>Nur so kann ein Benutzerwechsel beim n&auml;chsten Laden/Speichern des Layouts
	 * ber&uuml;cksichtigt werden.</b>
	 */
	public void reinit() {
		singleton = null;
	}
	
	private void println(String x) {
		if(output != null)
			output.println(x);
	}

	private void errPrintlnMsg(String x) {
		errPrintln(x);
		JOptionPane.showMessageDialog(null, x);
	}
	private void errPrintln(String x) {
		if(errOutput != null)
			errOutput.println(x);
	}
	
	/**
	 * L&ouml;scht die Layouteinstellungen aus dem Filesystem / der DB.
	 */
	public void deleteSettings() {
		try {
			cpLoaderAndSaver.deleteSettings();
			println("Anzeigeeinstellungen gel\u00F6scht.");
		} catch (Exception e) {
			//TODO: ordentliche fehlermeldung
			errPrintln("Fehler beim L\u00F6schen der Anzeigeeinstellungen.");
//			e.printStackTrace();
		}
	}
	
	/**
	 * Schreibt die aktuellen Layouteigenschaften in den Buffer.
	 */
	private void updateLayout() {
		Desktop d = LPMain.getInstance().getDesktop();
		setStartupModule(d.getOffeneLPModule());
		setDesktopSettings(d);
		for(String modul : getStartupModule())
			cpLoaderAndSaver.updateFramePosition(d.getLPModul(modul));
	}
	
	/**
	 * L&auml;dt die Layouteigenschaften aus dem Filesystem / der DB und wendet sie an.
	 * @param d 
	 * @return true wenn erfolgreich, false wenn keine settings gefunden wurden
	 */
	public boolean load(Desktop d) {
		if(!doLayoutSettingsExist())
			return false;
		loadAndApplyDesktopSettings(d);
		holeDesktopStartupModule(d);
		return true;
	}
	
	/**
	 * Holt die Layouteigenschaften und speichert sie ab.
	 */
	public void save() {
		try {
			updateLayout();
			cpLoaderAndSaver.persistAllSettings();
			println("Alle Anzeigeeinstellungen gespeichert.");
		} catch (Exception e) {
			//TODO: ordentliche fehlermeldung
			errPrintlnMsg("Konnte Anzeigeeinstellungen nicht speichern.");
			e.printStackTrace();
		}
		cpLoaderAndSaver =  new ClientPerspectiveLoaderAndSaver(getClientPerspectiveIO()); 
	}
	
	public void saveInternalFramePosition(String belegart) {
		Desktop d = LPMain.getInstance().getDesktop();
		cpLoaderAndSaver.updateFramePosition(d.getLPModul(belegart));
		try {
			cpLoaderAndSaver.saveFramePosition(belegart);
		} catch (Exception e) {
			errPrintlnMsg("Konnte Anzeigeeinstellungen von Modul nicht speichern: " + belegart);
			e.printStackTrace();
		}
	}
	
	public void saveQueryFLRPosition(PanelQueryFLR panel) {
		cpLoaderAndSaver.updateFramePosition(panel);
		try {
			cpLoaderAndSaver.saveFramePosition(Integer.toString(panel.getIdUsecase()));
		} catch (Exception e) {
			errPrintlnMsg("Konnte Anzeigeeinstellungen von PanelQueryFLR nicht speichern. UC = " + panel.getIdUsecase());
			e.printStackTrace();
		}
	}
	
	public void loadAndApplyQueryFLRPosition(PanelQueryFLR panel) {
		try {
			FramePositionData fpData = cpLoaderAndSaver.getFramePositionData(Integer.toString(panel.getIdUsecase()));
			fpData.applyTo(panel);
		} catch (FileNotFoundException e) {
			errPrintln("Keine Anzeigeeinstellungen f\u00FCr PanelQueryFLR. UC = " + panel.getIdUsecase());
		} catch (Exception e) {
			errPrintln("Konnte Anzeigeeinstellungen von PanelQueryFLR nicht laden. UC = " + panel.getIdUsecase());
			e.printStackTrace();
		}
	}
	/**
	 * Speichert die Sortierkriterien der Tabelle im Layout
	 * @param usecaseId die ucId des <code>PanelQuery</code>
	 * @param kriterien eine <code>List\<SortierKriterium\></code>
	 */
	public void saveQueryColumnSorting(int usecaseId, List<SortierKriterium> kriterien) {
		try {
			kriterien = removeEmptyKriterien(kriterien);
			cpLoaderAndSaver.saveQueryColumnSorting(usecaseId, kriterien);
			println("Sortierkriterien f\u00FCr UC " + usecaseId + " erfolgreich gespeichert.");
		} catch (Exception e) {
			errPrintlnMsg("Konnte die Sortierkriterien nicht speichern f\u00FCr UC " + usecaseId);
			e.printStackTrace();
		}
	}
	
	/**
	 * Entfernt alle SortierKriterien, in welchen <code>kritName null</code> oder ein leerer String ist.
	 * @param kriterien wird nicht ver&auml;ndert
	 * @return die eine neue Liste mit Sortierkriterien
	 */
	private List<SortierKriterium> removeEmptyKriterien(List<SortierKriterium> kriterien) {		
		List<SortierKriterium> clearedKriterien = new ArrayList<SortierKriterium>();
		for (SortierKriterium sortierKriterium : kriterien) {
			if(!Helper.isStringEmpty(sortierKriterium.kritName)) {
				clearedKriterien.add(sortierKriterium);
			}
		}
		return clearedKriterien;
	}
	
	/**
	 * L&auml;dt die Sortierkriterien der Tabelle aus dem Layout
	 * @param usecaseId
	 * @return falls gespeicherte gefunden wurden, die Spaltenbreiten, sonst null.
	 */
	public List<SortierKriterium> loadQueryColumnSorting(int usecaseId) {
		try {
			List<SortierKriterium> kriterien = cpLoaderAndSaver.loadQueryColumnSorting(usecaseId);
			println("Sortierkriterien f\u00FCr UC " + usecaseId + " erfolgreich geladen.");
			return removeEmptyKriterien(kriterien);
		} catch (Exception e) {
			errPrintln("F\u00FCr den UC " + usecaseId + " wurden keine Sortierkriterien gefunden");
		}
		return null;
	}

	/**
	 * Speichert die Spaltenbreiten der Tabelle im Layout
	 * @param usecaseId die ucId des <code>PanelQuery</code>
	 * @param widths eine <code>List\<Integer\></code> mit den Breiten der Spalten
	 */
	public void saveQueryColumnWidths(int usecaseId, List<Integer> widths) {
		try {
			cpLoaderAndSaver.saveQueryColumnWidths(usecaseId, widths);
			println("Spaltenbreiten f\u00FCr UC " + usecaseId + " erfolgreich gespeichert.");
		} catch (Exception e) {
			errPrintlnMsg("Konnte die Spaltenbreiten nicht speichern f\u00FCr UC " + usecaseId);
			e.printStackTrace();
		}
	}


	/**
	 * L&auml;dt die Spaltenbreiten der Tabelle aus dem Layout
	 * @param usecaseId
	 * @return falls gespeicherte gefunden wurden, die Spaltenbreiten, sonst null.
	 */
	public List<Integer> loadQueryColumnWidths(int usecaseId) {
		try {
			List<Integer> widths = cpLoaderAndSaver.loadQueryColumnWidths(usecaseId);
			println("Spaltenbreiten f\u00FCr UC " + usecaseId + " erfolgreich geladen.");
			return widths;
		} catch (Exception e) {
			errPrintln("F\u00FCr den UC " + usecaseId + " wurden keine Spaltenbreiten gefunden");
		}
		return null;
	}
	
	/**
	 * L&auml;dt die Spaltenbreiten der Tabelle aus dem Layout
	 * @param usecaseId
	 * @return falls gespeicherte gefunden wurden, die Spaltenbreiten, sonst null.
	 */
	public int[] loadQueryColumnWidthsAsArray(int usecaseId) {
		List<Integer> widths = loadQueryColumnWidths(usecaseId);
		if(widths == null) return null;
		int[] array = new int[widths.size()];
		for(int i = 0; i < widths.size(); i++) {
			array[i] = widths.get(i);
		}
		return array;
	}
	
	public void setOutput(PrintStream output) {
		this.output = output;
	}
	
	public void setErrorOut(PrintStream errOutput) {
		this.errOutput = errOutput;
	}
	
	/**
	 * L&auml;dt die Einstellungen des Desktop-Fensters aus dem Filesystem / der DB
	 * und wendet sie auf dieses an. Sollten die gespeicherten Ausma&szlig;e gr&ouml;&szlig;er sein als
	 * die Bildschirmaufl&ouml;sung, werden die Einstellungen nicht &uuml;bernommen und false zur&uuml;ckgegeben 
	 * @param desktop
	 * @return false wenn, und nur wenn, die gespeicherte Gr&ouml;&szlig;e des Fensters
	 * gr&ouml;&szlig;er ist als die Desktopaufl&ouml;sung
	 */
	private boolean loadAndApplyDesktopSettings(Desktop desktop) {
		FramePositionData desktopData;
		try {
			desktopData = cpLoaderAndSaver.getDesktopPositionData();
			if(desktopData != null) {
				if(isDesktopBiggerThanScreen(desktopData)) {
					println("Geladene Desktopgr\u00F6\u00DFe (" + desktopData.getSize().toString()
							+ ") ist gr\u00F6\u00DFer als Bildschirmaufl\u00F6sung (" + SCREENSIZE.toString() + ")");
					return false;
				}
				desktopData = fitLocationToScreen(desktopData);
				desktopData.applyTo(desktop);

				println("Desktopeinstellungen erfolgreich geladen.");
			}
		} catch (Exception e) {
			//TODO: ordentliche fehlermeldung
			errPrintlnMsg("Konnte Desktopeinstellungen nicht laden.");
			e.printStackTrace(errOutput);
		}
		return true;
	}
	
	private boolean isDesktopBiggerThanScreen(FramePositionData desktopData) {
		return desktopData.getSize().width > SCREENSIZE.width
		|| desktopData.getSize().height > SCREENSIZE.height;
	}
	
	private FramePositionData fitLocationToScreen(FramePositionData desktop) {
		Point newLoc = desktop.getLocation();
		if(desktop.getSize().width + desktop.getLocation().x > SCREENSIZE.width)
			newLoc.x = 0;
		if(desktop.getSize().height + desktop.getLocation().y > SCREENSIZE.height)
			newLoc.y = 0;
		desktop.setLocation(newLoc);
		return desktop;
	}
	
	private void setDesktopSettings(Desktop desktop) {
		cpLoaderAndSaver.setDesktopPositionData(new FramePositionData(desktop));
	}
	
	private List<String> getStartupModule() {
		try {
			return cpLoaderAndSaver.getStartupModule();
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
	private void setStartupModule(List<String> startupModule) {
		cpLoaderAndSaver.setStartupModule(startupModule);
	}
	
	private void holeDesktopStartupModule(Desktop desktop) {
		try {
			for(String startupModul:cpLoaderAndSaver.getStartupModule()) {
				try {
					if (desktop.isModulEnabled(startupModul)) {
						desktop.holeModul(startupModul);
						println("Modul " + startupModul.trim() + " geladen.");
					} else {
						println("Kein Recht f\u00FCr Modul " + startupModul.trim());
					}
				} catch (Throwable e) {
					errPrintln("Konnte Modul " + startupModul.trim() + " nicht \u00F6ffnen.");
//					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			//TODO: ordentliche fehlermeldung
			errPrintln("Fehler beim laden der ModulStartup-Liste.");
			e.printStackTrace();
		}
	}
	
	public boolean doLayoutSettingsExist() {
		return cpLoaderAndSaver.doSavedSettingsExist();
	}
	
	private IClientPerspectiveIO getClientPerspectiveIO() {
		return new ClientPerspectiveFileIO(new LocalSettingsPathGenerator().getUserPath());
	}
	
	/**
	 * Der InternalFrame muss nur mit dieser Methode hinzugef&uuml;gt werden
	 * <b>bevor</b> er ge&ouml;ffnet wird. Das Speichern usw. regelt der Manager.
	 * @param frameToManage
	 */
	public void addInternalFrame(InternalFrame frameToManage) {
		frameToManage.addInternalFrameListener(this);
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		e.getInternalFrame().removeInternalFrameListener(this);
	}

	/**
	 * Modul wird geschlossen,
	 * Frameeigenschaften speichern.
	 */
	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		InternalFrame iFrame = (InternalFrame)e.getInternalFrame();
		cpLoaderAndSaver.updateFramePosition(iFrame);
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	/**
	 * Modul wird ge&ouml;ffnet,
	 * Frameeigenschaften laden und anwenden.
	 */
	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		InternalFrame iFrame = (InternalFrame)e.getInternalFrame();
		if(iFrame.isVisible()) return;
		try {
			cpLoaderAndSaver.applyFramePosition(iFrame);
			println("Anzeigeeinstellungen des Moduls " + iFrame.getBelegartCNr().trim() + " erfolgreich geladen.");
		} catch (Exception e1) {
			//TODO: ordentliche fehlermeldung
			errPrintln("Konnte Anzeigeeinstellungen des Moduls " + iFrame.getBelegartCNr().trim() + " nicht laden.");
//			e1.printStackTrace();
		}
	}
	
	public void saveFont(Font font) {
		try {
			cpLoaderAndSaver.saveFont(font);
		} catch (Exception e) {
			errPrintln("Font " + font.toString() + " konnte nicht gespeichert werden");
		}
	}
	
	public Font readFont() {
		try {
			return cpLoaderAndSaver.readFont();
		} catch (Exception e) {
			errPrintln("Font konnte nicht gelesen werden");
		}
		return null;
	}
	
	public void resetFont() {
		try {
			cpLoaderAndSaver.resetFont();
		} catch (Exception e) {
			errPrintln("Font konnte nicht zur\u00FCckgesetzt werden");
		}
	}

	public void saveColorVision(String colorVision) {
		try {
			cpLoaderAndSaver.saveColorVision(colorVision);
		} catch (Exception e) {
			errPrintln("ColorVision konnte nicht gespeichert werden");
		}
	}
	
	public String readColorVision() {
		try {
			return cpLoaderAndSaver.readColorVision();
		} catch (Exception e) {
			errPrintln("ColorVision konnte nicht gelesen werden");
		}
		return null;
	}
	
	public void setDirekthilfeEnabled(boolean b) {
		try {
			cpLoaderAndSaver.saveDirekthilfeVisible(b);
		} catch (Exception e) {
			errPrintln("ColorVision konnte nicht gespeichert werden");
		}
	}
	
	public boolean isDirekthilfeVisible() {
		try {
			return cpLoaderAndSaver.readDirekthilfeVisible();
		} catch (Exception e) {
			errPrintln("ColorVision konnte nicht gelesen werden");
		}
		return true;
	}
}
