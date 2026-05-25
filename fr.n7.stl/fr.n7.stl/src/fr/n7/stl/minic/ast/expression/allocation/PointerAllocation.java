/**
 * 
 */
package fr.n7.stl.minic.ast.expression.allocation;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.minic.ast.type.PointerType;

/**
 * @author Marc Pantel
 *
 */
public class PointerAllocation implements AccessibleExpression, AssignableExpression {

	protected Type element;

	public PointerAllocation(Type _element) {
		this.element = _element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "new " + this.element; 
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        // Rien à résoudre ici, le type est déjà connu à la construction
        return true;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        // Le type element a peut-être besoin d'être résolu (ex: struct)
        return true;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
        // L'allocation retourne un pointeur vers l'élément
        return new PointerType(this.element);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // 1. On met la taille nécessaire (en mots) sur la pile
        _result.add(_factory.createLoadL(this.element.length()));
        
        // 2. On appelle l'allocation dynamique
        _result.add(Library.MAlloc);
        
        return _result;
    }

}
