/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.NamedType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for an array type.
 * @author Marc Pantel
 *
 */
public class Assignment implements Instruction, Expression {

	protected Expression value;
	protected AssignableExpression assignable;

	/**
	 * Create an assignment instruction implementation from the assignable expression
	 * and the assigned value.
	 * @param _assignable Expression that can be assigned a value.
	 * @param _value Value assigned to the expression.
	 */
	public Assignment(AssignableExpression _assignable, Expression _value) {
		this.assignable = _assignable;
		this.value = _value;
		/* This attribute will be assigned to the appropriate value by the resolve action */
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assignable + " = " + this.value.toString() + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean res1 = this.assignable.collectAndPartialResolve(_scope);
		boolean res2 = this.value.collectAndPartialResolve(_scope);
		return res1 && res2;
	}
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		boolean res1 = this.assignable.collectAndPartialResolve(_scope);
		boolean res2 = this.value.collectAndPartialResolve(_scope);
		return res1 && res2;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean res1 = this.assignable.completeResolve(_scope);
		boolean res2 = this.value.completeResolve(_scope);
		return res1 && res2;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.value.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Type targetType = this.assignable.getType();
        Type valueType = this.value.getType();

        if (targetType instanceof NamedType) {
            targetType = ((NamedType) targetType).getType();
        }
        if (valueType instanceof NamedType) {
            valueType = ((NamedType) valueType).getType();
        }

        if (valueType.compatibleWith(targetType)) {
            return true;
        } else {
            Logger.error("Type mismatch in assignment.");
            return false;
        }
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
	
		// Empiler la valeur (côté droit)
    	frag.append(this.value.getCode(_factory));

		// Empiler l'adresse de destination (côté gauche)
    	frag.append(this.assignable.getCode(_factory));
    	int size = this.assignable.getType().length();
    	frag.add(_factory.createStoreI(size));
		return frag;
	}

}
