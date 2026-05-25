/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.AbstractPointer;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a pointer access expression.
 * @author Marc Pantel
 *
 */
public class PointerAccess extends AbstractPointer<AccessibleExpression> implements AccessibleExpression {

	/**
	 * Construction for the implementation of a pointer content access expression Abstract Syntax Tree node.
	 * @param _pointer Abstract Syntax Tree for the pointer expression in a pointer content access expression.
	 */
	public PointerAccess(AccessibleExpression _pointer) {
		super(_pointer);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
<<<<<<< HEAD
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		frag.append(this.pointer.getCode(_factory));
		//pour lire la valeur située à cette adresse
		frag.add(_factory.createLoadI(this.getType().length()));
		return frag;
	}
=======
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        _result.append(this.pointer.getCode(_factory));
        
        _result.add(_factory.createLoadI(this.getType().length()));
        
        return _result;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	@Override
	public String toString() {
		return "* " + this.pointer.toString();
	}

}
