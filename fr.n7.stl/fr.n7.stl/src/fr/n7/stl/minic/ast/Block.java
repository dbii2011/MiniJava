/**
 * 
 */
package fr.n7.stl.minic.ast;

import java.util.List;
<<<<<<< HEAD
=======
import fr.n7.stl.minic.ast.scope.SymbolTable;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Represents a Block node in the Abstract Syntax Tree node for the Bloc language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 * 
 * @author Marc Pantel
 *
 */
public class Block {

	/**
	 * Sequence of instructions contained in a block.
	 */
	protected List<Instruction> instructions;
<<<<<<< HEAD
=======
	protected int storageSize = 0;
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> _instructions) {
		this.instructions = _instructions;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n" ;
	}
	
	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and check
	 * that the declaration are allowed.
	 * @param _scope Inherited Scope attribute that contains the identifiers defined previously
	 * in the context.
	 * @return Synthesized Semantics attribute that indicates if the identifier declaration are
	 * allowed.
	 */
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
            boolean ok = true;
            for (Instruction instruction : this.instructions) {
                ok &= instruction.collectAndPartialResolve(_scope);
            }
            return ok;
	//	throw new SemanticsUndefinedException("Semantics collect is undefined in Block.");
	}
	
	/**
	 * Inherited Semantics attribute to collect all the identifiers declaration and check
	 * that the declaration are allowed.
	 * @param _scope Inherited Scope attribute that contains the identifiers defined previously
	 * in the context.
	 * @param _container Inherited Container attribute that allows to link the return instructions
	 * with the function declaration.
	 * @return Synthesized Semantics attribute that indicates if the identifier declaration are
	 * allowed.
	 */
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
<<<<<<< HEAD
		boolean ok = true;
		for (Instruction instruction : this.instructions) {
			ok &= instruction.collectAndPartialResolve(_scope, _container);
		}
		return ok;
	}
=======
        boolean ok = true;
        HierarchicalScope<Declaration> localScope = new SymbolTable(_scope);
        for (Instruction instruction : this.instructions) {
            ok &= instruction.collectAndPartialResolve(localScope, _container);
        }
        return ok;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f
	
	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined and
	 * associate all identifiers uses with their definitions.
	 * @param _scope Inherited Scope attribute that contains the defined identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used in the
	 * block have been previously defined.
	 */
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
            boolean ok = true;
            for (Instruction instruction : this.instructions) {
                ok &= instruction.completeResolve(_scope);
            }
            return ok;
	    // throw new SemanticsUndefinedException("Semantics completeResolve is undefined in Block.");
	}

	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */	
	public boolean checkType() {
        boolean ok = true;
        for (Instruction instruction : this.instructions) {
            ok &= instruction.checkType();
        }
        return ok;
		// throw new SemanticsUndefinedException("Semantics checkType is undefined in Block.");
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated memory. 
	 * @param _register Inherited Register associated to the address of the variables.
	 * @param _offset Inherited Current offset for the address of the variables.
	 */	
	public void allocateMemory(Register _register, int _offset) {
<<<<<<< HEAD
		int currentOffset = _offset;
		// On avance l'offset pour chaque instruction du bloc
		for (Instruction instruction : this.instructions) {
			currentOffset += instruction.allocateMemory(_register, currentOffset);
		}
	}
=======
        int currentOffset = 0; 
        for (Instruction instruction : this.instructions) {
            // Chaque instruction retourne la taille qu'elle occupe (ex: 1 pour un int)
            currentOffset += instruction.allocateMemory(_register, _offset + currentOffset);
        }
        // On sauvegarde la taille totale pour l'utiliser dans getCode
        this.storageSize = currentOffset; 
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * @param _factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */
	public Fragment getCode(TAMFactory _factory) {
<<<<<<< HEAD
		Fragment frag = _factory.createFragment();
		// On concatène le code de toutes les instructions du bloc
		for (Instruction instruction : this.instructions) {
			frag.append(instruction.getCode(_factory));
		}
		return frag;
	}
=======
        Fragment _fragment = _factory.createFragment();
        
        // 1. Réserver l'espace (ST = ST + n)
        if (this.storageSize > 0) {
            _fragment.add(_factory.createPush(this.storageSize));
        }
        
        // 2. Ajouter le code de chaque instruction
        for (Instruction instruction : this.instructions) {
            _fragment.append(instruction.getCode(_factory));
        }
        
        // 3. Nettoyer la pile en sortant (ST = ST - d - n)
        if (this.storageSize > 0) {
            // On retire n mots et on garde 0 résultat (d=0)
            _fragment.add(_factory.createPop(0, this.storageSize));
        }
        
        return _fragment;
    }
>>>>>>> 85da716e64ab002e03b4f6d57beb8d4f387ae33f

}
