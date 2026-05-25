package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractPointer<PointerKind extends Expression> implements Expression {

	/**
	 * AST node that represents an expression whose value is a pointer.
	 */
	protected PointerKind pointer;
	
	/**
	 * Construction for the implementation of a pointer content access expression Abstract Syntax Tree node.
	 * @param _pointer Abstract Syntax Tree for the pointer expression in a pointer content access expression.
	 */
	public AbstractPointer(PointerKind _pointer) {
		this.pointer = _pointer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(*" + this.pointer + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
	    // On propage l'analyse à l'expression du pointeur (ex: le 'p' dans '*p')
	    return this.pointer.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    // On résout l'expression du pointeur
	    return this.pointer.completeResolve(_scope);
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	@Override
	public Type getType() {
	    Type _typePointer = this.pointer.getType();
	    
	    // On vérifie que c'est bien un type pointeur avant de déréférencer
	    if (_typePointer instanceof fr.n7.stl.minic.ast.type.PointerType) {
	        return ((fr.n7.stl.minic.ast.type.PointerType) _typePointer).getPointedType();
	    } else {
	        fr.n7.stl.util.Logger.error(this.pointer + " n'est pas un pointeur, déréférencement impossible.");
	        return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
	    }
	}

}