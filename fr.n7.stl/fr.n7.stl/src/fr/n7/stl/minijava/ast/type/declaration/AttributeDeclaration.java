package fr.n7.stl.minijava.ast.type.declaration;

import fr.n7.stl.minic.ast.type.Type;

public class AttributeDeclaration extends ClassElement {
	
	protected Type type;
    protected int offset;

	public AttributeDeclaration( String _name, Type _type) {
		super(_name);
		this.type = _type;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.accessRight + " " + type + " " + this.name + ";\n"; 
	}

    @Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// dans la tds de la classe
		if (_scope.accepts(this)) {
			_scope.register(this);
			return true;
		} else {
			System.err.println("Erreur: L'attribut " + this.name + " est déjà défini.");
			return false;
		}
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.type.completeResolve(_scope);
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.offset = _offset;
		return this.type.length();
	}

	public int getOffset() {
		return this.offset;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		return _factory.createFragment();
	}

}
