/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node  for an expression extracting the second component in a couple.
 * @author Marc Pantel
 *
 */
public class Second implements AccessibleExpression {

	/**
	 * AST node for the expression whose value must whose second element is extracted by the expression.
	 */
	private AccessibleExpression target;
	
	/**
	 * Builds an Abstract Syntax Tree node for an expression extracting the second component of a couple.
	 * @param _target : AST node for the expression whose value must whose second element is extracted by the expression.
	 */
	public Second(AccessibleExpression _target) {
		this.target = _target;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(snd" + this.target + ")";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
    public Type getType() {
        Type _typeTarget = this.target.getType();
        if (_typeTarget instanceof fr.n7.stl.minic.ast.type.CoupleType) {
            // On extrait le type du deuxième élément du couple
            return ((fr.n7.stl.minic.ast.type.CoupleType) _typeTarget).getSecond();
        } else {
            fr.n7.stl.util.Logger.error(this.target + " n'est pas un couple.");
            return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
        }
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return this.target.collectAndPartialResolve(_scope);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.target.completeResolve(_scope);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // 1. On empile le couple complet <first, second>
        _result.append(this.target.getCode(_factory));
        
        // 2. Logique du POP pour 'snd' :
        // On veut garder ce qui est au sommet (second) et supprimer ce qui est en dessous (first)
        int _sizeFirst = ((fr.n7.stl.minic.ast.type.CoupleType)this.target.getType()).getFirst().length();
        int _sizeSecond = this.getType().length();
        
        // POP(taille_a_garder, taille_a_supprimer)
        // On garde 'sizeSecond' mots et on jette les 'sizeFirst' mots qui étaient en dessous
        _result.add(_factory.createPop(_sizeSecond, _sizeFirst));
        
        return _result;
    }

}
