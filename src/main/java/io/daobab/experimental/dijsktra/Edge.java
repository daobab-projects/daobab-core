package io.daobab.experimental.dijsktra;


import io.daobab.model.Column;
import io.daobab.model.Entity;

public class Edge {
    private final Vertex source;
    private final Vertex destination;
    private final int weight;

    private Entity fromNode;
    private Entity toNode;
    private Column column;

    public Edge(Vertex source, Vertex destination, int weight, Entity from, Entity to, Column column) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.setFromNode(from);
        this.setToNode(to);
        this.setColumn(column);
    }


    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }


    public Entity getFromNode() {
        return fromNode;
    }

    public void setFromNode(Entity fromNode) {
        this.fromNode = fromNode;
    }

    public Entity getToNode() {
        return toNode;
    }

    public void setToNode(Entity toNode) {
        this.toNode = toNode;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}