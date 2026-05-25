/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import java.util.Iterator;
<<<<<<< HEAD
=======
import fr.n7.stl.tam.ast.Register;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
=======
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
<<<<<<< HEAD
 */
public class FunctionCall implements AccessibleExpression {

	protected String name;
	protected FunctionDeclaration function;
	protected List<AccessibleExpression> arguments;

=======
 *
 */
public class FunctionCall implements AccessibleExpression {

	/**
	 * Name of the called function.
	 * TODO : Should be an expression.
	 */
	protected String name;
	
	/**
	 * Declaration of the called function after name resolution.
	 * TODO : Should rely on the VariableUse class.
	 */
	protected FunctionDeclaration function;
	
	/**
	 * List of AST nodes that computes the values of the parameters for the function call.
	 */
	protected List<AccessibleExpression> arguments;
	
	/**
	 * @param _name : Name of the called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	public FunctionCall(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

<<<<<<< HEAD
	@Override
	public String toString() {
		String _result = this.name + "( ";
=======
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.function == null)?this.name:this.function) + "( ";
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
		Iterator<AccessibleExpression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
<<<<<<< HEAD
		return _result + ")";
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// On collecte les arguments
		boolean ok = true;
		for (AccessibleExpression arg : this.arguments) {
			ok &= arg.collectAndPartialResolve(_scope);
		}
		return ok;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// On résout le nom de la fonction
		if (_scope.knows(this.name)) {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof FunctionDeclaration) {
				this.function = (FunctionDeclaration) decl;
			} else {
				Logger.error("Identifier " + this.name + " is not a function.");
				return false;
			}
		} else {
			Logger.error("Function " + this.name + " is not declared.");
			return false;
		}
		// On résout les arguments
		boolean ok = true;
		for (AccessibleExpression arg : this.arguments) {
			ok &= arg.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public Type getType() {
		if (this.function == null) {
			throw new SemanticsUndefinedException("Function " + this.name + " has not been resolved.");
		}
		return this.function.getType();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.function == null) {
			throw new SemanticsUndefinedException("Function " + this.name + " has not been resolved.");
		}
		Fragment frag = _factory.createFragment();
		// On empile les arguments dans l'ordre
		for (AccessibleExpression arg : this.arguments) {
			frag.append(arg.getCode(_factory));
		}
		// On appelle la fonction via son label
		frag.add(_factory.createCall(this.function.getLabel(), Register.LB));
		return frag;
	}

}
=======
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _res = true;
        for (AccessibleExpression _arg : this.arguments) {
            _res &= _arg.collectAndPartialResolve(_scope);
        }
        return _res;
    }

    @Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean _res = true;
        // 1. Résoudre tous les arguments
        for (AccessibleExpression _arg : this.arguments) {
            _res &= _arg.completeResolve(_scope);
        }
        
        // 2. Chercher la fonction dans la table des symboles
        if (_scope.contains(this.name)) {
            Declaration _decl = _scope.get(this.name);
            if (_decl instanceof FunctionDeclaration) {
                this.function = (FunctionDeclaration) _decl;
            } else {
                fr.n7.stl.util.Logger.error(this.name + " n'est pas une fonction.");
                _res = false;
            }
        } else {
            fr.n7.stl.util.Logger.error("Fonction " + this.name + " non déclarée.");
            _res = false;
        }
        return _res;
    }
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
    @Override
    public Type getType() {
        return this.function.getType();
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _fragment = _factory.createFragment();
        
        // 1. On génère le code pour chaque argument (ils s'empilent dans l'ordre)
        for (AccessibleExpression _arg : this.arguments) {
            _fragment.append(_arg.getCode(_factory));
        }
        
        // 2. Appel de la fonction via son label (défini dans FunctionDeclaration)
        // On utilise Register.SB car en MiniC les fonctions sont au niveau global
        _fragment.add(_factory.createCall("begin:" + this.name, Register.SB));
        
        return _fragment;
    }

}
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
