package Controller;

import Model.FiniteStateMachine;

import java.io.File;
import java.text.ParseException;

public class FSMController {
    private static final String FILE_FSM_IDENTIFIERS = "src/main/resources/identifiersFSM.in";
    private static final String FILE_FSM_NUMERIC_CONSTANTS = "src/main/resources/numericConstantsFSM.in";
    private final FiniteStateMachine identifiersFSM = new FiniteStateMachine(new File(FILE_FSM_IDENTIFIERS));
    private final FiniteStateMachine numericConstantsFSM = new FiniteStateMachine(new File(FILE_FSM_NUMERIC_CONSTANTS));

    public FSMController() {
        try {
            identifiersFSM.readFromFile();
            numericConstantsFSM.readFromFile();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public FiniteStateMachine getIdentifiersFSM() {
        return identifiersFSM;
    }

    public FiniteStateMachine getNumericConstantsFSM() {
        return numericConstantsFSM;
    }
}
