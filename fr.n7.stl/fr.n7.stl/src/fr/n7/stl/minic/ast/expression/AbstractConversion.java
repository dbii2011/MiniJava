/**
 * 
 */
package fr.n7.stl.minic.ast.expression;
<<<<<<< HEAD
=======
import fr.n7.stl.minic.ast.instruction.declaration.TypeDeclaration;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.instruction.declaration.TypeDeclaration;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
<<<<<<< HEAD
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.util.Logger;
=======
import fr.n7.stl.tam.ast.TAMFactory;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected TargetType target;
	protected Type type;
	protected String name;

	public AbstractConversion(TargetType _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(TargetType _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
<<<<<<< HEAD
		if (this.type != null) {
			return this.type;
		}
		return AtomicType.ErrorType;
=======
	    // Le type résultant est le type cible de la conversion
	    return this.type;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
		// On collecte l'expression cible
		return ((Expression) this.target).collectAndPartialResolve(_scope);
=======
	    // On propage la collecte à la cible de la conversion
	    return ((Expression)this.target).collectAndPartialResolve(_scope);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
				boolean res = ((Expression) this.target).completeResolve(_scope);
		
		if (this.type == null && this.name != null) {
			// Le type cible a été donné sous forme de nom 
			if (_scope.knows(this.name)) {
				Declaration decl = _scope.get(this.name);
				if (decl instanceof TypeDeclaration) {
					this.type = decl.getType();
				} else {
					Logger.error("The declaration for " + this.name + " is not a valid type.");
					res = false;
				}
			} else {
				Logger.error("The type identifier " + this.name + " has not been found.");
				res = false;
			}
		} else if (this.type != null) {
			res = res && this.type.completeResolve(_scope);
		}
		
		return res;

=======
	    // 1. Résoudre la cible du cast
	    boolean _result = ((Expression) this.target).completeResolve(_scope);

	    if (_result) {
	        // 2. Si le type est défini par un nom (String), on le cherche dans le scope
	        if (this.type == null && this.name != null) {
	            Declaration _decl = _scope.get(this.name); // Ou _scope.get(this.name).get() si c'est un Optional
	            
	            if (_decl instanceof TypeDeclaration) {
	                // On récupère le vrai type associé au nom
	                this.type = ((TypeDeclaration) _decl).getType();
	            } else {
	                fr.n7.stl.util.Logger.error(this.name + " is not a type declaration.");
	                _result = false;
	            }
	        } 
	    } else {
	        fr.n7.stl.util.Logger.error(this.target + " has not been resolved.");
	    }
	    return _result;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
<<<<<<< HEAD
		Fragment _fragment = _factory.createFragment();
		
	
		_fragment.append(((Expression) this.target).getCode(_factory));
		
		
		Type sourceType = ((Expression) this.target).getType();
		Type targetType = this.getType();
 
		if (sourceType.compatibleWith(AtomicType.BooleanType)) {
			if (targetType.compatibleWith(AtomicType.CharacterType)) {
				_fragment.add(Library.B2C);
			} else if (targetType.compatibleWith(AtomicType.IntegerType)) {
				_fragment.add(Library.B2I);
			} else if (targetType.compatibleWith(AtomicType.StringType)) {
				_fragment.add(Library.B2S);
			}
			
		} else if (sourceType.compatibleWith(AtomicType.CharacterType)) {
			if (targetType.compatibleWith(AtomicType.BooleanType)) {
				_fragment.add(Library.C2B);
			} else if (targetType.compatibleWith(AtomicType.IntegerType)) {
				_fragment.add(Library.C2I);
			} else if (targetType.compatibleWith(AtomicType.StringType)) {
				_fragment.add(Library.C2S);
			}
			
		} else if (sourceType.compatibleWith(AtomicType.IntegerType)) {
			if (targetType.compatibleWith(AtomicType.BooleanType)) {
				_fragment.add(Library.I2B);
			} else if (targetType.compatibleWith(AtomicType.CharacterType)) {
				_fragment.add(Library.I2C);
			} else if (targetType.compatibleWith(AtomicType.StringType)) {
				_fragment.add(Library.I2S);
			}
			
		} else if (sourceType.compatibleWith(AtomicType.StringType)) {
			if (targetType.compatibleWith(AtomicType.BooleanType)) {
				_fragment.add(Library.S2B);
			} else if (targetType.compatibleWith(AtomicType.CharacterType)) {
				_fragment.add(Library.S2C);
			} else if (targetType.compatibleWith(AtomicType.IntegerType)) {
				_fragment.add(Library.S2I);
			}
			
		}
	
 
		return _fragment;

=======
	    // On génère le code pour évaluer l'expression cible
	    // La valeur résultante sera sur la pile
	    return ((Expression)this.target).getCode(_factory);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

}
