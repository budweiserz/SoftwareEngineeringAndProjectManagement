package at.ticketline.kassa.ui.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class TransaktionWizardDialog extends WizardDialog {

    public TransaktionWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);        
    }
    
    @Override
    public void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.BACK_ID).setText("Zurück");
        getButton(IDialogConstants.NEXT_ID).setText("Weiter");
        getButton(IDialogConstants.CANCEL_ID).setText("Abbrechen");
        getButton(IDialogConstants.FINISH_ID).setText("Fertig");
    }

}
