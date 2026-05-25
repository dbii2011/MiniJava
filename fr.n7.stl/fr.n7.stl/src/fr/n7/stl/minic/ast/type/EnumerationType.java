/**
 * 
 */
package fr.n7.stl.minic.ast.type;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.declaration.LabelDeclaration;

/**
 * @author Marc Pantel
 *
 */
public class EnumerationType implements Type, Declaration {
	
	private String name;
	
	private List<LabelDeclaration> labels;

	/**
	 * 
	 */
	public EnumerationType(String _name, List<LabelDeclaration> _labels) {
		this.name = _name;
		this.labels = _labels;

		// pour que chaque étiquette connaisse son parent et a un index
		int i=0;
		for (LabelDeclaration label : this.labels) {
			label.setParent(this,i);
			i++;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = "enum" + this.name + " { ";
		Iterator<LabelDeclaration> _iter = this.labels.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " }";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#equalsTo(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		return _other instanceof EnumerationType && this.name.equals(((EnumerationType)_other).getName());
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#compatibleWith(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		return this.equalsTo(_other);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#merge(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public Type merge(Type _other) {
		return this.compatibleWith(_other) ? this : null;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#length()
	 */
	@Override
	public int length() {
		return 1; // représentée par un entier (1 mot)
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean res = true;
		for (LabelDeclaration label : this.labels) {
			if (_scope.accepts(label)) {
				_scope.register(label);
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this;
	}

}
