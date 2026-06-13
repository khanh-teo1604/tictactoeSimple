package vgu.pe2026.ttt.basic.Constant;

import java.util.ArrayList;
import java.util.List;

public enum PlayerType {
    HUMAN(1),
    COMPUTER(2);

    private final int symbol;

    PlayerType(int symbol) {
        this.symbol = symbol;
    }

    public int getplayerTypeSymbol() {
        return symbol;
    }

    public static List<String> getValidArguments() {
        List<String> allValidArguments = new ArrayList<>();
        for (int i = 1; i <= PlayerType.values().length; i++) {
            allValidArguments.add(String.valueOf(i));
        }
        return allValidArguments;
    }
}
