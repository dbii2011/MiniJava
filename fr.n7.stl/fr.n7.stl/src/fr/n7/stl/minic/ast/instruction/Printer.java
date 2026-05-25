/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.Type;

/**
 * Implementation of the Abstract Syntax Tree node for a printer instruction.
 * @author Marc Pantel
 *
 */
public class Printer implements Instruction {

	protected Expression parameter;

	public Printer(Expression _value) {
		this.parameter = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "print " + this.parameter + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        // On délègue la collecte à l'expression (ex: l'appel fact(5))
        return this.parameter.collectAndPartialResolve(_scope);
    }
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
        return this.parameter.collectAndPartialResolve(_scope);
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        // On délègue la résolution à l'expression
        return this.parameter.completeResolve(_scope);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
        // On vérifie que ce qu'on imprime n'est pas une erreur de type
        return !this.parameter.getType().equals(AtomicType.ErrorType);
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
        // Print ne réserve pas d'espace sur la pile, il consomme juste la valeur
        return 0;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
	    Fragment _fragment = _factory.createFragment();
	    _fragment.append(this.parameter.getCode(_factory));
	    
	    Type _type = this.parameter.getType();
	    
	    if (_type.compatibleWith(AtomicType.IntegerType)) {
	        _fragment.add(Library.IOut); //
	    } else if (_type.compatibleWith(AtomicType.BooleanType)) {
	        _fragment.add(Library.BOut); //
	    } else if (_type.compatibleWith(AtomicType.CharacterType)) {
	        _fragment.add(Library.COut); //
	    } else if (_type.compatibleWith(AtomicType.StringType)) {
	        _fragment.add(Library.SOut); //
	    }
	    // On ignore le cas FloatingType car Library ne le supporte pas
	    
	    return _fragment;
	}
    }


