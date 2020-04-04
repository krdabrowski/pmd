/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.symbols.table.nimpl;

import net.sourceforge.pmd.lang.java.symbols.JMethodSymbol;
import net.sourceforge.pmd.lang.java.symbols.JTypeDeclSymbol;
import net.sourceforge.pmd.lang.java.symbols.JVariableSymbol;
import net.sourceforge.pmd.lang.java.symbols.table.JSymbolTable;

final class NSymbolTableImpl implements JSymbolTable {

    static JSymbolTable EMPTY = new NSymbolTableImpl(RootShadowGroup.empty(), RootShadowGroup.empty(), RootShadowGroup.empty());

    private final ShadowGroup<JVariableSymbol> vars;
    private final ShadowGroup<JTypeDeclSymbol> types;
    private final ShadowGroup<JMethodSymbol> methods;

    NSymbolTableImpl(ShadowGroup<JVariableSymbol> vars,
                     ShadowGroup<JTypeDeclSymbol> types,
                     ShadowGroup<JMethodSymbol> methods) {
        this.vars = vars;
        this.types = types;
        this.methods = methods;
    }

    @Override
    public ShadowGroup<JVariableSymbol> variables() {
        return vars;
    }

    @Override
    public ShadowGroup<JTypeDeclSymbol> types() {
        return types;
    }

    @Override
    public ShadowGroup<JMethodSymbol> methods() {
        return methods;
    }

    @Override
    public String toString() {
        return "NSymTableImpl{" +
            "vars=" + vars +
            ", types=" + types +
            ", methods=" + methods +
            '}';
    }

    static JSymbolTable withVars(JSymbolTable parent, ShadowGroup<JVariableSymbol> vars) {
        return new NSymbolTableImpl(vars, parent.types(), parent.methods());
    }

    static JSymbolTable withTypes(JSymbolTable parent, ShadowGroup<JTypeDeclSymbol> types) {
        return new NSymbolTableImpl(parent.variables(), types, parent.methods());
    }
}
