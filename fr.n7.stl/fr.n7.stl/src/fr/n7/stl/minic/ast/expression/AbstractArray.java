package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractArray<ArrayKind extends Expression> implements Expression {

	/**
	 * AST node that represents the expression whose result is an array.
	 */
	protected ArrayKind array;
	
	/**
	 * AST node that represents the expression whose result is an integer value used to index the array.
	 */
	protected AccessibleExpression index;
	
	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public AbstractArray(ArrayKind _array, AccessibleExpression _index) {
		this.array = _array;
		this.index = _index;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.array + "[ " + this.index + " ]");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
	    boolean _res = this.array.collectAndPartialResolve(_scope);
	    _res &= this.index.collectAndPartialResolve(_scope);
	    return _res;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    boolean _res = this.array.completeResolve(_scope);
	    _res &= this.index.completeResolve(_scope);
	    return _res;
	}
	
	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	@Override
	public Type getType() {
	    Type _typeTableau = this.array.getType();
	    // On vérifie que c'est bien un type "Tableau" avant d'extraire le type des éléments
	    if (_typeTableau instanceof fr.n7.stl.minic.ast.type.ArrayType) {
	        return ((fr.n7.stl.minic.ast.type.ArrayType) _typeTableau).getType();
	    } else {
	        // Si on essaie d'indexer quelque chose qui n'est pas un tableau
	        fr.n7.stl.util.Logger.error(this.array + " n'est pas un tableau.");
	        return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
	    }
	}

}