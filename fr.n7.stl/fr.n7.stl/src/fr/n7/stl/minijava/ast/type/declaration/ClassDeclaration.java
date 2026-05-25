/**
 * 
 */
package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * 
 */
public class ClassDeclaration implements Instruction, Declaration {
	
	protected List<ClassElement> elements;
	
	protected boolean concrete;
	
	protected String name;
	
	protected String ancestor;

    protected ClassDeclaration ancestorClass;

	protected SymbolTable classScope;

	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, String _ancestor, List<ClassElement> _elements) {
		this.concrete = _concrete;
		this.name = _name;
		this.ancestor = _ancestor;
		this.elements = _elements;
	}
	
	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, List<ClassElement> _elements) {
		this( _concrete, _name, null, _elements);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (_scope.accepts(this)) {
			_scope.register(this);
		} else {
			System.err.println("Erreur: La classe " + this.name + " est déjà définie.");
			return false;
		}

		// on crée un scope propre à la classe
		this.classScope = new SymbolTable(_scope);
		boolean ok = true;
		for (ClassElement e : this.elements) {
			ok = ok && e.collectAndPartialResolve(this.classScope);
		}
		return ok;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		return this.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.ancestor != null) {
			if (_scope.contains(this.ancestor)) {
				Declaration decl = _scope.get(this.ancestor);
				if (decl instanceof ClassDeclaration) {
					this.ancestorClass = (ClassDeclaration) decl;
				} else {
					System.err.println("Erreur: " + this.ancestor + " n'est pas une classe valide.");
					ok = false;
				}
			} else {
				System.err.println("Erreur: Classe parente " + this.ancestor + " introuvable.");
				ok = false;
			}
		}

		for (ClassElement e : this.elements) {
			ok = ok && e.completeResolve(this.classScope);
		}
		return ok;
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		for (ClassElement e : this.elements) {
			ok = ok && e.checkType();
		}
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		int currentOffset = 0;
		if (this.ancestorClass != null) {
			currentOffset = this.ancestorClass.getInstanceSize();
		}
		for (ClassElement e : this.elements) {
			if (e.getElementKind() == ElementKind.OBJECT) {
				currentOffset += e.allocateMemory(Register.HB, currentOffset);
			}
		}
		return 0;
	}

    public int getInstanceSize() {
		int size = 0;
		if (this.ancestorClass != null) {
			size += this.ancestorClass.getInstanceSize();
		}
		for (ClassElement e : this.elements) {
			if (e instanceof AttributeDeclaration && e.getElementKind() == ElementKind.OBJECT) {
				size += ((AttributeDeclaration)e).getType().length();
			}
		}
		return size;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		for (ClassElement e : this.elements) {
			frag.append(e.getCode(_factory));
		}
		return frag;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return new ClassType(this.name);
	}

    public boolean inheritsFrom(ClassType other) {
		if (this.name.equals(other.name)) return true;
		if (this.ancestorClass != null) {
			return this.ancestorClass.inheritsFrom(other);
		}
		return false;
	}
	
	@Override
	public String toString() {
		String image = "";
		if (! this.concrete) {
			image += "abstract ";
		}
		image += "class " + this.name + " ";
		if (this.ancestor != null) {
			image += "extends " + this.ancestor + " ";
		}
		image += "{\n";
		for (ClassElement e : this.elements) {
			image += e;
		}
		image += "}\n";
		return image;
	}

}
