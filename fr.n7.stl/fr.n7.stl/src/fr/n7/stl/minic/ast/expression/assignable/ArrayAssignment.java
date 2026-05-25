/**
 * 
 */
package fr.n7.stl.minic.ast.expression.assignable;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;

import fr.n7.stl.minic.ast.expression.AbstractArray;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.expression.accessible.BinaryOperator;

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

	
}
