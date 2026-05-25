/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.PointerType;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;

/**
* Implementation of the Abstract Syntax Tree node for accessing an expression address.
* @author Marc Pantel
*
*/
public class AddressAccess implements AccessibleExpression {

	protected AssignableExpression assignable;

	public AddressAccess(AssignableExpression _assignable) {
		this.assignable = _assignable;
	}
	
	@Override
	public String toString() {
		return "& " + this.assignable.toString();
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.assignable.collectAndPartialResolve(_scope);	
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.assignable.completeResolve(_scope);	
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		// Le type de &var est un pointeur vers le type de var
		return new fr.n7.stl.minic.ast.type.PointerType(this.assignable.getType());
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		
		// Si on demande l'adresse d'une variable classique 
		if (this.assignable instanceof fr.n7.stl.minic.ast.expression.assignable.VariableAssignment) {
			fr.n7.stl.minic.ast.expression.assignable.VariableAssignment va = (fr.n7.stl.minic.ast.expression.assignable.VariableAssignment) this.assignable;
			// On récupère sa déclaration pour avoir son registre et son offset
			VariableDeclaration decl = va.getDeclaration(); 
			frag.add(_factory.createLoadA(decl.getRegister(), decl.getOffset()));
		} 
		
		else if (this.assignable instanceof fr.n7.stl.minic.ast.expression.assignable.PointerAssignment) {
			fr.n7.stl.minic.ast.expression.assignable.PointerAssignment pa = (fr.n7.stl.minic.ast.expression.assignable.PointerAssignment) this.assignable;
			// L'adresse de *ptr, c'est ptr tout court !
			frag.append(pa.getPointer().getCode(_factory));
		}
		else {
			fr.n7.stl.util.Logger.error("L'opérateur & n'est implémenté que pour les variables et les pointeurs.");
		}
		
		return frag;
	}

}
