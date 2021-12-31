package codegenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
    private int num;
    private TypeAddress type;
    private VarType varType;

    public Address(int num, VarType varType, TypeAddress Type) {
        this.num = num;
        this.type = Type;
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
            case IMIDIATE:
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
