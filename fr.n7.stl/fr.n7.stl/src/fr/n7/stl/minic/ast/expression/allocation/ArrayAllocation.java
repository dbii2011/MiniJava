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
import fr.n7.stl.minic.ast.type.ArrayType;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.expression.accessible.BinaryOperator;
import fr.n7.stl.tam.ast.Library;
/**
 * @author Marc Pantel
 *
 */
public class ArrayAllocation implements AccessibleExpression, AssignableExpression {

	protected Type element;
	protected Expression size;

	public ArrayAllocation(Type _element, Expression _size) {
		this.element = _element;
		this.size = _size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "new " + this.element + "[ " + this.size + " ]"; 
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return this.size.collectAndPartialResolve(_scope);
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.size.completeResolve(_scope);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
    public Type getType() {
        if (this.size.getType().compatibleWith(AtomicType.IntegerType)) {
            return new ArrayType(this.element);
        } else {
            return AtomicType.ErrorType;
        }
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // 1. On évalue la taille demandée (met le nombre d'éléments sur la pile)
        _result.append(this.size.getCode(_factory));
        
        // 2. On multiplie par la taille d'un élément (en mots)
        _result.add(_factory.createLoadL(this.element.length()));
        
        // Utilisation de la méthode statique de l'interface TAMFactory pour l'opérateur
        _result.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
        
        // 3. On ajoute l'instruction MAlloc directement
        // Puisque Library.MAlloc est une TAMInstruction utilisable
        _result.add(fr.n7.stl.tam.ast.Library.MAlloc);
        
        return _result;
    }

 

}
