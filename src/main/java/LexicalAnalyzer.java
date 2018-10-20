import javafx.util.Pair;

import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class LexicalAnalyzer {
    private String fileName;
    private Map<Integer, String> SymbolTabel;
    private List<Pair<Integer, Integer>> ProgramInternalForm;

    private static final int MAX_IDENTIFIER_LENGTH = 250;

    public LexicalAnalyzer() {
    }

    public LexicalAnalyzer(String fileName, Map<Integer, String> symbolTabel, List<Pair<Integer, Integer>> programInternalForm) {
        this.fileName = fileName;
        SymbolTabel = symbolTabel;
        ProgramInternalForm = programInternalForm;
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
            int currentPosition = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String atom = atomDetection(line, currentPosition);
                if ((isValidKeyword(atom)) || (isValidOperator(atom)) || (isValidSeparator(atom))) {
                    //add(FIP,cod,-1);
                } else {
                    if ((isValidIdentifier(atom)) || (isValidConstant(atom))) {
                        //poz<-cauta(atom,TS);
                        //se returneaza pozitia pe care se afla atomin TS sau poz unde trebuie inserat
                    } else {
                        error = true;
                        System.out.println("Lexical error: found illegal character "+atom+" at line "+lineNumber);
                    }
                }
            }
            lineNumber++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }

    private String atomDetection(String line, int startPosition) {
        String atom = "";
        boolean error = false;
        int i = startPosition;
        while (!error) {
            int priorityStrongestTypeFound = 3;
            atom = line.substring(startPosition, i + 1);
            if (isValidKeyword(atom)) {
                priorityStrongestTypeFound = 1;
            }
            if (isValidOperator(atom)) {
                priorityStrongestTypeFound = 1;
            }
            if (isValidSeparator(atom)) {
                priorityStrongestTypeFound = 1;
            }
            if (isValidIdentifier(atom) && LexicalPriority.IDENTIFIER.getPriority() < priorityStrongestTypeFound) {
                priorityStrongestTypeFound = 2;
            }
            if (isValidConstant(atom) && LexicalPriority.CONSTANT.getPriority() < priorityStrongestTypeFound) {
                priorityStrongestTypeFound = 2;
            }
            if (priorityStrongestTypeFound == 3) {
                error = true;
            }
            i++;
        }
        if (atom.length() <= 1) {
            return atom;
        }
        return atom.substring(0, atom.length() - 1);
    }

    /**
     * Checks if an atom is a valid keyword
     *
     * @param atom - the atom that should be checked
     * @return TRUE - if atom is one of the keywords
     * FALSE - otherwise
     */
    private boolean isValidKeyword(String atom) {
        for (Keyword keyword : Keyword.values()) {
            if (atom.equalsIgnoreCase(keyword.toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidOperator(String atom) {
        for (Operator operator : Operator.values()) {
            if (atom.equalsIgnoreCase(operator.getOperator())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidSeparator(String atom) {
        for (Separator separator : Separator.values()) {
            if (atom.equals(separator.getSeparator())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidIdentifier(String atom) {
        return atom.matches("^[A-Za-z][A-Za-z0-9_]{0," + (MAX_IDENTIFIER_LENGTH - 1) + "}$");
    }

    private boolean isValidConstant(String atom) {
        return atom.matches("\"[^\"]*\"") || atom.matches("^[+-]?[0-9]*\\.?[0-9]+$");
    }
}
