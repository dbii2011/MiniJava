package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;

public class ClassType implements Type {
	
	protected String name;
    public ClassDeclaration declaration; // lien vers l'AST

	public ClassType(String _name) {
		this.name = _name;
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		if (_other instanceof ClassType) {
			return this.name.equals(((ClassType)_other).name);
		}
		return false;
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Auto-generated method stub
		if (this.equalsTo(_other)) return true;
		if (_other instanceof ClassType) {
			// on accepte l'affectation si ce type hérite de l'autre
			return this.declaration != null && this.declaration.inheritsFrom((ClassType)_other);
		}
		return false;
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
        if (this.compatibleWith(_other)) return _other;
		if (_other.compatibleWith(this)) return this;
		return null;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 1; // un objet est un pointeur
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		if (_scope.knows(this.name)) {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof ClassDeclaration) {
				this.declaration = (ClassDeclaration) decl;
				return true;
			} else {
				System.err.println("Erreur de type : " + this.name + " n'est pas une classe.");
				return false;
			}
		} else {
			System.err.println("Erreur de type : Classe " + this.name + " non trouvée.");
			return false;
		}
	}
	
	public String toString() {
		return " " + this.name + " ";
	}

}
