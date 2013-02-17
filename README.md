Transient 2 Desktop Level Editor
=======================

A java application for creating and testing levels for the next version of Transient.  You can run this app by cloning this repo and running the executable 'editor.jar'. 

Basic Instruction:

- Click a tile in the tile window then place it in the level window.  You can rotate the tile using a mouse scrollwheel.  

- Right click a tile in the tile window to edit its collision vectors and properties.

- Click 'options' -> 'test play' to test your level.

Test play: 

- w,s,a,d -> arrow keys 

- You can re-jump midair.  This is only for testing purposes and will be removed for the real game.

Development notes:

- No 3rd party libraries or frameworks.

- [Collision testing](https://github.com/petekinnecom/transient2_level_editor/blob/master/src/org/petekinnecom/t2_level_pieces/Line.java#L155):
  
  - Based on whiteboard math session.

  - Detect collisions very well, need some work on corrections.

  - Current behavior is reasonable, with the occasional oddity.

- Controller and View code already ported to Android.

- Model code completely portable.
