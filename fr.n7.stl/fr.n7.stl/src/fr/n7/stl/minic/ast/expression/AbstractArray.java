package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.util.Logger;
import fr.n7.stl.minic.ast.type.ArrayType;

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
		boolean _array = this.array.collectAndPartialResolve(_scope);
		boolean _index = this.index.collectAndPartialResolve(_scope);
		return _array && _index;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean _array = this.array.completeResolve(_scope);
		boolean _index = this.index.completeResolve(_scope);
		return _array && _index;
	}
	
	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
		Type _type = this.array.getType();
		Type _indexType = this.index.getType();
		
		// On vérifie que c'est bien un tableau et que l'index est un entier
		if (_type instanceof ArrayType) {
			if (_indexType.compatibleWith(AtomicType.IntegerType)) {
				return ((ArrayType) _type).getType();
			} else {
				Logger.error("L'index d'un tableau doit être un entier.");
			}
		} else {
			Logger.error("Tentative d'accès indexé sur un élément qui n'est pas un tableau.");
		}
		return AtomicType.ErrorType;
	}

}