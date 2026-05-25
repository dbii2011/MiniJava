/**
 * 
 */
package fr.n7.stl.minic.ast.expression.assignable;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
<<<<<<< HEAD
=======

>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.expression.AbstractArray;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
<<<<<<< HEAD
=======
import fr.n7.stl.minic.ast.expression.accessible.BinaryOperator;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a cell in an array.
 * @author Marc Pantel
 */
public class ArrayAssignment extends AbstractArray<AssignableExpression> implements AssignableExpression {

	/**
	 * Construction for the implementation of an array element assignment expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element assignment expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element assignment expression.
	 */
	public ArrayAssignment(AssignableExpression _array, AccessibleExpression _index) {
		super(_array, _index);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.ArrayAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
<<<<<<< HEAD
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		
		frag.append(this.array.getCode(_factory));
		
		
		frag.add(_factory.createLoadI(1)); // déréférence

		// On charge l'index sur la pile
		frag.append(this.index.getCode(_factory));
		
		
		int elementSize = this.getType().length();
		if (elementSize > 1) {
			frag.add(_factory.createLoadL(elementSize));
			frag.add(fr.n7.stl.tam.ast.Library.IMul);
		}
		frag.add(fr.n7.stl.tam.ast.Library.IAdd);
		
		
		return frag;
	}
=======
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        _result.append(this.array.getCode(_factory));
        
        _result.append(this.index.getCode(_factory));
        
        int _size = this.getType().length();
        _result.add(_factory.createLoadL(_size));
        
        _result.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
        
        _result.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
        
        return _result;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	
}
