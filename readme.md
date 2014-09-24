Neo4j Relationship Mover
================================

Note: This is a work in progress, made public to facilitate reviews.

This utility provides you the ability to move relationships from one node to another.

  Regardless of Direction
  Regardless of Relationship Type

This utility provides you the ability to move relationships from one node to another

* With this tool, you can move (relink) all nodes from one node to a different node.
    - Regardless of Direction
    - Regardless of Relationship Type
    - Refer to test class MoveAllRelationships_MT for an example

* One caveat is that you cannot move relationships...
    - that are between the source and destination node, or
    - that have the source node as both nodes in the relationship.

* Options
    - DIRECTION - Refer to MoveInboundRelationships_MT as an example
    - WITH_LABEL - Refer to MoveRelationshipsWithLabel_MT as an example



