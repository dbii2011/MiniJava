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

public class ConstructorDeclaration extends ClassElement {
	
	protected List<ParameterDeclaration> parameters;
	protected Block body;
	protected SymbolTable constructorScope;
	protected int paramsSize;

	// Le constructeur appelé par votre ASTBuilder
	public ConstructorDeclaration(String _name, List<ParameterDeclaration> _parameters, Block _body) {
		super(ElementKind.CONSTRUCTOR, AccessRight.PUBLIC, _name);
		this.parameters = _parameters;
		this.body = _body;
		this.paramsSize = 0;
	}

	@Override
	public Type getType() {
		return null; // Un constructeur renvoie techniquement l'objet, mais dans l'AST on met null
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.accepts(this)) {
			System.err.println("Erreur : Le constructeur " + this.name + " est déjà défini.");
			return false;
		}
		_scope.register(this);

		this.constructorScope = new SymbolTable(_scope);
		boolean ok = true;
		
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.collectAndPartialResolve(this.constructorScope);
		}
		ok = ok && this.body.collectAndPartialResolve(this.constructorScope);
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.completeResolve(this.constructorScope);
		}
		ok = ok && this.body.completeResolve(this.constructorScope);
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		for (ParameterDeclaration p : this.parameters) {
			ok = ok && p.checkType();
		}
		ok = ok && this.body.checkType();
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.paramsSize = 0;
		for (ParameterDeclaration p : this.parameters) {
			this.paramsSize += p.getType().length();
		}
		
		int currentOffset = -this.paramsSize;
		for (ParameterDeclaration p : this.parameters) {
			currentOffset += p.allocateMemory(Register.LB, currentOffset);
		}
		
		this.body.allocateMemory(Register.LB, 3);
		return 0; 
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.add(_factory.createLabel("constructor_" + this.name));
		frag.append(this.body.getCode(_factory));
		// On ne retourne rien, mais on nettoie les arguments
		frag.add(_factory.createReturn(0, this.paramsSize));
		return frag;
	}
}