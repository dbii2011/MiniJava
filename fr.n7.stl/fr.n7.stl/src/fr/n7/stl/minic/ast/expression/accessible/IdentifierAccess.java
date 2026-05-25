/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.expression.AbstractIdentifier;
import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
import fr.n7.stl.minic.ast.type.declaration.LabelDeclaration;

/**
 * Implementation of the Abstract Syntax Tree node for an identifier access expression.
 * @author Marc Pantel
 */
public class IdentifierAccess extends AbstractIdentifier implements AccessibleExpression {

	protected AbstractAccess expression;
	protected LabelDeclaration labelDeclaration = null;

	public IdentifierAccess(String _name) {
		super(_name);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (((HierarchicalScope<Declaration>) _scope).knows(this.name)) {
			Declaration _declaration = _scope.get(this.name);
			if (_declaration instanceof VariableDeclaration) {
				this.expression = new VariableAccess((VariableDeclaration) _declaration);
			} else if (_declaration instanceof ConstantDeclaration) {
				this.expression = new ConstantAccess((ConstantDeclaration) _declaration);
			} else if (_declaration instanceof ParameterDeclaration) {
				this.expression = new ParameterAccess((ParameterDeclaration) _declaration);
			} else if (_declaration instanceof LabelDeclaration) {
				this.labelDeclaration = (LabelDeclaration) _declaration;
			}
		}
		return true;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (this.expression == null && this.labelDeclaration == null) {
			if (((HierarchicalScope<Declaration>) _scope).knows(this.name)) {
				Declaration _declaration = _scope.get(this.name);
				if (_declaration instanceof VariableDeclaration) {
					this.expression = new VariableAccess((VariableDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof ConstantDeclaration) {
					this.expression = new ConstantAccess((ConstantDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof ParameterDeclaration) {
					this.expression = new ParameterAccess((ParameterDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof LabelDeclaration) {
					this.labelDeclaration = (LabelDeclaration) _declaration;
					return true;
				} else {
					Logger.error("The declaration for " + this.name + " is of the wrong kind.");
					return false;
				}
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;
			}
		}
		return true;
	}

	@Override
	public Type getType() {
		if (this.labelDeclaration != null) {
			return this.labelDeclaration.getType();
		}
		return this.expression.getType();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.labelDeclaration != null) {
			Fragment frag = _factory.createFragment();
			frag.add(_factory.createLoadL(this.labelDeclaration.getIndex()));
			return frag;
		}
		return this.expression.getCode(_factory);
	}

}