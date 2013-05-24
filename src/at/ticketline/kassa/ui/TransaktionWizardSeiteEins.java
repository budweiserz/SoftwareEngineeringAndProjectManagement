package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class TransaktionWizardSeiteEins extends WizardPage {

    private Composite container;
    
    /**
     * Diese Seite stellt einen grafischen Saalplan zur Auswahl
     * der Plätze zur Verfügung sowie die Möglichkeit zwischen Reservierung
     * und Buchung zu entscheiden
     */
    public TransaktionWizardSeiteEins() {
        super("saalplan");
        setTitle("Verfügbare Plätze");
        setDescription("Wählen Sie die gewünschten Plätze und die Art der Transaktion aus");
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        //TODO Add Saalplan in here... somehow
        
        setControl(container);
        
        Button btnBuchung = new Button(container, SWT.RADIO);
        btnBuchung.setBounds(10, 10, 90, 16);
        btnBuchung.setText("Buchung");
        
        Button btnReservierung = new Button(container, SWT.RADIO);
        btnReservierung.setBounds(106, 10, 90, 16);
        btnReservierung.setText("Reservierung");
        
        //TODO make true when plaetze selected
        setPageComplete(true);

        
    }
}
