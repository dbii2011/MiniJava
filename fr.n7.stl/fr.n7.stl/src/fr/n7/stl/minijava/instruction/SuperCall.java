package fr.n7.stl.minijava.instruction;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.Iterator;
import java.util.List;

public class SuperCall implements Instruction {
	
	protected ConstructorDeclaration constructor;
	
	protected List<AccessibleExpression> arguments;

	public SuperCall(List<AccessibleExpression> _arguments) {
		this.arguments = _arguments;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
    public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
        boolean ok = true;
        for (AccessibleExpression arg : this.arguments) {
            ok = ok && arg.completeResolve(_scope);
        }

        // --- RECHERCHE DU CONSTRUCTEUR PARENT ---
        if (_scope.knows("super")) {
            Type superType = _scope.get("super").getType();
            
            if (superType != null && superType instanceof fr.n7.stl.minijava.ast.type.ClassType) {
                fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration parentClass = 
                    ((fr.n7.stl.minijava.ast.type.ClassType) superType).declaration;
                
                if (parentClass != null) {
                    // On parcourt les éléments de la classe parente pour trouver son constructeur
                    for (fr.n7.stl.minijava.ast.type.declaration.ClassElement e : parentClass.getElements()) {
                        if (e instanceof ConstructorDeclaration) {
                            this.constructor = (ConstructorDeclaration) e;
                            break;
                        }
                    }
                }
            }
        }

        if (this.constructor == null) {
            System.err.println("Erreur : Impossible de trouver le constructeur parent pour l'appel super()");
            return false;
        }
        
        return ok;
    }

	@Override
	public boolean checkType() {
		boolean ok = true;

		// 1. SI le constructeur parent a été trouvé au moment du resolve (Vérification stricte)
		if (this.constructor != null) {
			// On récupère les paramètres attendus par le constructeur de la classe mère
			List<ParameterDeclaration> params = this.constructor.getParameters(); 
			
			// Vérification 1 : A-t-on le bon nombre d'arguments ?
			if (this.arguments.size() != params.size()) {
				System.err.println("Erreur : Nombre d'arguments incorrect pour l'appel super(...)");
				return false;
			}

			// Vérification 2 : Les types sont-ils compatibles ? (Unification)
			for (int i = 0; i < this.arguments.size(); i++) {
				Type argType = this.arguments.get(i).getType();
				if (argType == null) return false; // L'argument est invalide à la base
				
				Type paramType = params.get(i).getType();
				if (!argType.compatibleWith(paramType)) {
					System.err.println("Erreur : Argument " + i + " de type incompatible dans super(...)");
					return false;
				}
			}
			return true;
		}

		// 2. SINON (Filet de sécurité basique si le constructeur parent n'est pas encore lié)
		for (AccessibleExpression arg : this.arguments) {
			if (arg.getType() == null) {
				ok = false;
			}
		}
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	@Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment frag = _factory.createFragment();
        
        // 1. On empile l'adresse de l'objet courant ('this')
        // (Attention : vérifie si ton compilateur place 'this' à 3[LB] ou -1[LB] dans les constructeurs)
        frag.add(_factory.createLoad(Register.LB, -1, 1)); 
        
        // 2. On empile les arguments passés à super(...)
        for (AccessibleExpression arg : this.arguments) {
            frag.append(arg.getCode(_factory));
        }
        
        // 3. L'APPEL DÉCOMMENTÉ !
        if (this.constructor != null) {
            frag.add(_factory.createCall("constructor_" + this.constructor.getName(), Register.LB));
        }
        
        return frag;
    }
	
	@Override
	public String toString() {
		String image = "";
		image += "super( ";
		Iterator<AccessibleExpression> iterator = this.arguments.iterator();
		if (iterator.hasNext()) {
			AccessibleExpression argument = iterator.next();
			image += argument;
			while (iterator.hasNext()) {
				 argument = iterator.next();
				 image += " ," + argument;
			}
		}
		image += ");\n";
		return image;
	}

}
