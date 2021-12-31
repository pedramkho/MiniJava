package codegenerator;

public class Address {
    private int num;
    private TypeAddress type;
    private VarType varType;

    public Address(int num, VarType varType, TypeAddress type) {
        this.num = num;
        this.type = type;
        this.varType = varType;
    }

    public Address(int num, VarType varType) {
        this.num = num;
        this.type = TypeAddress.DIRECT;
        this.varType = varType;
    }
    public String toString(){
        switch (type){
            case DIRECT:
                return num+"";
            case INDIRECT:
                return "@"+num;
            case IMMEDIATE:
                return "#"+num;
        }
        return num+"";
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public TypeAddress getType() {
        return type;
    }

    public void setType(TypeAddress type) {
        this.type = type;
    }

    public VarType getVarType() {
        return varType;
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }
}
