/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
 */
public class FunctionCall implements AccessibleExpression {

	protected String name;
	protected FunctionDeclaration function;
	protected List<AccessibleExpression> arguments;

	public FunctionCall(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

	@Override
	public String toString() {
		String _result = this.name + "( ";
		Iterator<AccessibleExpression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return _result + ")";
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// On collecte les arguments
		boolean ok = true;
		for (AccessibleExpression arg : this.arguments) {
			ok &= arg.collectAndPartialResolve(_scope);
		}
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// On résout le nom de la fonction
		if (_scope.knows(this.name)) {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof FunctionDeclaration) {
				this.function = (FunctionDeclaration) decl;
			} else {
				Logger.error("Identifier " + this.name + " is not a function.");
				return false;
			}
		} else {
			Logger.error("Function " + this.name + " is not declared.");
			return false;
		}
		// On résout les arguments
		boolean ok = true;
		for (AccessibleExpression arg : this.arguments) {
			ok &= arg.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public Type getType() {
		if (this.function == null) {
			throw new SemanticsUndefinedException("Function " + this.name + " has not been resolved.");
		}
		return this.function.getType();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.function == null) {
			throw new SemanticsUndefinedException("Function " + this.name + " has not been resolved.");
		}
		Fragment frag = _factory.createFragment();
		// On empile les arguments dans l'ordre
		for (AccessibleExpression arg : this.arguments) {
			frag.append(arg.getCode(_factory));
		}
		// On appelle la fonction via son label
		frag.add(_factory.createCall(this.function.getLabel(), Register.LB));
		return frag;
	}

}