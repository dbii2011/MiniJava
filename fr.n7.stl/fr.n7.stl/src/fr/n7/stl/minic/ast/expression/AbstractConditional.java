/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a conditional expression.
 * @author Marc Pantel
 *
 */
public class AbstractConditional<ExpressionKind extends Expression> implements Expression {

	/**
	 * AST node for the expression whose value is the condition for the conditional expression.
	 */
	protected Expression condition;
	
	/**
	 * AST node for the expression whose value is the then parameter for the conditional expression.
	 */
	protected ExpressionKind thenExpression;
	
	/**
	 * AST node for the expression whose value is the else parameter for the conditional expression.
	 */
	protected ExpressionKind elseExpression;
	
	/**
	 * Builds a binary expression Abstract Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _left : Expression for the left parameter.
	 * @param _operator : Binary Operator.
	 * @param _right : Expression for the right parameter.
	 */
	public AbstractConditional(Expression _condition, ExpressionKind _then, ExpressionKind _else) {
		this.condition = _condition;
		this.thenExpression = _then;
		this.elseExpression = _else;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
	    boolean _res = this.condition.collectAndPartialResolve(_scope);
	    _res &= this.thenExpression.collectAndPartialResolve(_scope);
	    _res &= this.elseExpression.collectAndPartialResolve(_scope);
	    return _res;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    boolean _res = this.condition.completeResolve(_scope);
	    _res &= this.thenExpression.completeResolve(_scope);
	    _res &= this.elseExpression.completeResolve(_scope);
	    return _res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.condition + " ? " + this.thenExpression + " : " + this.elseExpression + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	
	@Override
	public Type getType() {
	    Type _tThen = this.thenExpression.getType();
	    Type _tElse = this.elseExpression.getType();
	    
	    if (_tThen.compatibleWith(_tElse)) {
	        return _tThen; // Ou le type le plus général des deux
	    } else if (_tElse.compatibleWith(_tThen)) {
	        return _tElse;
	    } else {
	        fr.n7.stl.util.Logger.error("Types incompatibles dans l'expression ternaire : " + _tThen + " et " + _tElse);
	        return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
	    }
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
	    Fragment _result = _factory.createFragment();
	    int id = _factory.createLabelNumber();
	    
	    // 1. Évaluation de la condition (laisse 0 ou 1 sur la pile)
	    _result.append(this.condition.getCode(_factory));
	    
	    // 2. Saut vers le ELSE si la condition est fausse (0)
	    // Attention : l'étiquette doit être identique à celle définie plus bas
	    _result.add(_factory.createJumpIf("ElseTernary_" + id, 0));
	    
	    // 3. Branche THEN : on génère le code et on saute à la fin
	    _result.append(this.thenExpression.getCode(_factory));
	    _result.add(_factory.createJump("EndTernary_" + id));
	    
	    // 4. Branche ELSE : on définit le label ICI avant le code du else
	    Fragment _elseFragment = this.elseExpression.getCode(_factory);
	    _elseFragment.addPrefix("ElseTernary_" + id);
	    _result.append(_elseFragment);
	    
	    // 5. Fin : on définit le label de sortie
	    _result.addPrefix("EndTernary_" + id);
	    
	    return _result;
	}

}
