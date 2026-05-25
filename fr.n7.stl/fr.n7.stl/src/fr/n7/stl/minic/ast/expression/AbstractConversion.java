/**
 * 
 */
package fr.n7.stl.minic.ast.expression;
import fr.n7.stl.minic.ast.instruction.declaration.TypeDeclaration;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected TargetType target;
	protected Type type;
	protected String name;

	public AbstractConversion(TargetType _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(TargetType _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
	    // Le type résultant est le type cible de la conversion
	    return this.type;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
	    // On propage la collecte à la cible de la conversion
	    return ((Expression)this.target).collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    // 1. Résoudre la cible du cast
	    boolean _result = ((Expression) this.target).completeResolve(_scope);

	    if (_result) {
	        // 2. Si le type est défini par un nom (String), on le cherche dans le scope
	        if (this.type == null && this.name != null) {
	            Declaration _decl = _scope.get(this.name); // Ou _scope.get(this.name).get() si c'est un Optional
	            
	            if (_decl instanceof TypeDeclaration) {
	                // On récupère le vrai type associé au nom
	                this.type = ((TypeDeclaration) _decl).getType();
	            } else {
	                fr.n7.stl.util.Logger.error(this.name + " is not a type declaration.");
	                _result = false;
	            }
	        } 
	    } else {
	        fr.n7.stl.util.Logger.error(this.target + " has not been resolved.");
	    }
	    return _result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
	    // On génère le code pour évaluer l'expression cible
	    // La valeur résultante sera sur la pile
	    return ((Expression)this.target).getCode(_factory);
	}

}
