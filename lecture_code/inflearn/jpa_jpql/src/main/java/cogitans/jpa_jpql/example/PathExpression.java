package cogitans.jpa_jpql.example;

import javax.persistence.EntityManager;

public class PathExpression {

    public static void pathExpressionTypes(EntityManager em) {

        String stateFieldQuery = "SELECT m.username FROM Member m";
        String singleAssociationFieldQuery = "SELECT m.team.name FROM Member m";
    }

    public static void pathExpression(EntityManager em) {

        String query = "SELECT m.username FROM Team t join t.members m";
    }
}
