package parser;

import scanner.token.Token;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;


public class Rule {
    public Rule(String stringRule) {
        int index = stringRule.indexOf("#");
        if (index != -1) {
            try {
            semanticAction = Integer.parseInt(stringRule.substring(index + 1));
            }catch (NumberFormatException ex){
                semanticAction = 0;
            }
            stringRule = stringRule.substring(0, index);
        } else {
            semanticAction = 0;
        }
        String[] splited = stringRule.split("->");
            LHS = NonTerminal.valueOf(splited[0]);
        RHS = new ArrayList<GrammarSymbol>();
        if (splited.length > 1) {
            String[] RHSs = splited[1].split(" ");
            for (String s : RHSs){
                try {
                    RHS.add(new GrammarSymbol(NonTerminal.valueOf(s)));
                } catch (Exception e) {
                        RHS.add(new GrammarSymbol(new Token(Token.getTyepFormString(s), s)));
                }
            }
        }
    }
    public NonTerminal LHS;
    public List<GrammarSymbol> RHS;
    public int semanticAction;
}

class GrammarSymbol{
    public boolean isTerminal;
    public NonTerminal nonTerminal;
    public Token terminal;
    public GrammarSymbol(NonTerminal nonTerminal)
    {
        this.nonTerminal = nonTerminal;
        isTerminal = false;
    }
    public GrammarSymbol(Token terminal)
    {
        this.terminal = terminal;
        isTerminal = true;
    }
}