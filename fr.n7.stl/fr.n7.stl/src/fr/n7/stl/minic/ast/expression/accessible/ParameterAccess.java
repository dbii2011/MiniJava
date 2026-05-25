/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;

import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.tam.ast.Register;

/**
 * Implementation of the Abstract Syntax Tree node for a variable access expression.
 * @author Marc Pantel
 */
public class ParameterAccess extends AbstractAccess {
	
	protected ParameterDeclaration declaration;
	
	/**
	 * Creates a variable use expression Abstract Syntax Tree node.
	 * @param _name Name of the used variable.
	 */
	public ParameterAccess(ParameterDeclaration _declaration) {
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
	@Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // Si getRegister() n'existe pas, on utilise Register.LB (Local Base) 
        // car les paramètres sont dans le bloc d'activation local.
        _result.add(_factory.createLoad(
                Register.LB,
                this.declaration.getOffset(),
                this.declaration.getType().length()));
                
        return _result;
    }

}
