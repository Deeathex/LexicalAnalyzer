import javafx.util.Pair;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassLoader classLoader = Main.class.getClassLoader();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(classLoader.getResource("program2.txt").getFile());
        lexicalAnalyzer.analyze();
        System.out.println("Symbol table:");
        List<Pair<Integer,String>> symbolTable = lexicalAnalyzer.getSymbolTable();
        for (Pair<Integer,String> pair : symbolTable) {
            System.out.println(pair+" ");
        }
        System.out.println(" ");
        System.out.println("Program internal form:");
        List<Pair<Integer, Integer>> PIF = lexicalAnalyzer.getProgramInternalForm();
        for (Pair<Integer,Integer> pair : PIF) {
            System.out.println(pair+" ");
        }

    }
}
