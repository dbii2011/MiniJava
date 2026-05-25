/**
 * 
 */
package fr.n7.stl.minic.ast.type;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;

/**
 * Implementation of the Abstract Syntax Tree node for a pointer type.
 * @author Marc Pantel
 *
 */
public class PointerType implements Type {

	protected Type element;

	public PointerType(Type _element) {
		this.element = _element;
	}
	
	public Type getPointedType() {
		return this.element;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof PointerType) {
<<<<<<< HEAD
			return this.element.equalsTo(((PointerType) _other).getPointedType());
		}
		return false;
	}
=======
            return this.element.equalsTo(((PointerType) _other).element);
        }
        return false;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
<<<<<<< HEAD
		// 2 pointeurs sont compatibles s'ils pointent vers des types compatibles
		if (_other instanceof PointerType) {
			return this.element.compatibleWith(((PointerType) _other).getPointedType());
		}
		// Un pointeur est compatible avec la valeur "null"
		if (_other == AtomicType.NullType) {
			return true;
		}
		return false;
	}
=======
        if (_other instanceof PointerType) {
            return this.element.compatibleWith(((PointerType) _other).element);
        }
        return false;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
<<<<<<< HEAD
		if (this.compatibleWith(_other)) {
			return this;
		}
		return null;
	}
=======
        if (_other instanceof PointerType) {
            return new PointerType(this.element.merge(((PointerType) _other).element));
        }
        return AtomicType.ErrorType;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
<<<<<<< HEAD
		return 1; // Une adresse prend toujours 1 mot mémoire en TAM
	}
=======
        return 1;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.element + " *)";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.element.completeResolve(_scope);
	}

}
