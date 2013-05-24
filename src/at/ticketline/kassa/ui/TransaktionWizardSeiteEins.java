package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class TransaktionWizardSeiteEins extends WizardPage implements Listener {

    private Composite container;
    private Button btnBuchung;
    private Button btnReservierung;
    private TransaktionWizardValues values;
    
    /**
     * Diese Seite stellt einen grafischen Saalplan zur Auswahl
     * der Plätze zur Verfügung sowie die Möglichkeit zwischen Reservierung
     * und Buchung zu entscheiden
     */
    public TransaktionWizardSeiteEins(TransaktionWizardValues values) {
        super("saalplan");
        setTitle("Verfügbare Plätze");
        setDescription("Wählen Sie die gewünschten Plätze und die Art der Transaktion aus");
        this.values = values;
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        //TODO Add Saalplan in here... somehow
        
        setControl(container);
        
        btnBuchung = new Button(container, SWT.RADIO);
        btnBuchung.addListener(SWT.Selection, this);
        btnBuchung.setBounds(10, 10, 90, 16);
        btnBuchung.setText("Buchung");
        
        btnReservierung = new Button(container, SWT.RADIO);
        btnReservierung.addListener(SWT.Selection, this);
        btnReservierung.setBounds(106, 10, 90, 16);
        btnReservierung.setText("Reservierung");
        
        //TODO make true when plaetze selected
        setPageComplete(false);

        
    }

    @Override
    public void handleEvent(Event e) {
        if(e.widget == btnBuchung) {
            setPageComplete(true);
            values.setReservierung(false);
        }
        if(e.widget == btnReservierung) {
            setPageComplete(true);
            values.setReservierung(true);
        }
        
    }
}
