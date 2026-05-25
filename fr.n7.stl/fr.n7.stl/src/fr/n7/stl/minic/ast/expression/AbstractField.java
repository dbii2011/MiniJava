package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
<<<<<<< HEAD
import fr.n7.stl.minic.ast.type.NamedType;
import fr.n7.stl.minic.ast.type.RecordType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.minic.ast.type.AtomicType;
=======
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractField<RecordKind extends Expression> implements Expression {

	protected RecordKind record;
	protected String name;
	protected FieldDeclaration field;
	
	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(RecordKind _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.record + "." + this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
		// On transmet la collecte à l'expression de l'enregistrement
		return this.record.collectAndPartialResolve(_scope);
=======
	    // On propage la collecte à l'enregistrement parent
	    return this.record.collectAndPartialResolve(_scope);
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
<<<<<<< HEAD
		// On résout l'enregistrement
		boolean res = this.record.completeResolve(_scope);
		
		// On cherche à lier le champ à sa déclaration dans la structure
		Type recordType = this.record.getType();
		
		// Si le type est caché derrière un Typedef (NamedType)
		if (recordType instanceof NamedType) {
			recordType = ((NamedType) recordType).getType();
		}
		
		// Si on a bien affaire à une structure
		if (recordType instanceof RecordType) {
			this.field = ((RecordType) recordType).get(this.name);
		}
		
		return res && (this.field != null);
=======
	    // 1. Résoudre l'enregistrement (ex: la variable 'p' dans 'p.x')
	    boolean _res = this.record.completeResolve(_scope);
	    
	    if (_res) {
	        Type _type = this.record.getType();
	        // 2. Vérifier que c'est bien un enregistrement (RecordType)
	        if (_type instanceof fr.n7.stl.minic.ast.type.RecordType) {
	            fr.n7.stl.minic.ast.type.RecordType _recordType = (fr.n7.stl.minic.ast.type.RecordType) _type;
	            // 3. Chercher le champ par son nom
	            if (_recordType.contains(this.name)) {
	                this.field = _recordType.get(this.name);
	            } else {
	                fr.n7.stl.util.Logger.error("Le champ " + this.name + " n'existe pas dans l'enregistrement.");
	                _res = false;
	            }
	        } else {
	            fr.n7.stl.util.Logger.error(this.record + " n'est pas un enregistrement.");
	            _res = false;
	        }
	    }
	    return _res;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
<<<<<<< HEAD
	public Type getType() {
		if (this.field != null) {
			return this.field.getType();
		}
		
		Type recordType = this.record.getType();
		if (recordType instanceof NamedType) {
			recordType = ((NamedType) recordType).getType();
		}
		
		if (recordType instanceof RecordType) {
			FieldDeclaration fd = ((RecordType) recordType).get(this.name);
			if (fd != null) {
				return fd.getType();
			}
		}
		return AtomicType.ErrorType;
=======
	@Override
	public Type getType() {
	    if (this.field != null) {
	        return this.field.getType();
	    }
	    return fr.n7.stl.minic.ast.type.AtomicType.ErrorType;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	}

}