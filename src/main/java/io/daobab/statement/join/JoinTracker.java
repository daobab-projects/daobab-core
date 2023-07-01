package io.daobab.statement.join;

import io.daobab.experimental.dijsktra.DijkstraAlgorithm;
import io.daobab.experimental.dijsktra.Edge;
import io.daobab.experimental.dijsktra.Graph;
import io.daobab.experimental.dijsktra.Vertex;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.model.TableColumn;
import io.daobab.target.Target;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public class JoinTracker {

    public static List<JoinWrapper> calculateRoute(Target target, List<Entity> bunch, Collection<String> sourcePoints, Set<String> destinations, List<JoinWrapper> alreadyDefinedJoins) {
        return calculateJoins(target, bunch, sourcePoints, destinations, alreadyDefinedJoins);
    }

    public static List<JoinWrapper> calculateJoins(Target target, List<Entity> bunch, Collection<String> sourcePoints, Set<String> destinations, List<JoinWrapper> alreadyDefinedJoins) {
        return calculateJoins(target, bunch, getEntities(target, sourcePoints, bunch), destinations, alreadyDefinedJoins);
    }

    public static List<JoinWrapper> calculateThrougth(Target target, List<Entity> bunch, Collection<String> sourcePoints, Set<String> destinations, List<JoinWrapper> alreadyDefinedJoins, List<String> throughtPoints) {

        Set<String> fromcol = new HashSet<>();
        Set<String> tocol = new HashSet<>();


        for (String s : sourcePoints) {

            for (int i = 0; i < throughtPoints.size(); i++) {
                if (i > 0) {
                    fromcol.clear();
                    fromcol.add(throughtPoints.get(i - 1));
                }
                tocol.clear();
                tocol.add(throughtPoints.get(i));

                alreadyDefinedJoins = calculateJoins(target, bunch, getEntities(target, fromcol, bunch), tocol, alreadyDefinedJoins);

            }
            alreadyDefinedJoins = calculateJoins(target, bunch, getEntities(target, fromcol, bunch), tocol, alreadyDefinedJoins);
        }

        return alreadyDefinedJoins;
    }


    public static List<JoinWrapper> calculateJoins(Target target, List<Entity> bunch, List<Entity> sourcePoints, Set<String> dest, List<JoinWrapper> alreadyDefinedJoins) {
        Set<String> destinations = new HashSet<>();
        if (dest != null) destinations.addAll(dest);

        alreadyDefinedJoins.stream()
                .filter((jw) -> jw.getWhere() != null && jw.getWhere().getAllDaoInWhereClause(target) != null)
                .forEach((jw) -> destinations.addAll(jw.getWhere().getAllDaoInWhereClause(target)));

        List<Vertex> nodes = bunch.stream().map(Vertex::new).collect(Collectors.toList());
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < bunch.size(); i++) {
            Entity left = bunch.get(i);
            for (TableColumn tableColumn : left.columns()) {
                Column column = tableColumn.getColumn();
                for (int r = 0; r < bunch.size(); r++) {
                    Entity right = bunch.get(r);
                    if (target.getEntityName(left.entityClass()).equals(target.getEntityName(right.entityClass())))
                        continue;
                    for (TableColumn tableColumnRight : right.columns()) {
                        Column columnRight = tableColumnRight.getColumn();
                        if (column.getColumnName().equals(columnRight.getColumnName()) && column.getFieldClass().equals(columnRight.getFieldClass()) && oneOfThemIsPk(left, right, column, columnRight)) {
                            edges.add(new Edge(nodes.get(i), nodes.get(r), 1, left, right, getLink(left, right, column, columnRight)));
                        }
                    }
                }
            }
        }

        List<JoinWrapper> rv = new ArrayList<>();
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

        for (Entity sourcePoint : sourcePoints) {

            for (Entity destinationPoint : getEntities(target, destinations, bunch)) {
                dijkstra.execute(getByEntity(sourcePoint, nodes));
                List<Vertex> path = dijkstra.getPath(getByEntity(destinationPoint, nodes));
                if (path == null) continue;
                for (int i = 0; i < path.size(); i++) {
                    if (i < path.size() - 1) {
                        Edge e = getEdge(path.get(i), path.get(i + 1), edges);
                        JoinWrapper jw = new JoinWrapper(JoinType.INNER, e.getToNode(), e.getColumn());
                        if (!addedAlready(jw, rv)) rv.add(jw);
                    }
                }
            }
        }

        for (JoinWrapper jw : alreadyDefinedJoins) {
            if (!addedAlready(jw, rv)) rv.add(jw);
        }

        return rv;
    }

    public static boolean addedAlready(JoinWrapper jw, List<JoinWrapper> list) {
        if (jw == null || list == null) return true;
        for (JoinWrapper a : list) {
            if (a.getTable().entityClass().equals(jw.getTable().entityClass())
                    && a.getType().equals(jw.getType())
                    && a.getByColumn().getColumnName().equals(jw.getByColumn().getColumnName())
            ) return true;
        }
        return false;
    }

    public static Vertex getByEntity(Entity entity, List<Vertex> list) {
        for (Vertex v : list) {
            if (v.getEntity().entityClass().equals(entity.entityClass())) return v;
        }
        return null;
    }

    public static Edge getEdge(Vertex from, Vertex to, List<Edge> edges) {
        for (Edge e : edges) {
            if ((e.getFromNode().entityClass().equals(from.getEntity().entityClass()) && e.getToNode().entityClass().equals(to.getEntity().entityClass()))) {
                return e;
            }
        }
        return null;
    }

    public static boolean oneOfThemIsPk(Entity left, Entity right, Column leftcolumn, Column rightcolumn) {
        boolean leftIsPK = left instanceof PrimaryKey;
        boolean rightIsPK = right instanceof PrimaryKey;

        boolean leftColIsPK = (leftIsPK && leftcolumn.getColumnName().equals(((PrimaryKey) left).colID().getColumnName()));
        boolean rightColIsPK = (rightIsPK && rightcolumn.getColumnName().equals(((PrimaryKey) right).colID().getColumnName()));

        return leftColIsPK || rightColIsPK;
    }

    public static Column getLink(Entity left, Entity right, Column leftcolumn, Column rightcolumn) {
        boolean leftIsPK = left instanceof PrimaryKey;
        boolean rightIsPK = right instanceof PrimaryKey;

//        boolean leftColIsPK = (leftIsPK && leftcolumn.getColumnName().equals(((PrimaryKey) left).colID().getColumnName()));
//        boolean rightColIsPK = (rightIsPK && rightcolumn.getColumnName().equals(((PrimaryKey) right).colID().getColumnName()));

        return leftcolumn;
    }

    public static List<Entity> getEntities(Target target, Collection<String> names, List<Entity> allEntities) {
        List<Entity> rv = new ArrayList<>();
        if (allEntities == null || allEntities.isEmpty() || names == null || names.isEmpty()) return rv;

        for (String name : names) {
            for (Entity entity : allEntities) {
                if (name.equals(target.getEntityName(entity.entityClass()))) {
                    rv.add(entity);
                    break;
                }
            }
        }
        return rv;
    }
}
