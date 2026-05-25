/**
 * 
 */
package fr.n7.stl.minic.ast.type.declaration;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.EnumerationType;
import fr.n7.stl.minic.ast.type.Type;

/**
 * Implementation of the Abstract Syntax Tree node for a field in a record.
 * @author Marc Pantel
 *
 */
public class LabelDeclaration implements Declaration {

	private String name;

	private EnumerationType parent; // lien vers l'énumération qui contient cette étiquette

	private int index; // la valeur de l'étiquette (0, 1, 2...)

	public LabelDeclaration(String _name) {
		this.name = _name;
		this.parent = null;
		this.index = 0;
	}

	/*public void setParent(EnumerationType _parent) { // sera appelée par EnumerationType
		this.parent = _parent;
	}*/

	public void setParent(EnumerationType _parent, int _index) { // sera appelée par EnumerationType
		this.parent = _parent;
		this.index = _index;
	}

	public int getIndex() {
		return this.index;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Type getType() {
		// TODO : Should be the type of the enum containing the label...
		if (this.parent != null) {
			return this.parent;
		} else {
			return AtomicType.ErrorType; // Si le parent n'est pas encore défini
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name + ";";
	}

}
