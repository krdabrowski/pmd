/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.symbols.table;

import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.Nullable;

import net.sourceforge.pmd.annotation.Experimental;
import net.sourceforge.pmd.lang.java.symbols.JElementSymbol;
import net.sourceforge.pmd.lang.java.symbols.JMethodSymbol;
import net.sourceforge.pmd.lang.java.symbols.JTypeDeclSymbol;
import net.sourceforge.pmd.lang.java.symbols.JVariableSymbol;

// @formatter:off
/**
 * A symbol table for a particular region of a Java program. Keeps track of the types,
 * values, and methods accessible from their simple name in their extent.
 *
 * <p>Each symbol table is linked to a parent table, and keeps track of a particular set
 * of declarations having the same relative precedence. When a symbol table is asked for
 * the meaning of a name in a particular syntactic context (type name, method name, value name),
 * it first determines if it tracks a declaration with a matching name.
 * <ul>
 *      <li>If there is one, it returns the {@link JElementSymbol} representing the entity
 *          the name stands for in the given context;
 *      <li>If there is none, it asks the same question to its parent table recursively
 *          and returns that result.
 * </ul>
 * This allows directly encoding shadowing and hiding mechanisms in the parent-child
 * relationships.
 *
 * @since 7.0.0
 */
// @formatter:on
@Experimental
public interface JSymbolTable {

    /**
     * Returns the parent of this table, that is, the symbol table that will be
     * delegated to if this table doesn't find a declaration.
     *
     * @return a symbol table, or null if this is the top-level symbol table
     */
    JSymbolTable getParent();

    // note that types and value names can be obscured, but that depends on the syntactic
    // context of the *usage* and is not relevant to the symbol table stack.


    /**
     * Resolves the type referred to by the given name. This must be a
     * simple name, ie, parameterized types and array types are not
     * available. Primitive types are also not considered because it's
     * not useful.
     *
     * @param simpleName Simple name of the type to look for
     *
     * @return A result for the search, null if the search failed
     */
    @Nullable
    ResolveResult<JTypeDeclSymbol> resolveTypeName(String simpleName);


    /**
     * Finds the variable to which the given simple name refers
     * in the scope of this symbol table. Returns null if the symbol
     * cannot be resolved.
     *
     * @param simpleName simple name of the value to find
     *
     * @return A result for the search, null if the search failed
     */
    @Nullable
    ResolveResult<JVariableSymbol> resolveValueName(String simpleName);


    /**
     * Finds all accessible methods that can be called with the given simple name
     * on an implicit receiver in the scope of this symbol table. The returned methods may
     * have different arity and parameter types.
     *
     * <p>Possibly, looking up a method may involve exploring all the
     * supertypes of the implicit receiver (and the enclosing classes
     * and their supertypes, for inner/anonymous classes). Since this
     * might be costly, the method returns a lazy stream that should be
     * filtered down to exactly what you want.
     *
     * @param simpleName Simple name of the method
     *
     * @return A stream yielding all methods with the given name accessible and applicable to the
     *     implicit receiver in the scope of this symbol table.
     */
    Stream<JMethodSymbol> resolveMethodName(String simpleName);

}
