package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.expression.AbstractAttribute;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAccess extends AbstractAttribute<AccessibleExpression> implements AccessibleExpression {

    // protected Declaration attributeDeclaration; // Pour sauvegarder la déclaration de l'attribut après résolution

    public AttributeAccess(AccessibleExpression _object, String _name) {
        super(_object, _name);
    }

    @Override
    public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
        // 1. On résout d'abord l'objet à gauche du point (hérité d'AbstractAttribute)
        // Note: Si 'this.object' ne compile pas, vérifie le nom du champ dans AbstractAttribute (parfois 'protected T object;')
        return this.object.collectAndPartialResolve(_scope);
    }

    // @Override
    // public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
    //     // 1. On termine la résolution de l'objet
    //     if (!this.object.completeResolve(_scope)) {
    //         return false;
    //     }

    //     // 2. On récupère le type de l'objet (qui doit être une classe)
    //     Type targetType = this.object.getType();
        
    //     if (targetType instanceof ClassType) {
    //         ClassDeclaration classDecl = ((ClassType) targetType).declaration;
            
    //         if (classDecl != null) {
    //             // On utilise la nouvelle méthode qui remonte la chaîne d'héritage !
    //             this.attributeDeclaration = classDecl.getAttribute(this.name);
                
    //             if (this.attributeDeclaration != null) {
    //                 return true;
    //             }
    //         }
    //     }

    //     System.err.println("Erreur sémantique : L'attribut " + this.name + " n'est pas défini dans la hiérarchie.");
    //     return false;
    // }

    // @Override
    // public Type getType() {
    //     return ((AttributeDeclaration) this.attributeDeclaration).getType();
    // }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();

        /* =================================================================
         * 1. ADRESSE DE BASE DE L'OBJET
         * ================================================================= */
        // On pousse l'adresse de référence de l'objet (sur le Tas) sur la pile TAM
        _result.append(this.object.getCode(_factory));

        /* =================================================================
         * 2. CALCUL DU DÉPLACEMENT (OFFSET)
         * ================================================================= */
        // REPERE COLLABORATION MEMBRE 1 : À remplacer par le vrai calcul d'offset d'Imane
        
        int offset = ((AttributeDeclaration) this.attribute).getOffset();

        // Si l'attribut n'est pas le premier de la classe, on ajoute son offset
        if (offset > 0) {
            _result.add(_factory.createLoadL(offset));
            _result.add(Library.IAdd); // Adresse absolue de l'attribut = Base + Offset
        }

        /* =================================================================
         * 3. CHARGEMENT DE LA VALEUR (Comme en miniC !)
         * ================================================================= */
        int attributeSize = this.getType().length();
        
        // LOADI va chercher la valeur dans le Tas à l'adresse calculée
        _result.add(_factory.createLoadI(attributeSize));

        return _result;
    }
}