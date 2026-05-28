package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.expression.AbstractAttribute;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAssignment extends AbstractAttribute<AssignableExpression> implements AssignableExpression {

    public AttributeAssignment(AssignableExpression _object, String _name) {
        super(_object, _name);
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment _result = _factory.createFragment();

        /* =================================================================
         * CALCUL DE L'ADRESSE ABSOLUE DE L'ATTRIBUT
         * ================================================================= */
        // 1. On évalue l'objet à gauche. En tant qu'AssignableExpression ou Expression,
        // son code va pousser son adresse de référence (dans le Tas) sur la pile.
        _result.append(this.object.getCode(_factory));

        // 2. On récupère l'offset calculé par Imane (0 par défaut)
        // On récupère le vrai offset calculé pendant la passe allocateMemory
        int offset = ((AttributeDeclaration) this.attribute).getOffset();

        // 3. Si l'attribut a un déplacement, on l'ajoute à l'adresse de base de l'objet
        if (offset > 0) {
            _result.add(_factory.createLoadL(offset));
            _result.add(Library.IAdd); // Adresse absolue = Adresse de base + Offset
        }

        // En TAM, pour une AssignableExpression, on s'arrête ici ! 
        // La pile contient maintenant l'adresse mémoire exacte où on veut écrire.
        return _result;
    }
}