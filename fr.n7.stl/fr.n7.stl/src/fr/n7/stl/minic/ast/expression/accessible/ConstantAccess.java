/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a constant access expression.
 * @author Marc Pantel
 */
public class ConstantAccess extends AbstractAccess {
	
	protected ConstantDeclaration declaration;
	
	/**
	 * Creates a variable use expression Abstract Syntax Tree node.
	 * @param _name Name of the used variable.
	 */
	public ConstantAccess(ConstantDeclaration _declaration) {
		this.declaration = _declaration;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractUse#getDeclaration()
	 */
	public Declaration getDeclaration() {
		return this.declaration;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractUse#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
<<<<<<< HEAD
	public Fragment getCode(TAMFactory _factory) {
		// On génère le code de la valeur associée à cette constante
    	return this.declaration.getValue().getCode(_factory);
=======
	@Override
	public Fragment getCode(TAMFactory _factory) {
	    // On doit demander le code de la VALEUR (ex: LOADL 10)
	    return this.declaration.getValue().getCode(_factory);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

}
