package codegenerator;

import java.util.ArrayList;
import java.util.List;


public class Memory {
    private List<AddressCodeTrio> codeBlock;
    private int lastTempIndex;
    private int lastDataAddress;
    private static final int startTempMemoryAddress = 500;
    private static final int startDataMemoryAddress = 200;
    private static final int dataSize = 4;
    private static final int tempSize = 4;

    public Memory() {
        codeBlock = new ArrayList<AddressCodeTrio>();
        lastTempIndex = startTempMemoryAddress;
        lastDataAddress = startDataMemoryAddress;
    }

    public int getTemp() {
        lastTempIndex += tempSize;
        return lastTempIndex - tempSize;
    }
    public  int getDateAddress(){
        lastDataAddress += dataSize;
        return lastDataAddress-dataSize;
    }
    public int saveMemory() {
        codeBlock.add(new AddressCodeTrio());
        return codeBlock.size() - 1;
    }

    public void add3AddressCode(Operation op, Address opr1, Address opr2, Address opr3) {
        codeBlock.add(new AddressCodeTrio(op,opr1,opr2,opr3));
    }

    public void add3AddressCode(int i, Operation op, Address opr1, Address opr2, Address opr3) {
        codeBlock.remove(i);
        codeBlock.add(i, new AddressCodeTrio(op, opr1, opr2,opr3));
    }


    public int getCurrentCodeBlockAddress() {
        return codeBlock.size();
    }

    public void pintCodeBlock() {
        System.out.println("Code Block");
        for (int i = 0; i < codeBlock.size(); i++) {
            System.out.println(i + " : " + codeBlock.get(i).toString());
        }
    }
}

class AddressCodeTrio {
    private Operation operation;
    private Address operand1;
    private Address operand2;
    public Address operand3;

    public AddressCodeTrio() {

    }

    public AddressCodeTrio(Operation op, Address opr1, Address opr2, Address opr3) {
        operation = op;
        operand1 = opr1;
        operand2 = opr2;
        operand3 = opr3;
    }

    public String toString()
    {
        if(operation == null) {return "";}
        StringBuffer res = new StringBuffer("(");
        res.append(operation.toString()).append(",");
        if(operand1 != null) {
            res.append(operand1.toString());}
        res.append(",");
        if(operand2 != null) {
            res.append(operand2.toString());}
        res.append(",");
        if(operand3 != null) {
            res.append(operand3.toString());}
        res.append(")");

        return res.toString();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Address getOperand1() {
        return operand1;
    }

    public void setOperand1(Address operand1) {
        this.operand1 = operand1;
    }

    public Address getOperand2() {
        return operand2;
    }

    public void setOperand2(Address operand2) {
        this.operand2 = operand2;
    }
}
