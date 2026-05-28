package fr.n7.stl.minijava.instruction;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisCall implements Instruction {
	
	protected ConstructorDeclaration constructor;
	
	protected List<AccessibleExpression> arguments;

	public ThisCall(List<AccessibleExpression> _arguments) {
		this.arguments = _arguments;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return true;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		for (AccessibleExpression arg : this.arguments) {
			ok = ok && arg.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;

		// 1. SI le constructeur a été trouvé (Vérification stricte façon MethodCall)
		if (this.constructor != null) {
			// Attention : il faut que ConstructorDeclaration ait un public List<ParameterDeclaration> getParameters()
			List<ParameterDeclaration> params = this.constructor.getParameters(); 
			
			if (this.arguments.size() != params.size()) {
				System.err.println("Erreur : Nombre d'arguments incorrect pour l'appel this(...)");
				return false;
			}

			for (int i = 0; i < this.arguments.size(); i++) {
				Type argType = this.arguments.get(i).getType();
				if (argType == null) return false;
				
				Type paramType = params.get(i).getType();
				if (!argType.compatibleWith(paramType)) {
					System.err.println("Erreur : Argument " + i + " incompatible dans this(...)");
					return false;
				}
			}
			return true;
		}

    // 2. SINON (Filet de sécurité basique pour ne pas crasher)
    for (AccessibleExpression arg : this.arguments) {
        ok = ok && (arg.getType() != null);
    }
    return ok;
}
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// On empile l'objet en cours de construction (this)
		frag.add(_factory.createLoad(Register.LB, 3, 1));
		// On empile les arguments pour le constructeur délégué
		for (AccessibleExpression arg : this.arguments) {
			frag.append(arg.getCode(_factory));
		}
		// On appelle le constructeur ciblé (Le nommage dépend de la Partie 2 de ton équipe)
		// frag.add(_factory.createCall("constructor_...", Register.LB));
		return frag;
	}
	
	@Override
	public String toString() {
		String image = "";
		image += "this( ";
		Iterator<AccessibleExpression> iterator = this.arguments.iterator();
		if (iterator.hasNext()) {
			AccessibleExpression argument = iterator.next();
			image += argument;
			while (iterator.hasNext()) {
				 argument = iterator.next();
				 image += " ," + argument;
			}
		}
		image += ");\n";
		return image;
	}

}
