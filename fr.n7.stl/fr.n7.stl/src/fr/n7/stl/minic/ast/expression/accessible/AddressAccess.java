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
import fr.n7.stl.minic.ast.expression.assignable.VariableAssignment;
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

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.assignable.completeResolve(_scope);
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
    @Override
    public Type getType() {
        // On crée un nouveau PointerType qui pointe vers le type de l'assignable
        return new fr.n7.stl.minic.ast.type.PointerType(this.assignable.getType());
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
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

}
