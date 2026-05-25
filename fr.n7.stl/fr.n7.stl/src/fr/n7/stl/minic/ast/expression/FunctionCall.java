/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import java.util.Iterator;
import fr.n7.stl.tam.ast.Register;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
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
	public FunctionCall(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.function == null)?this.name:this.function) + "( ";
		Iterator<AccessibleExpression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
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
