/**
 * 
 */
package fr.n7.stl.minic.ast.expression.assignable;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.AbstractField;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
<<<<<<< HEAD
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.minic.ast.type.RecordType;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAssignment extends AbstractField<AssignableExpression> implements AssignableExpression {

	/**
	 * Construction for the implementation of a record field assignment expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field assignment expression.
	 * @param _name Name of the field in the record field assignment expression.
	 */
	public FieldAssignment(AssignableExpression _record, String _name) {
		super(_record, _name);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.FieldAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
<<<<<<< HEAD
		Fragment _fragment = _factory.createFragment();
		
		// Empiler l'adresse de base de l'enregistrement
		
		_fragment.append(this.record.getCode(_factory));
		
		// Calculer l'offset du champ dans le struct
		RecordType _type = (RecordType) this.record.getType();
		int _offset = 0;
		for (FieldDeclaration _field : _type.getFields()) {
			if (_field.getName().equals(this.name)) {
				break;
			}
			_offset += _field.getType().length();
		}
		
		// ajoute l'offset à l'adresse de base pour obtenir l'adresse du champ
		if (_offset > 0) {
			_fragment.add(_factory.createLoadL(_offset));
			_fragment.add(Library.IAdd);
		}
		// Si offset == 0, l'adresse de base est déjà la bonne adresse
		
		return _fragment;

=======
		throw new SemanticsUndefinedException("Semantics getCode undefined in FieldAssignment.");
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}
	
}
