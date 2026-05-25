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
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.CoupleType;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Abstract Syntax Tree node for an expression extracting the first component in a couple.
 * @author Marc Pantel
 *
 */
public class First implements AccessibleExpression {

	/**
	 * AST node for the expression whose value must whose first element is extracted by the expression.
	 */
	protected AccessibleExpression target;

	/**
	 * Builds an Abstract Syntax Tree node for an expression extracting the first component of a couple.
	 * @param _target : AST node for the expression whose value must whose first element is extracted by the expression.
	 */
	public First(AccessibleExpression _target) {
		this.target = _target;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(fst" + this.target + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
<<<<<<< HEAD
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.target.collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.target.completeResolve(_scope);
	}
=======
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        return this.target.collectAndPartialResolve(_scope);
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.target.completeResolve(_scope);
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
<<<<<<< HEAD
	@Override
	public Type getType() {
		Type t = this.target.getType();
		if (t instanceof CoupleType) {
			return ((CoupleType) t).getFirst();
		}
		return null;
	}
=======
    @Override
    public Type getType() {
        Type _typeTarget = this.target.getType();
        if (_typeTarget instanceof fr.n7.stl.minic.ast.type.CoupleType) {
            return ((fr.n7.stl.minic.ast.type.CoupleType) _typeTarget).getFirst();
        } else {
            fr.n7.stl.util.Logger.error(this.target + " n'est pas un couple.");
            return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
        }
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
<<<<<<< HEAD
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.append(this.target.getCode(_factory)); // Empile [A, B]
		
		CoupleType ct = (CoupleType) this.target.getType();
		// On garde 0 mots au sommet, supprime B en dessous
		// ce qui revient à supprimer B du sommet, laissant A
		frag.add(_factory.createPop(0, ct.getSecond().length()));
		return frag;
	}
=======
    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        
        // 1. On empile le couple complet <first, second>
        _result.append(this.target.getCode(_factory));
        
        // 2. On doit nettoyer la partie 'second' de la pile.
        // En TAM, POP(taille_a_garder, taille_a_supprimer) permet de faire ça.
        int _sizeFirst = this.getType().length();
        int _sizeSecond = ((fr.n7.stl.minic.ast.type.CoupleType)this.target.getType()).getSecond().length();
        
        _result.add(_factory.createPop(_sizeFirst, _sizeSecond));
        
        return _result;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

}
