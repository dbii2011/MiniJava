package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.List;

public class ConstructorDeclaration extends ClassElement {
	
	protected List<ParameterDeclaration> parameters;
	protected Block body;
	protected SymbolTable constructorScope;
	protected int paramsSize;

	public ConstructorDeclaration(String _name, List<ParameterDeclaration> _parameters, Block _body) {
		super(ElementKind.CONSTRUCTOR, AccessRight.PUBLIC, _name);
		this.parameters = _parameters;
		this.body = _body;
		this.paramsSize = 0;
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.accepts(this)) {
			System.err.println("Erreur : Le constructeur " + this.name + " est dÃ©jÃ  dÃ©fini.");
			return false;
		}
		_scope.register(this);

		this.constructorScope = new SymbolTable(_scope);
		boolean ok = true;
		
		for (ParameterDeclaration p : this.parameters) {
			if (!this.constructorScope.accepts(p)) {
				System.err.println("Erreur : Le paramÃ¨tre " + p.getName() + " est dÃ©jÃ  dÃ©fini.");
				ok = false;
			} else {
				this.constructorScope.register(p);
			}
		}
		
		if (this.body != null) {
		    ok = ok && this.body.collectAndPartialResolve(this.constructorScope);
		}
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.body != null) {
		    ok = ok && this.body.completeResolve(this.constructorScope);
		}
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		if (this.body != null) {
		    ok = ok && this.body.checkType();
		}
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.paramsSize = 0;
		for (ParameterDeclaration p : this.parameters) {
			this.paramsSize += p.getType().length();
		}
		
		if (this.body != null) {
		    this.body.allocateMemory(Register.LB, 3);
		}
		return 0; 
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		// 1. On ajoute le corps du constructeur
		if (this.body != null) {
		    frag.append(this.body.getCode(_factory));
		}
		
		// 2. On ajoute l'instruction de retour (garantit que le fragment n'est plus vide)
		frag.add(_factory.createReturn(0, this.paramsSize));
		
		// 3. MAINTENANT on peut coller l'Ã©tiquette en toute sÃ©curitÃ© !
		frag.addPrefix("constructor_" + this.name);
		
		return frag;
	}
}