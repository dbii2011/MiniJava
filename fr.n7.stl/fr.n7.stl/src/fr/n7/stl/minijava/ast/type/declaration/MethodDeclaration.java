package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.List;

public class MethodDeclaration extends ClassElement {
	
	protected Type returnType;
	protected List<ParameterDeclaration> parameters;
	protected Block body;
	protected SymbolTable methodScope;
	protected int paramsSize;
	protected boolean isAbstract;
	public String className; // Pour identifier la classe propriétaire

	public MethodDeclaration(String _name, Type _returnType, List<ParameterDeclaration> _parameters, Block _body) {
		super(ElementKind.METHOD, AccessRight.PUBLIC, _name);
		this.returnType = _returnType;
		this.parameters = _parameters;
		this.body = _body;
		this.isAbstract = false;
		this.paramsSize = 0;
	}

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
			System.err.println("Erreur : La mÃ©thode " + this.name + " est dÃ©jÃ  dÃ©finie.");
			return false;
		}
		_scope.register(this);

		this.methodScope = new SymbolTable(_scope);
		boolean ok = true;
		
		// Les paramÃ¨tres n'ont pas de mÃ©thode "collect", on les enregistre juste manuellement
		for (ParameterDeclaration p : this.parameters) {
			if (!this.methodScope.accepts(p)) {
				System.err.println("Erreur : Le paramÃ¨tre " + p.getName() + " est dÃ©jÃ  dÃ©fini.");
				ok = false;
			} else {
				this.methodScope.register(p);
			}
		}
		
		if (!this.isAbstract && this.body != null) {
			// On simule une FunctionDeclaration pour que le "Return" connaisse le type attendu
			FunctionDeclaration container = new FunctionDeclaration(this.name, this.returnType, this.parameters, this.body);
			ok = ok && this.body.collectAndPartialResolve(this.methodScope, container);
		}
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.returnType != null) {
			ok = ok && this.returnType.completeResolve(_scope);
		}
		// Pas besoin de rÃ©soudre les paramÃ¨tres, ils le sont dÃ©jÃ 
		if (!this.isAbstract && this.body != null) {
			ok = ok && this.body.completeResolve(this.methodScope);
		}
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
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
		
		// Le corps de la mÃ©thode s'alloue Ã  partir de LB + 3 (aprÃ¨s l'ancienne base et PC)
		if (this.body != null) {
			this.body.allocateMemory(Register.LB, 3);
		}
		return 0; 
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		if (this.isAbstract) return frag;

		// 1. On ajoute le corps de la mÃ©thode
		if (this.body != null) {
			frag.append(this.body.getCode(_factory));
		}
		
		// 2. On ajoute l'instruction de retour
		int returnSize = (this.returnType == null) ? 0 : this.returnType.length();
		frag.add(_factory.createReturn(returnSize, this.paramsSize + 1));
		
		// 3. On colle l'Ã©tiquette Ã  la fin
		String label = "method_" + this.className + "_" + this.name;
		frag.addPrefix(label);
		
		return frag;
	}

	public List<ParameterDeclaration> getParameters() {
		return this.parameters;
	}

	@Override
	public String toString() {
		String params = "";
		for (int i = 0; i < this.parameters.size(); i++) {
			fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration p = this.parameters.get(i);
			params += p.getType() + " " + p.getName();
			if (i < this.parameters.size() - 1) params += ", ";
		}
		if (this.isAbstract) {
			return "public abstract " + this.returnType + " " + this.name + "(" + params + ");\n";
		} else {
			return "public " + this.returnType + " " + this.name + "(" + params + ") " + (this.body != null ? this.body : "{}") + "\n";
		}
	}
}