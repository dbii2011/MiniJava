package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.PointerType;
import fr.n7.stl.minic.ast.type.AtomicType;
=======
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

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
<<<<<<< HEAD
		// On transmet la collecte à l'expression du pointeur
		return this.pointer.collectAndPartialResolve(_scope);		
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// On transmet la résolution à l'expression du pointeur
		return this.pointer.completeResolve(_scope);	
=======
	    // On propage l'analyse à l'expression du pointeur (ex: le 'p' dans '*p')
	    return this.pointer.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    // On résout l'expression du pointeur
	    return this.pointer.completeResolve(_scope);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
<<<<<<< HEAD
	public Type getType() {
		// si on fait *ptr le type obtenu est le type pointé par ptr
		Type pointerType = this.pointer.getType();
		if (pointerType instanceof PointerType) {
			return ((PointerType) pointerType).getPointedType();
		} else {
			return AtomicType.ErrorType;
		}
	}


	public PointerKind getPointer() {
		return this.pointer;
=======
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
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

}