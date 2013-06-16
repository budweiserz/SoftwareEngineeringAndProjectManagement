package at.ticketline.kassa.ui;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;

import at.ticketline.entity.Kunde;

@SuppressWarnings("restriction")
public class NewKundePart extends KundePart {

    @Inject private MPart activePart;

    @Override
    protected void overrideableInit(Composite parent, Kunde kunde) {
        /*
         * XXX: When multiple Tabs are open Eclipse will show the first x 
         * Kuenstler in the first tab. Then the first x-1 Kuenstler in the 
         * second tab and so on.
         * Having a created boolean fixes this.
         */
        if(created == false) {
            created = true;
            createControls(parent);
        }
        
        activePart.setLabel("Neuen Kunden erstellen");
    }
}
