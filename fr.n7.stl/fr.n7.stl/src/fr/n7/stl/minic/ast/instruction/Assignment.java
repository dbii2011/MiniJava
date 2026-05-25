/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.NamedType;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
<<<<<<< HEAD
import fr.n7.stl.util.Logger;
=======
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Implementation of the Abstract Syntax Tree node for an array type.
 * @author Marc Pantel
 *
 */
public class Assignment implements Instruction, Expression {

	protected Expression value;
	protected AssignableExpression assignable;

	/**
	 * Create an assignment instruction implementation from the assignable expression
	 * and the assigned value.
	 * @param _assignable Expression that can be assigned a value.
	 * @param _value Value assigned to the expression.
	 */
	public Assignment(AssignableExpression _assignable, Expression _value) {
		this.assignable = _assignable;
		this.value = _value;
		/* This attribute will be assigned to the appropriate value by the resolve action */
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assignable + " = " + this.value.toString() + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
<<<<<<< HEAD
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean res1 = this.assignable.collectAndPartialResolve(_scope);
		boolean res2 = this.value.collectAndPartialResolve(_scope);
		return res1 && res2;
	}
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		boolean res1 = this.assignable.collectAndPartialResolve(_scope);
		boolean res2 = this.value.collectAndPartialResolve(_scope);
		return res1 && res2;
=======
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        boolean _assignable = this.assignable.collectAndPartialResolve(_scope);
        boolean _value = this.value.collectAndPartialResolve(_scope);
        return _assignable && _value;
    }

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
	    // Pour l'assignable et la valeur (qui sont des Expressions), 
	    // on appelle la méthode standard à UN SEUL paramètre.
	    boolean _assignable = this.assignable.collectAndPartialResolve(_scope);
	    boolean _value = this.value.collectAndPartialResolve(_scope); 
	    return _assignable && _value;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
<<<<<<< HEAD
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean res1 = this.assignable.completeResolve(_scope);
		boolean res2 = this.value.completeResolve(_scope);
		return res1 && res2;
	}
=======
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        // On résout les deux côtés de l'affectation
        boolean _assignable = this.assignable.completeResolve(_scope);
        boolean _value = this.value.completeResolve(_scope);
        return _assignable && _value;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#getType()
	 */
	@Override
<<<<<<< HEAD
	public Type getType() {
		return this.value.getType();
	}
=======
    public Type getType() {
        // Le type d'une affectation est le type de la valeur affectée
        return this.value.getType();
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
<<<<<<< HEAD
		Type targetType = this.assignable.getType();
        Type valueType = this.value.getType();

        if (targetType instanceof NamedType) {
            targetType = ((NamedType) targetType).getType();
        }
        if (valueType instanceof NamedType) {
            valueType = ((NamedType) valueType).getType();
        }

        if (valueType.compatibleWith(targetType)) {
            return true;
        } else {
            Logger.error("Type mismatch in assignment.");
            return false;
        }
	}
=======
        // On vérifie que le type de la valeur est compatible avec la destination
        boolean _res = this.value.getType().compatibleWith(this.assignable.getType());
        if (! _res) {
            System.err.println("Erreur de type : types incompatibles dans l'affectation.");
        }
        return _res;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
<<<<<<< HEAD
		return 0;
	}
=======
        // L'affectation ne réserve pas de nouvel espace sur la pile
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
	
		// Empiler la valeur (côté droit)
    	frag.append(this.value.getCode(_factory));

		// Empiler l'adresse de destination (côté gauche)
    	frag.append(this.assignable.getCode(_factory));
    	int size = this.assignable.getType().length();
    	frag.add(_factory.createStoreI(size));
		return frag;
	}
=======
    public Fragment getCode(TAMFactory _factory) {
        Fragment _fragment = _factory.createFragment();
        
        // 1. On calcule la valeur (mise sur la pile)
        _fragment.append(this.value.getCode(_factory));
        
        // 2. On calcule l'adresse de destination (mise sur la pile au-dessus de la valeur)
        _fragment.append(this.assignable.getCode(_factory));
        
        // 3. On stocke la valeur à l'adresse calculée
        // StoreI prend en paramètre la taille du type à copier
        _fragment.add(_factory.createStoreI(this.value.getType().length()));
        
        return _fragment;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

}
