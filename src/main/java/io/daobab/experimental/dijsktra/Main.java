package io.daobab.experimental.dijsktra;

public class Main {

//
//
//  public <R extends EntityRelation> List<JoinWrapper> test(List<Entity> bunch, List<Entity> sourcePoints, List<Entity> destinationPoints){
//
//
//    List<Vertex> nodes = new ArrayList<Vertex>();
//    List<Edge> edges=new LinkedList<>();
//
//    for (Entity b:bunch){
//      nodes.add(new Vertex(b));
//    }
//
//    for (int i=0;i<bunch.size();i++){
//      Entity left=bunch.get(i);
//      for (Column<?,?,?> col:left.columns()){
//        for (int r=0;r<bunch.size();r++){
//          Entity right=bunch.get(r);
//          if (left.getEntityName().equals(right.getEntityName())) continue;
//          for (Column<?,?,?> colright:right.columns()){
//            if (col.getColumnName().equals(colright.getColumnName()) && col.getFieldClass().equals(colright.getFieldClass()) && oneOfThemIsPK(left,right,col,colright)){
//              edges.add(new Edge("",nodes.get(i),nodes.get(r),1,left,right,getLink(left,right,col,colright)));
//            }
//          }
//        }
//      }
//    }
//
//    List<JoinWrapper> rv=new LinkedList<>();
//    Graph graph = new Graph(nodes, edges);
//    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
//
//    for (Entity sourcePoint:sourcePoints){
//        dijkstra.execute(getByEntity(sourcePoint,nodes));
//
//        for (Entity destinationPoint:destinationPoints) {
//            LinkedList<Vertex> path = dijkstra.getPath(getByEntity(destinationPoint, nodes));
//
//            for (int i = 0; i < path.size(); i++) {
//                System.out.println(path.get(i));
//                if (i < path.size() - 1) {
//                    Edge e = getEdge(path.get(i), path.get(i + 1), edges);
//                    JoinWrapper jw = new JoinWrapper(JoinType.INNER,e.getColumn(),(R)e.getToNode());
//                    System.out.println("by column " + e.getColumn().getColumnName());
//                    rv.add(jw);
//                }
//
//            }
//        }
//
//    }
//
//    return rv;
//
//  }
//
//  private Vertex getByEntity(Entity entity,List<Vertex> list){
//      for (Vertex v: list){
//          if (v.getEntity().getEntityName().equals(entity.getEntityName())) return v;
//      }
//      return null;
//  }
//
//  private Edge getEdge(Vertex from,Vertex to,List<Edge> edges){
//    for (Edge e: edges){
//      if ((e.getFromNode().getEntityName().equals(from.getEntity().getEntityName()) && e.getToNode().getEntityName().equals(to.getEntity().getEntityName())) || (e.getFromNode().getEntityName().equals(to.getEntity().getEntityName()) && e.getToNode().getEntityName().equals(from.getEntity().getEntityName())) ){
//        return e;
//      }
//    }
//    return null;
//  }
//
//  private boolean oneOfThemIsPK(Entity left,Entity right, Column leftcolumn,Column rightcolumn){
//     boolean leftIsPK=left instanceof PrimaryKey;
//     boolean rightIsPK=right instanceof PrimaryKey;
//
//    boolean leftColIsPK =(leftIsPK && leftcolumn.getColumnName().equals( ((PrimaryKey)left).colID().getColumnName()));
//    boolean rightColIsPK =(rightIsPK && rightcolumn.getColumnName().equals(((PrimaryKey)right).colID().getColumnName()));
//
//    return leftColIsPK||rightColIsPK;
//
//  }
//
//  private Column getLink(Entity left,Entity right, Column leftcolumn,Column rightcolumn){
//    boolean leftIsPK=left instanceof PrimaryKey;
//    boolean rightIsPK=right instanceof PrimaryKey;
//
//    boolean leftColIsPK =(leftIsPK && leftcolumn.getColumnName().equals( ((PrimaryKey)left).colID().getColumnName()));
//    boolean rightColIsPK =(rightIsPK && rightcolumn.getColumnName().equals(((PrimaryKey)right).colID().getColumnName()));
//
//    if (leftColIsPK){
//      return leftcolumn;
//    }else if (rightColIsPK){
//      return rightcolumn;
//    }return null;
//
//  }

}