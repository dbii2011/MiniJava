/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.util.Logger;
import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.AtomicType;
/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Iteration implements Instruction {

	protected Expression condition;
	protected Block body;

	public Iteration(Expression _condition, Block _body) {
		this.condition = _condition;
		this.body = _body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "while (" + this.condition + " )" + this.body;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _condition = this.condition.collectAndPartialResolve(_scope);
        boolean _body = this.body.collectAndPartialResolve(_scope);
        return _condition && _body;
    }
    
    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
        boolean _condition = this.condition.collectAndPartialResolve(_scope);
        boolean _body = this.body.collectAndPartialResolve(_scope, _container);
        return _condition && _body;
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        return this.condition.completeResolve(_scope) && this.body.completeResolve(_scope);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
    @Override
    public boolean checkType() {
        boolean _conditionType = this.condition.getType().equalsTo(AtomicType.BooleanType);
        if (!_conditionType) {
            Logger.error("The condition in while is not a boolean.");
        }
        return _conditionType && this.body.checkType();
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
    @Override
    public int allocateMemory(Register _register, int _offset) {
        // On transmet le registre (SB ou LB) au corps de la boucle
        this.body.allocateMemory(_register, _offset); 
        return 0; // Le while lui-même ne prend pas de place sur la pile
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();
        String _labelStart = "loop_" + _factory.createLabelNumber();
        String _labelEnd = "end_loop_" + _factory.createLabelNumber();

        // 1. On génère d'abord le code de la condition
        Fragment _cond = this.condition.getCode(_factory);
        
        // 2. On ajoute l'étiquette sur la première instruction de la condition
        _cond.addPrefix(_labelStart);
        
        // 3. On assemble
        _result.append(_cond);
        _result.add(_factory.createJumpIf(_labelEnd, 0));
        _result.append(this.body.getCode(_factory));
        _result.add(_factory.createJump(_labelStart));
        _result.addSuffix(_labelEnd); // addSuffix fonctionne souvent mieux sur un fragment plein
        
        return _result;
    }

}
