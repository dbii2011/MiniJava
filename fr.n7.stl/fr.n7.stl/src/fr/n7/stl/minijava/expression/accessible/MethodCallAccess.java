package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.minijava.expression.AbstractMethodCall;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.List;

public class MethodCallAccess extends AbstractMethodCall<AccessibleExpression> implements AccessibleExpression {

	public MethodCallAccess(AccessibleExpression _target, String _name, List<AccessibleExpression> _arguments) {
		super(_target,_name,_arguments);
	}
	
	public MethodCallAccess(String _name, List<AccessibleExpression> _arguments) {
		super(_name,_arguments);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.target != null) {
			ok = ok && this.target.completeResolve(_scope);
		}
		for (AccessibleExpression arg : this.arguments) {
			ok = ok && arg.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public Type getType() {
		Type targetType = this.target.getType();
		if (targetType instanceof ClassType) {
			ClassDeclaration classDecl = ((ClassType)targetType).declaration;
			this.declaration = classDecl.getMethod(this.name);
			if (this.declaration != null) {
				return this.declaration.getType();
			}
		}
		return null; // Erreur de typage
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// 1. Paramètre caché 'this'
		if (this.target != null) {
			frag.append(this.target.getCode(_factory));
		}
		// 2. Paramètres normaux
		for (AccessibleExpression arg : this.arguments) {
			frag.append(arg.getCode(_factory));
		}
		// 3. Appel de fonction
		frag.add(_factory.createCall("method_" + this.declaration.className + "_" + this.name, Register.LB));
		return frag;
	}
	
}
