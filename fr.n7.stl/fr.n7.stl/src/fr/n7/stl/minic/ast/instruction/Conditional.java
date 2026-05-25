/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import java.util.Optional;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _cond = this.condition.collectAndPartialResolve(_scope);
        boolean _then = this.thenBranch.collectAndPartialResolve(_scope);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.collectAndPartialResolve(_scope);
        }
        return _cond && _then && _else;
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
        boolean _cond = this.condition.collectAndPartialResolve(_scope);
        boolean _then = this.thenBranch.collectAndPartialResolve(_scope, _container);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.collectAndPartialResolve(_scope, _container);
        }
        return _cond && _then && _else;
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean _cond = this.condition.completeResolve(_scope);
        boolean _then = this.thenBranch.completeResolve(_scope);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.completeResolve(_scope);
        }
        return _cond && _then && _else;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
    public boolean checkType() {
        boolean _condOk = this.condition.getType().equalsTo(fr.n7.stl.minic.ast.type.AtomicType.BooleanType);
        boolean _thenOk = this.thenBranch.checkType();
        boolean _elseOk = true;
        if (this.elseBranch != null) {
            _elseOk = this.elseBranch.checkType();
        }
        
        if (!_condOk) {
            System.err.println("Erreur : La condition du 'if' n'est pas un booléen.");
        }
        return _condOk && _thenOk && _elseOk;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
    public int allocateMemory(Register _register, int _offset) {
        this.thenBranch.allocateMemory(_register, _offset);
        if (this.elseBranch != null) {
            this.elseBranch.allocateMemory(_register, _offset);
        }
        return 0;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _fragment = _factory.createFragment();
        int _id = _factory.createLabelNumber();
        
        String _elseLabel = "else_" + _id;
        String _endifLabel = "endif_" + _id;

        _fragment.append(this.condition.getCode(_factory));
        _fragment.add(_factory.createJumpIf(_elseLabel, 0));
        
        _fragment.append(this.thenBranch.getCode(_factory));
        _fragment.add(_factory.createJump(_endifLabel));
        
        // Branche ELSE
        Fragment _elseFragment = _factory.createFragment();
        if (this.elseBranch != null) {
            _elseFragment.append(this.elseBranch.getCode(_factory));
        }
        
        // Sécurité : on ajoute une instruction vide pour porter le label
        _elseFragment.add(_factory.createPush(0));
        _elseFragment.add(_factory.createPop(0, 1));
        
        _elseFragment.addPrefix(_elseLabel);
        _fragment.append(_elseFragment);
        
        // Fin du IF
        Fragment _endFragment = _factory.createFragment();
        
        // Sécurité : on ajoute une instruction vide pour porter le label
        _endFragment.add(_factory.createPush(0));
        _endFragment.add(_factory.createPop(0, 1));
        
        _endFragment.addPrefix(_endifLabel);
        _fragment.append(_endFragment);
        
        return _fragment;
    }

}
