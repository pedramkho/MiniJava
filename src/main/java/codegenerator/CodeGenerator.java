package codegenerator;

import log.Log;
import errorhandling.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.Stack;


public class CodeGenerator {
    private final Memory memory = new Memory();
    private final Stack<Address> ss = new Stack<Address>();
    private final Stack<String> symbolStack = new Stack<>();
    private final Stack<String> callStack = new Stack<>();
    private final SymbolTable symbolTable;

    public CodeGenerator() {
        symbolTable = new SymbolTable(memory);
        //TODO
    }
    public void printMemory()
    {
        memory.pintCodeBlock();
    }
    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                add();
                break;
            case 11:
                sub();
                break;
            case 12:
                mult();
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                localWhile();
                break;
            case 16:
                jpfSave();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                lessThan();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                popClass();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                lastTypeBool();
                break;
            case 32:
                lastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    private VarType getVarType(SymbolType s) {
        return switch (s) {
            case BOOL -> VarType.BOOL;
            case INT -> VarType.INT;
        };
    }
    private void defMain() {
        memory.addAddressCodeTrio(ss.pop().getNum(), Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void checkID() {
        symbolStack.pop();
        if (ss.peek().getVarType() == VarType.NON) {
            //TODO : error
        }
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {

                Symbol s = symbolTable.get(className, methodName, next.getValue());
                VarType t = getVarType(s.getType());
                ss.push(new Address(s.getAddress(), t));


            } catch (Exception e) {
                ss.push(new Address(0, VarType.NON));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, VarType.NON));
        }
        symbolStack.push(next.getValue());
    }

    public void fpid() {
        ss.pop();
        ss.pop();

        Symbol s = symbolTable.get(symbolStack.pop(), symbolStack.pop());
        VarType t = getVarType(s.getType());
        ss.push(new Address(s.getAddress(), t));

    }

    public void kpid(Token next) {
        ss.push(symbolTable.get(next.getValue()));
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.getValue()), VarType.INT, TypeAddress.IMMEDIATE));
    }

    public void startCall() {
        //TODO: method ok
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTable.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);

    }

    public void call() {
        //TODO: method ok
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolTable.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException ignored) {}
            VarType t = VarType.INT;
            switch (symbolTable.getMethodReturnType(className, methodName))
            {
                case INT:
                    t = VarType.INT;
                    break;
                case BOOL:
                    t = VarType.BOOL;
                    break;
            }
            Address temp = new Address(memory.getTemp(),t);
            ss.push(temp);
            memory.addAddressCodeTrio(Operation.ASSIGN, new Address(temp.getNum(), VarType.ADDRESS, TypeAddress.IMMEDIATE), new Address(symbolTable.getMethodReturnAddress(className, methodName), VarType.ADDRESS), null);
            memory.addAddressCodeTrio(Operation.ASSIGN, new Address(memory.getCurrentCodeBlockAddress() + 2, VarType.ADDRESS, TypeAddress.IMMEDIATE), new Address(symbolTable.getMethodCallerAddress(className, methodName), VarType.ADDRESS), null);
            memory.addAddressCodeTrio(Operation.JP, new Address(symbolTable.getMethodAddress(className, methodName), VarType.ADDRESS), null, null);



    }

    public void arg() {
        //TODO: method ok

        String methodName = callStack.pop();
        try {
            Symbol s = symbolTable.getNextParam(callStack.peek(), methodName);
            VarType t = getVarType(s.getType());

            Address param = ss.pop();
            if (param.getVarType() != t) {
                ErrorHandler.printError("The argument type isn't match");
            }
            memory.addAddressCodeTrio(Operation.ASSIGN, param, new Address(s.getAddress(), t), null);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);

    }

    public void assign() {

            Address s1 = ss.pop();
            Address s2 = ss.pop();
            if (s1.getVarType() != s2.getVarType()) {
                ErrorHandler.printError("The type of operands in assign is different ");
            }
            memory.addAddressCodeTrio(Operation.ASSIGN, s1, s2, null);

    }

    public void add() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();

        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        memory.addAddressCodeTrio(Operation.ADD, s1, s2, temp);
        ss.push(temp);
    }

    public void sub() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        memory.addAddressCodeTrio(Operation.SUB, s1, s2, temp);
        ss.push(temp);
    }

    public void mult() {
        Address temp = new Address(memory.getTemp(), VarType.INT);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        memory.addAddressCodeTrio(Operation.MULT, s1, s2, temp);
        ss.push(temp);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS));
    }

    public void save() {
        ss.push(new Address(memory.saveMemory(), VarType.ADDRESS));
    }

    public void localWhile() {
        memory.addAddressCodeTrio(ss.pop().getNum(), Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress() + 1, VarType.ADDRESS), null);
        memory.addAddressCodeTrio(Operation.JP, ss.pop(), null, null);
    }

    public void jpfSave() {
        Address save = new Address(memory.saveMemory(), VarType.ADDRESS);
        memory.addAddressCodeTrio(ss.pop().getNum(), Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null);
        ss.push(save);
    }

    public void jpHere() {
        memory.addAddressCodeTrio(ss.pop().getNum(), Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null, null);
    }

    public void print() {
        memory.addAddressCodeTrio(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != s2.getVarType()) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        memory.addAddressCodeTrio(Operation.EQ, s1, s2, temp);
        ss.push(temp);
    }

    public void lessThan() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        memory.addAddressCodeTrio(Operation.LT, s1, s2, temp);
        ss.push(temp);
    }

    public void and() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != VarType.BOOL || s2.getVarType() != VarType.BOOL) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        memory.addAddressCodeTrio(Operation.AND, s1, s2, temp);
        ss.push(temp);

    }

    public void not() {
        Address temp = new Address(memory.getTemp(), VarType.BOOL);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.getVarType() != VarType.BOOL) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        memory.addAddressCodeTrio(Operation.NOT, s1, s2, temp);
        ss.push(temp);

    }

    public void defClass() {
        ss.pop();
        symbolTable.addClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);

    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        symbolTable.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        ss.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        //TODO : call ok

        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolTable.getMethodReturnType(symbolStack.peek(), methodName);
        VarType temp = getVarType(t);
        if (s.getVarType() != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memory.addAddressCodeTrio(Operation.ASSIGN, s, new Address(symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName), VarType.ADDRESS, TypeAddress.INDIRECT), null);
        memory.addAddressCodeTrio(Operation.JP, new Address(symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName), VarType.ADDRESS), null, null);

    }

    public void defParam() {
        //TODO : call Ok
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolTable.setLastType(SymbolType.BOOL);
    }

    public void lastTypeInt() {
        symbolTable.setLastType(SymbolType.INT);
    }

}
