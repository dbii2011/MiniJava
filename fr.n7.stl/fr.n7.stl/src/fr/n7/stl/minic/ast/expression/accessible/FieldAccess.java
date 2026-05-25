/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.AbstractField;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.RecordType;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField<AccessibleExpression> implements AccessibleExpression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(AccessibleExpression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		// empile l'enregistrement
		_fragment.append(this.record.getCode(_factory));
		
		RecordType _recordType = (RecordType) this.record.getType();
		int _offset = 0;
		// calcule l'offset du champ cible
		for (FieldDeclaration _field : _recordType.getFields()) {
			if (_field.getName().equals(this.name)) {
				break;
			}
			_offset += _field.getType().length();
		}
 
		int _fieldSize  = this.getType().length();
		int _recordSize = _recordType.length();

		// Nombre de mots après le champ cible 
		int _suffix = _recordSize - _offset - _fieldSize;

		
		//  nettoie la pile pour ne garder que le champ
		// supprime le suffix
		if (_suffix > 0) {
			_fragment.add(_factory.createPop(0, _suffix));
		}

		//  supprime le préfix
		if (_offset > 0) {
			_fragment.add(_factory.createPop(_fieldSize, _offset));
		}
		
		return _fragment;
	}

}
