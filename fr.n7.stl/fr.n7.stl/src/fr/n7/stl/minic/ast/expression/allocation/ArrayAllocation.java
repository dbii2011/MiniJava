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
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.ArrayType;

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
		// La taille est une expression, on la résout
		return this.size.collectAndPartialResolve(_scope);
	}

	
	public boolean checkType() {
		// La taille du tableau doit être un entier
		return this.size.getType().compatibleWith(AtomicType.IntegerType);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// On résout le type des éléments et l'expression de la taille
		boolean _element = this.element.completeResolve(_scope);
		boolean _size = this.size.completeResolve(_scope);
		return _element && _size;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		// Le type retourné par "new int[3]" est "int[]" (ArrayType de int)
		return new ArrayType(this.element);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		frag.append(this.size.getCode(_factory));
		
		
		int elementSize = this.element.length();
		if (elementSize > 1) {
			frag.add(_factory.createLoadL(elementSize));
			frag.add(Library.IMul);
		}
		
		
		frag.add(Library.MAlloc);
		return frag;
	}

}
