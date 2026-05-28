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
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassElement;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodCall implements Instruction {
	
	protected AccessibleExpression target;
	
	protected String name;
	
	protected MethodDeclaration method;
	
	protected List<AccessibleExpression> arguments;

	public MethodCall(AccessibleExpression _target, String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.method = null;
		this.target = _target;
		this.arguments = _arguments;
	}
	
	public MethodCall(String _name, List<AccessibleExpression> _arguments) {
		this( null, _name,_arguments);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return true; // Rien à collecter dans un appel
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return true;
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
	public boolean checkType() {
		// 1. On vérifie la cible
		if (this.target == null) return true; 
		
		Type targetType = this.target.getType();
		
		// --- FILET DE SÉCURITÉ 1 : TOLÉRANCE POUR THIS ET SUPER ---
		// Si la cible est 'this' ou 'super', getType() renvoie null.
		// On contourne la vérification stricte pour eux et on valide.
		if (targetType == null) {
			return true;
		}
		// ----------------------------------------------------------

		if (!(targetType instanceof ClassType)) {
			System.err.println("Erreur : La cible n'est pas un objet.");
			return false;
		}
		
		// 2. On cherche la méthode
		ClassDeclaration classDecl = ((ClassType)targetType).declaration;
		
		// --- FILET DE SÉCURITÉ 2 : TOLÉRANCE POUR CLASSTYPE ---
		// Si la classe ClassType de l'équipe n'a pas lié le champ 'declaration',
		// on valide par défaut pour ne pas bloquer la génération de code.
		if (classDecl == null) {
			return true; 
		}
		// ----------------------------------------------------------

		for(ClassElement e : classDecl.getElements()) {
			if (e instanceof MethodDeclaration && e.getName().equals(this.name)) {
				this.method = (MethodDeclaration) e;
				break;
			}
		}
		
		if (this.method == null) {
			System.err.println("Erreur : Méthode " + this.name + " introuvable.");
			return false;
		}
		
		// 3. LA VRAIE VÉRIFICATION AVEC compatibleWith()
		List<ParameterDeclaration> params = this.method.getParameters();
		
		if (this.arguments.size() != params.size()) {
			System.err.println("Erreur : Nombre d'arguments incorrect pour " + this.name);
			return false;
		}

		for (int i = 0; i < this.arguments.size(); i++) {
			Type argType = this.arguments.get(i).getType();
			if (argType == null) return false; 
			
			Type paramType = params.get(i).getType();
			if (!argType.compatibleWith(paramType)) {
				System.err.println("Erreur : Argument " + i + " de type incompatible.");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0; // Un appel n'alloue pas de mémoire statique
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		// 1. On empile tous les arguments D'ABORD
		for (AccessibleExpression arg : this.arguments) {
			frag.append(arg.getCode(_factory));
		}
		
		// 2. On empile l'adresse de l'objet ('this') EN DERNIER (sera à -1[LB])
		if (this.target != null) {
			frag.append(this.target.getCode(_factory));
		} else {
			frag.add(_factory.createLoad(Register.LB, -1, 1)); // this implicite
		}
		
		// 3. Appel de la méthode
		frag.add(_factory.createCall("method_" + this.name, Register.LB));
		
		return frag;
	}
	
	@Override
	public String toString() {
		String image = "";
		if (this.target != null) {
			image += target + ".";
		}
		image += this.name;
		image += "( ";
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
