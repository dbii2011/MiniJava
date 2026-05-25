/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.AbstractArray;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing an array element.
 * @author Marc Pantel
 *
 */
public class ArrayAccess extends AbstractArray<AccessibleExpression> implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(AccessibleExpression _array, AccessibleExpression _index) {
		super(_array,_index);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
    //  récupère l'adresse de base du tableau
    frag.append(this.array.getCode(_factory));
    //  évalue l'index
    frag.append(this.index.getCode(_factory));
    //Calcul de l'offset et ajout à l'adresse de base
    if (this.getType().length() > 1) {
        frag.add(_factory.createLoadL(this.getType().length()));
        frag.add(Library.IMul);
    }
    frag.add(Library.IAdd);
    // charge la valeur depuis cette adresse calculée
    frag.add(_factory.createLoadI(this.getType().length()));
    return frag;
	}

}
