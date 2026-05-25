/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.util.Logger;

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
		if (this.type != null) {
			return this.type;
		}
		return AtomicType.ErrorType;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// On collecte l'expression cible
		return ((Expression) this.target).collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
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

	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
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

	}

}
