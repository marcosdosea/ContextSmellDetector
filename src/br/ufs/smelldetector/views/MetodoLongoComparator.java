package br.ufs.smelldetector.views;

import org.designroleminer.smelldetector.model.DadosMetodoSmell;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class MetodoLongoComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public MetodoLongoComparator() {
		this.propertyIndex = 1;
		direction = 0;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		DadosMetodoSmell p1 = (DadosMetodoSmell) e1;
		DadosMetodoSmell p2 = (DadosMetodoSmell) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			rc = p1.getCodigoMetodo().compareTo(p2.getCodigoMetodo());
			break;
		case 1:
			if (p1.getListaTecnicas().toString().length() > p2.getListaTecnicas().toString().length())
				rc = 1;
			else if (p1.getListaTecnicas().toString().length() < p2.getListaTecnicas().toString().length())
				rc = -1;
			else
				rc = p1.getListaTecnicas().toString().compareTo(p2.getListaTecnicas().toString());
			break;
		case 2:
			rc = p1.getNomeClasse().compareTo(p2.getNomeClasse());
			break;
		case 3:
			rc = p1.getCodigoMetodo().compareTo(p2.getCodigoMetodo());
		case 4:
			rc = p1.getNomeMetodo().compareTo(p2.getNomeMetodo());
			break;
		case 5:
			if (p1.getLinesOfCode() >= p2.getLinesOfCode()) {
				rc = 1;
			} else
				rc = -1;
			break;
		case 6:
			if (p1.getComplexity() >= p2.getComplexity()) {
				rc = 1;
			} else
				rc = -1;
			break;
		case 7:
			if (p1.getEfferent() >= p2.getEfferent()) {
				rc = 1;
			} else
				rc = -1;
			break;
		case 8:
			if (p1.getNumberOfParameters() >= p2.getNumberOfParameters()) {
				rc = 1;
			} else
				rc = -1;
			break;
		case 9:
			rc = p1.getSmell().toString().compareTo(p2.getSmell().toString());
			break;
		default:
			rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
