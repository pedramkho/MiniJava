package semantic.symbol;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Symbol{
    private SymbolType type;
    private int address;
    public Symbol(SymbolType type , int address)
    {
        this.type = type;
        this.address = address;
    }

    public SymbolType getType() {
        return type;
    }

    public int getAddress() {
        return address;
    }
}
