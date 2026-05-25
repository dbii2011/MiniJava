/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

<<<<<<< HEAD
=======
import fr.n7.stl.minic.ast.SemanticsUndefinedException;

>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
<<<<<<< HEAD
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a parameter access expression.
 * @author Marc Pantel
 */
public class ParameterAccess extends AbstractAccess {

	protected ParameterDeclaration declaration;

	public ParameterAccess(ParameterDeclaration _declaration) {
		this.declaration = _declaration;
	}

	@Override
=======
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
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	public Declaration getDeclaration() {
		return this.declaration;
	}

<<<<<<< HEAD
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		// Les paramètres sont adressés relativement à LB avec un offset négatif
		frag.add(_factory.createLoad(
				Register.LB,
				this.declaration.getOffset(),
				this.declaration.getType().length()));
		frag.addComment(this.declaration.getName());
		return frag;
	}

}
=======
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
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
