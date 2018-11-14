import Controller.LexicalAnalyzer;
import javafx.util.Pair;

import java.util.List;

public class Main {
    public static final String FILE = "src/main/resources/program1.txt";
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(FILE);
        lexicalAnalyzer.analyze();
        System.out.println("Symbol table:");
        List<Pair<Integer,String>> symbolTable = lexicalAnalyzer.getSymbolTable();
        for (Pair<Integer,String> pair : symbolTable) {
            System.out.println(pair.getValue()+" = "+pair.getKey());
        }
        System.out.println(" ");
        System.out.println("Program internal form:");
        List<Pair<Integer, Integer>> PIF = lexicalAnalyzer.getProgramInternalForm();
        for (Pair<Integer,Integer> pair : PIF) {
            System.out.println(pair.getValue()+" = "+pair.getKey());
        }

    }
}
