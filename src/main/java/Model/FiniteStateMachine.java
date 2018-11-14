package Model;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class FiniteStateMachine {
    private static final String SPLIT_CONSTANT_FOR_SYMBOLS =",";
    private static final String SPLIT_CONSTANT_FOR_TRANSITIONS =" ";
    private final File file;

    private int numberOfStates = 0;
    private int startingState = 0;
    private Set<Character> alphabet = new HashSet<>();
    private Set<Integer> finalStates = new HashSet<>();
    private Map<Integer, Map<Integer, Set<Character>>> transitions = new HashMap<>();

    public FiniteStateMachine(File file) {
        this.file = file;
    }

    public List<Character> getAlphabet() {
        return alphabet.stream().sorted().collect(Collectors.toList());
    }

    public List<String> getStates() {
        List<String> states = new ArrayList<>();
        for (int i=0; i<numberOfStates; i++) {
            states.add("q("+i+")");
        }
        return states;
    }

    public List<String> getFinalStates() {
        List<String> finalStateList = new ArrayList<>();
        for (int finalState : finalStates) {
            finalStateList.add("q(" + finalState + ")");
        }
        return finalStateList;
    }

    public Map<Integer, Map<Integer, Set<Character>>> getTransitions() {
        return transitions;
    }

    public void readFromFile() throws ParseException {
        String line;
        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            line = bufferedReader.readLine();
            numberOfStates = Integer.parseInt(line);

            line = bufferedReader.readLine();
            startingState = Integer.parseInt(line);

            line = bufferedReader.readLine();
            int numberFinalStates = Integer.parseInt(line);

            line = bufferedReader.readLine();
            String[] finalStatesString = line.split(SPLIT_CONSTANT_FOR_SYMBOLS);
            for (int i = 0; i < numberFinalStates; i++) {
                finalStates.add(Integer.parseInt(finalStatesString[i]));
            }

            line = bufferedReader.readLine();
            int transitionNumber = Integer.parseInt(line);

            for (int i = 0; i < transitionNumber; i++) {
                line = bufferedReader.readLine();
                String[] transitionString = line.split(SPLIT_CONSTANT_FOR_TRANSITIONS);
                int transitionFrom = Integer.parseInt(transitionString[0]);
                int transitionTo = Integer.parseInt(transitionString[1]);
                Set<Character> characterSet = new HashSet<>();
                String[] symbols = transitionString[2].split(SPLIT_CONSTANT_FOR_SYMBOLS);
                for (String symbol : symbols) {
                    alphabet.add(symbol.charAt(0));
                    characterSet.add(symbol.charAt(0));
                }
                if (!transitions.containsKey(transitionFrom)) {
                    transitions.put(transitionFrom, new HashMap<>());
                }
                transitions.get(transitionFrom).put(transitionTo, characterSet);
            }
        } catch (IOException exception) {
            throw new ParseException("File '" + file.getName() + "' can not be parsed!",0);
        }
    }

    public boolean sequenceAccepted(String sequence) {
        String longestAcceptedPrefix = longestSequence(sequence);
        return longestAcceptedPrefix != null && longestAcceptedPrefix.length() == sequence.length();
    }

    public String longestSequence(String sequence) {
        String longestSequence = null;
        int currentState = startingState;
        if (finalStates.contains(startingState)) {
            longestSequence = "";
        }
        for (int i=0; i<sequence.length(); i++) {
            boolean stateChanged = false;
            Map<Integer, Set<Character>> stateTransitionMap = transitions.get(currentState);
            if (stateTransitionMap == null) {
                break;
            }
            for (Map.Entry<Integer, Set<Character>> potentialState : stateTransitionMap.entrySet()) {
                if (potentialState.getValue().contains(sequence.charAt(i))) {
                    currentState = potentialState.getKey();
                    stateChanged = true;
                    break;
                }
            }
            if (stateChanged && finalStates.contains(currentState)) {
                longestSequence = sequence.substring(0, i + 1);
            }
            if (!stateChanged) {
                break;
            }
        }
        return longestSequence;
    }
}

