package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;
import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodDeclaration extends ClassElement {
	
	protected Type returnType;
	protected List<ParameterDeclaration> parameters;
	protected Block body;
	protected SymbolTable methodScope;
	protected int paramsSize;
	protected boolean isAbstract; // Pour gérer exitMethodeAbstraite

	// Constructeur pour les méthodes avec corps (exitMethodeObjet / exitMethodeClasse)
	public MethodDeclaration(String _name, Type _returnType, List<ParameterDeclaration> _parameters, Block _body) {
		super(ElementKind.METHOD, AccessRight.PUBLIC, _name); // Public par défaut si non précisé
		this.returnType = _returnType;
		this.parameters = _parameters;
		this.body = _body;
		this.isAbstract = false;
		this.paramsSize = 0;
	}

	// Constructeur pour les méthodes abstraites (sans corps) (exitMethodeAbstraite)
	public MethodDeclaration(String _name, Type _returnType, List<ParameterDeclaration> _parameters) {
		super(ElementKind.METHOD, AccessRight.PUBLIC, _name);
		this.returnType = _returnType;
		this.parameters = _parameters;
		this.body = null;
		this.isAbstract = true;
		this.paramsSize = 0;
	}

	@Override
	public Type getType() {
		return this.returnType;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.accepts(this)) {
			System.err.println("Erreur : La méthode " + this.name + " est déjà définie.");
			return false;
		}
		_scope.register(this);

		this.methodScope = new SymbolTable(_scope);
		boolean ok = true;
		
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.collectAndPartialResolve(this.methodScope);
		}
		
		if (!this.isAbstract && this.body != null) {
			ok = ok && this.body.collectAndPartialResolve(this.methodScope);
		}
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.returnType != null) {
			ok = ok && this.returnType.completeResolve(_scope);
		}
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.completeResolve(this.methodScope);
		}
		if (!this.isAbstract && this.body != null) {
			ok = ok && this.body.completeResolve(this.methodScope);
		}
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.checkType();
		}
		if (!this.isAbstract && this.body != null) {
			ok = ok && this.body.checkType();
		}
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		if (this.isAbstract) return 0;

		this.paramsSize = 0;
		for (ParameterDeclaration p : this.parameters) {
			this.paramsSize += p.getType().length();
		}
		
		// En TAM, LB pointe sur l'adresse de retour. Les paramètres sont empilés AVANT.
		// Donc leur adresse est LB - tailleDesParametres
		int currentOffset = -this.paramsSize;
		for (ParameterDeclaration p : this.parameters) {
			currentOffset += p.allocateMemory(Register.LB, currentOffset);
		}
		
		// Le corps commence à LB + 3 (après ancienne base, base appelant, et PC)
		if (this.body != null) {
			this.body.allocateMemory(Register.LB, 3);
		}
		return 0; 
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		if (this.isAbstract) return frag; // Pas de code pour une méthode abstraite

		frag.add(_factory.createLabel("method_" + this.name));
		
		if (this.body != null) {
			frag.append(this.body.getCode(_factory));
		}
		
		// Nettoyage de la pile au retour : on retourne 0 ou 1 case (selon returnType), 
		// et on libère paramsSize cases (les arguments empilés)
		int returnSize = (this.returnType == null) ? 0 : this.returnType.length();
		frag.add(_factory.createReturn(returnSize, this.paramsSize));
		
		return frag;
	}
}