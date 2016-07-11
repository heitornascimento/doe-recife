/**
 * 
 */
package br.com.doe.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author heitornsouza
 * 
 */
public class HistoricList implements Serializable {

	private ArrayList<HashMap<Integer, Integer>> historicInstitution = new ArrayList<HashMap<Integer, Integer>>();

	public HistoricList(ArrayList<HashMap<Integer, Integer>> historicInstitution) {
		this.historicInstitution = historicInstitution;
	}

	/**
	 * @return the historicInstitution
	 */
	public ArrayList<HashMap<Integer, Integer>> getHistoricInstitution() {
		return historicInstitution;
	}

	/**
	 * @param historicInstitution
	 *            the historicInstitution to set
	 */
	public void setHistoricInstitution(
			ArrayList<HashMap<Integer, Integer>> historicInstitution) {
		this.historicInstitution = historicInstitution;
	}

}
