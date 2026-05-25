/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
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
	public Declaration getDeclaration() {
		return this.declaration;
	}

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