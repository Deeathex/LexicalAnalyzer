package Controller;

import Model.*;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LexicalAnalyzer {
    private String fileName;
    private List<Pair<Integer, String>> symbolTable = new ArrayList<>();
    private List<Pair<Integer, Integer>> programInternalForm = new ArrayList<>();
    private final FSMController fsmController = new FSMController();
    private static final boolean FSM = true;

    private static final int MAX_IDENTIFIER_LENGTH = 250;

    public LexicalAnalyzer(String fileName) {
        this.fileName = fileName;
    }

    public LexicalAnalyzer(String fileName, List<Pair<Integer, String>> symbolTable, List<Pair<Integer, Integer>> programInternalForm) {
        this.fileName = fileName;
        this.symbolTable = symbolTable;
        this.programInternalForm = programInternalForm;
    }

    /**
     * @return TRUE if the lexical analyzer doesn't find lexical errors and the Program Internal Form and the Symbol Table are
     * generated
     * FALSE otherwise and some information about the errors and the line that caused them
     * @author Ciforac Andreea 2018
     */
    public boolean analyze() {
        boolean error = false;
        File file = new File(fileName);
        int lineNumber = 1;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int currentPosition = 0;
                while (currentPosition < line.length()) {
                    Pair<String, Integer> atomAndCode = atomDetection(line, currentPosition);
                    String atom = atomAndCode.getKey();
                    //System.out.println(atom);
                    currentPosition += atom.length();
                    int code = atomAndCode.getValue();
                    if ((isValidKeyword_regex(atom) != -1) || (isValidOperator_regex(atom) != -1) || (isValidSeparator_regex(atom) != -1)) {
                        addToProgramInternalForm(code, -1);
                    } else {
                        if ((isValidIdentifier(atom) != -1) || (isValidConstant(atom) != -1)) {
                            int positionInSymbolTable = searchInSymbolTable(atom);
                            if (positionInSymbolTable == -1) {
                                positionInSymbolTable = insertInSymbolTable(atom);
                            }
                            addToProgramInternalForm(code, positionInSymbolTable);
                        } else {
                            error = true;
                            System.err.println("Lexical error: found illegal character " + atom + " at line " + lineNumber);
                        }
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }

    /**
     * This method detects the longest possible atom that can be found starting from the startPosition param
     * and identifies the code of the atom too.
     *
     * @param line          - represents the line read from the source code file
     * @param startPosition - represents the position from which it has to find the longest possible atom
     * @return a Pair<String, Integer> - the String (KEY) is the atom and the Integer (VALUE) is the code of the atom
     */
    private Pair<String, Integer> atomDetection(String line, int startPosition) {
        String atom = "";
        boolean error = false;
        int i = startPosition;
        int priorityStrongestTypeFound = 3;
        int code;
        int codeToBeReturned = -1;
        // string literal check
        if (line.charAt(startPosition) == '"' && startPosition < line.length() - 1) {
            i++;
            while (i < line.length() - 1 && line.charAt(i) != '"') {
                i++;
            }
            atom = line.substring(startPosition, i + 1);
            if (line.charAt(i) != '"') {
                codeToBeReturned = isValidConstant(atom);
                return new Pair<>(atom, codeToBeReturned);
            }
        }
        while (i<line.length() && !error) {
            priorityStrongestTypeFound = 3;
            atom = line.substring(startPosition, i + 1);
            if ((code = isValidKeyword_regex(atom)) != -1) {
                codeToBeReturned = code;
                priorityStrongestTypeFound = 1;
            }
            if ((code = isValidOperator_regex(atom)) != -1) {
                codeToBeReturned = code;
                priorityStrongestTypeFound = 1;
            }
            if ((code = isValidSeparator_regex(atom)) != -1) {
                codeToBeReturned = code;
                priorityStrongestTypeFound = 1;
            }
            if ((code = isValidIdentifier(atom)) != -1 && LexicalPriority.IDENTIFIER.getPriority() < priorityStrongestTypeFound) {
                codeToBeReturned = code;
                priorityStrongestTypeFound = 2;
            }
            if (((code = isValidConstant(atom)) != -1 && LexicalPriority.CONSTANT.getPriority() < priorityStrongestTypeFound) ||
                    ((i + 1 < line.length()) && (code = isValidConstant(line.substring(startPosition, i + 2))) != -1)) {
                codeToBeReturned = code;
                priorityStrongestTypeFound = 2;
            }
            if (priorityStrongestTypeFound == 3) {
                error = true;
            }
            i++;
        }
        if (atom.length() <= 1) {
            return new Pair<>(atom, codeToBeReturned);
        }
        if (error) {
            return new Pair<>(atom.substring(0, atom.length() - 1), codeToBeReturned);
        }
        return new Pair<>(atom.substring(0, atom.length()), codeToBeReturned);
    }

    /**
     * Checks if an atom is a valid keyword
     *
     * @param atom - the atom that should be checked
     * @return the atom's corresponding code - if atom is one of the keywords
     * -1 - otherwise
     */
    private int isValidKeyword_regex(String atom) {
        for (Keyword keyword : Keyword.values()) {
            if (atom.equalsIgnoreCase(keyword.toString())) {
                return keyword.getCode();
            }
        }
        return -1;
    }

    private int isValidOperator_regex(String atom) {
        for (Operator operator : Operator.values()) {
            if (atom.equalsIgnoreCase(operator.getOperator())) {
                return operator.getCode();
            }
        }
        return -1;
    }

    private int isValidSeparator_regex(String atom) {
        for (Separator separator : Separator.values()) {
            if (atom.equals(separator.getSeparator())) {
                return separator.getCode();
            }
        }
        return -1;
    }

    private int isValidIdentifier(String atom) {
        return FSM ? isValidIdentifier_FSM(atom) : isValidIdentifier_regex(atom);
    }

    private int isValidConstant(String atom) {
        return FSM ? isValidConstant_FSM(atom) : isValidConstant_regex(atom);
    }

    private int isValidIdentifier_regex(String atom) {
        if (atom.matches("^[A-Za-z][A-Za-z0-9_]{0," + (MAX_IDENTIFIER_LENGTH - 1) + "}$")) {
            return Identifier.IDENTIFIER.getCode();
        }
        return -1;
    }

    private int isValidConstant_regex(String atom) {
        if (atom.matches("\"[^\"]*\"") || atom.matches("^[+-]?[0-9]*\\.?[0-9]+$")) {
            return Constant.CONSTANT.getCode();
        }
        return -1;
    }

    private int isValidIdentifier_FSM(String atom) {
        if (fsmController.getIdentifiersFSM().sequenceAccepted(atom)) {
            return Identifier.IDENTIFIER.getCode();
        }
        return -1;
    }

    private int isValidConstant_FSM(String atom) {
        if (atom.matches("\"[^\"]*\"") || fsmController.getNumericConstantsFSM().sequenceAccepted(atom)) {
            return Constant.CONSTANT.getCode();
        }
        return -1;
    }



    /**
     * Adds a pair of (code,position) where code that corresponds to the found atom, and position corresponds the
     * with position in Symbol Table; position -1 represents that the atom doesn't belong to the Symbol Table)
     *
     * @param code     - the corresponding atom code of the atom from the Model.AtomTable list (enum)
     * @param position - the position from Symbol Table
     */
    private void addToProgramInternalForm(Integer code, Integer position) {
        programInternalForm.add(new Pair<>(code, position));
    }

    /**
     * @param atom - the atom we are looking for the corresponding position
     * @return This method returns the position of the atom in the Symbol Table or -1 if the atom doesn't exist in the ST
     */
    private int searchInSymbolTable(String atom) {
        if (!symbolTable.stream().map(Pair::getValue).collect(Collectors.toList()).contains(atom)) {
            return -1;
        }
        for (Pair<Integer, String> pair : symbolTable) {
            if (Objects.equals(pair.getValue(), atom)) {
                return pair.getKey();
            }
        }
        return -1;
    }

    private int insertInSymbolTable(String atom) {
        int code = symbolTable.size();
        Pair<Integer, String> pair = new Pair<>(code, atom);
        symbolTable.add(pair);
        symbolTable.sort(Comparator.comparing(Pair::getValue));
        return code;
    }

    public List<Pair<Integer, String>> getSymbolTable() {
        return symbolTable;
    }

    public List<Pair<Integer, Integer>> getProgramInternalForm() {
        return programInternalForm;
    }
}
