/**
 * 
 */
package fr.n7.stl.minic.ast.expression.accessible;

import fr.n7.stl.minic.ast.expression.AbstractIdentifier;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
=======

import fr.n7.stl.minic.ast.expression.AbstractAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.declaration.LabelDeclaration;

/**
 * Implementation of the Abstract Syntax Tree node for an identifier access expression.
 * @author Marc Pantel
 */
public class IdentifierAccess extends AbstractIdentifier implements AccessibleExpression {

	protected AbstractAccess expression;
	protected LabelDeclaration labelDeclaration = null;

	public IdentifierAccess(String _name) {
		super(_name);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (((HierarchicalScope<Declaration>) _scope).knows(this.name)) {
			Declaration _declaration = _scope.get(this.name);
			if (_declaration instanceof VariableDeclaration) {
				this.expression = new VariableAccess((VariableDeclaration) _declaration);
			} else if (_declaration instanceof ConstantDeclaration) {
				this.expression = new ConstantAccess((ConstantDeclaration) _declaration);
			} else if (_declaration instanceof ParameterDeclaration) {
				this.expression = new ParameterAccess((ParameterDeclaration) _declaration);
			} else if (_declaration instanceof LabelDeclaration) {
				this.labelDeclaration = (LabelDeclaration) _declaration;
			}
		}
		return true;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (this.expression == null && this.labelDeclaration == null) {
			if (((HierarchicalScope<Declaration>) _scope).knows(this.name)) {
				Declaration _declaration = _scope.get(this.name);
				if (_declaration instanceof VariableDeclaration) {
					this.expression = new VariableAccess((VariableDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof ConstantDeclaration) {
					this.expression = new ConstantAccess((ConstantDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof ParameterDeclaration) {
					this.expression = new ParameterAccess((ParameterDeclaration) _declaration);
					return true;
				} else if (_declaration instanceof LabelDeclaration) {
					this.labelDeclaration = (LabelDeclaration) _declaration;
					return true;
				} else {
					Logger.error("The declaration for " + this.name + " is of the wrong kind.");
					return false;
				}
			} else {
				Logger.error("The identifier " + this.name + " has not been found.");
				return false;
			}
		}
		return true;
	}

	@Override
	public Type getType() {
		if (this.labelDeclaration != null) {
			return this.labelDeclaration.getType();
		}
		return this.expression.getType();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.labelDeclaration != null) {
			Fragment frag = _factory.createFragment();
			frag.add(_factory.createLoadL(this.labelDeclaration.getIndex()));
			return frag;
		}
		return this.expression.getCode(_factory);
	}

}
=======

/**
 * Implementation of the Abstract Syntax Tree node for an identifier access expression (can be a variable,
 * a parameter, a constant, a function, ...).
 * Will be connected to an appropriate object after resolving the identifier to know to which kind of element
 * it is associated (variable, parameter, constant, function, ...).
 * @author Marc Pantel
 * TODO : Should also hold a function and not only a variable.
 */
public class IdentifierAccess extends AbstractIdentifier implements AccessibleExpression {
	
	protected AbstractAccess expression;
	
	/**
	 * Creates a variable use expression Abstract Syntax Tree node.
	 * @param _name Name of the used variable.
	 */
	public IdentifierAccess(String _name) {
		super(_name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override 
	public String toString() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
	    if (_scope.knows(this.name)) {
	        Declaration _declaration = _scope.get(this.name);
	        if (_declaration instanceof VariableDeclaration) {
	            this.expression = new VariableAccess((VariableDeclaration) _declaration);
	        } else if (_declaration instanceof ConstantDeclaration) {
	            this.expression = new ConstantAccess((ConstantDeclaration) _declaration);
	        } else if (_declaration instanceof FunctionDeclaration) {
	            // AJOUT : On lie l'identifiant à une déclaration de fonction
	            // Il te faudra probablement créer une classe FunctionAccess
	            // this.expression = new FunctionAccess((FunctionDeclaration) _declaration);
	        } else {
	            return false;
	        }
	        return true;
	    }
	    return false;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
	    Declaration _decl = _scope.get(this.name);
	    if (_decl instanceof VariableDeclaration) {
	        this.expression = new VariableAccess((VariableDeclaration) _decl);
	    } else if (_decl instanceof ConstantDeclaration) {
	        this.expression = new ConstantAccess((ConstantDeclaration) _decl);
	    }
	    return true;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.expression.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
	    // Si l'identifiant pointe sur une fonction, l'objet 'expression' 
	    // (ex: FunctionAccess) saura s'il doit charger l'adresse de la fonction.
	    if (this.expression != null) {
	        return this.expression.getCode(_factory);
	    }
	    return _factory.createFragment();
	}

}
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
