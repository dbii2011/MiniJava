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
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.AtomicType;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
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
<<<<<<< HEAD
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean res = this.condition.collectAndPartialResolve(_scope);
		res = res && this.thenBranch.collectAndPartialResolve(_scope);
		if (this.elseBranch != null) {
			res = res && this.elseBranch.collectAndPartialResolve(_scope);
		}
		return res;
	}
=======
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _cond = this.condition.collectAndPartialResolve(_scope);
        boolean _then = this.thenBranch.collectAndPartialResolve(_scope);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.collectAndPartialResolve(_scope);
        }
        return _cond && _then && _else;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
<<<<<<< HEAD
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		boolean res = this.condition.collectAndPartialResolve(_scope);
		res = res && this.thenBranch.collectAndPartialResolve(_scope, _container);
		if (this.elseBranch != null) {
			res = res && this.elseBranch.collectAndPartialResolve(_scope, _container);
		}
		return res;
	}
=======
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
        boolean _cond = this.condition.collectAndPartialResolve(_scope);
        boolean _then = this.thenBranch.collectAndPartialResolve(_scope, _container);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.collectAndPartialResolve(_scope, _container);
        }
        return _cond && _then && _else;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
<<<<<<< HEAD
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean res = this.condition.completeResolve(_scope);
		res = res && this.thenBranch.completeResolve(_scope);
		if (this.elseBranch != null) {
			res = res && this.elseBranch.completeResolve(_scope);
		}
		return res;
	}
=======
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean _cond = this.condition.completeResolve(_scope);
        boolean _then = this.thenBranch.completeResolve(_scope);
        boolean _else = true;
        if (this.elseBranch != null) {
            _else = this.elseBranch.completeResolve(_scope);
        }
        return _cond && _then && _else;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
<<<<<<< HEAD
	public boolean checkType() {
		// La condition doit impérativement être un booléen
		boolean res = this.condition.getType().compatibleWith(AtomicType.BooleanType);
		res = res && this.thenBranch.checkType();
		if (this.elseBranch != null) {
			res = res && this.elseBranch.checkType();
		}
		return res;
	}
=======
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
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
<<<<<<< HEAD
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return 0; // if ne prend pas d'espace
	}
=======
    public int allocateMemory(Register _register, int _offset) {
        this.thenBranch.allocateMemory(_register, _offset);
        if (this.elseBranch != null) {
            this.elseBranch.allocateMemory(_register, _offset);
        }
        return 0;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
<<<<<<< HEAD
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.append(this.condition.getCode(_factory));
		
		// étiquettes
    	String elseLabel = "Label_" + _factory.createLabelNumber();
   		String endLabel = "Label_" + _factory.createLabelNumber();

		if (this.elseBranch != null) {
			frag.add(_factory.createJumpIf(String.valueOf(elseLabel), 0));
			frag.append(this.thenBranch.getCode(_factory)); 
			frag.add(_factory.createJump(endLabel));
			
			frag.addSuffix(elseLabel);
			frag.append(this.elseBranch.getCode(_factory)); 
			frag.addSuffix(endLabel);
		} else {
			frag.add(_factory.createJumpIf(String.valueOf(endLabel), 0));
			frag.append(this.thenBranch.getCode(_factory)); 
			frag.addSuffix(String.valueOf(endLabel)); 
		}
		return frag;
	}
=======
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
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

}
