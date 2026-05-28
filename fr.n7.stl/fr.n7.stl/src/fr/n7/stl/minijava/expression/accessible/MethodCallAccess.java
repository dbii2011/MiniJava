package fr.n7.stl.minijava.expression.accessible;

import java.util.List;

import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassElement;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.minijava.expression.AbstractMethodCall;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

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
			for(ClassElement e : classDecl.getElements()) {
				if (e instanceof MethodDeclaration && e.getName().equals(this.name)) {
					this.declaration = (MethodDeclaration) e;
					return this.declaration.getType(); // Retourne le type de la méthode !
				}
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
		frag.add(_factory.createCall("method_" + this.name, Register.LB));
		return frag;
	}
	
}
