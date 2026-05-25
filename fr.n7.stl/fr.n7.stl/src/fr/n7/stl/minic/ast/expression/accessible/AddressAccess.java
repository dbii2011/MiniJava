/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
<<<<<<< HEAD
=======

>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.PointerType;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;

=======
import fr.n7.stl.minic.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
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
<<<<<<< HEAD
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
=======
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return this.assignable.collectAndPartialResolve(_scope);
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.assignable.completeResolve(_scope);
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
<<<<<<< HEAD
	@Override
	public Type getType() {
		// Le type de &var est un pointeur vers le type de var
		return new fr.n7.stl.minic.ast.type.PointerType(this.assignable.getType());
	}
=======
    @Override
    public Type getType() {
        // On crée un nouveau PointerType qui pointe vers le type de l'assignable
        return new fr.n7.stl.minic.ast.type.PointerType(this.assignable.getType());
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
<<<<<<< HEAD
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
=======
    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        if (this.assignable instanceof VariableAssignment) {
            // On récupère la déclaration (qui contient le registre et l'offset)
            VariableDeclaration _decl = ((VariableAssignment) this.assignable).getDeclaration();
            
            // On met l'adresse de la variable sur la pile
            _result.add(_factory.createLoadA(_decl.getRegister(), _decl.getOffset()));
        }
        
        return _result;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

}
