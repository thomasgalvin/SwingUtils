/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DraggableTreeCellRenderer
        extends DefaultTreeCellRenderer
{

    private boolean isTargetNode;
    private Insets normalInsets;
    private DragAndDropTree dragAndDropTree;
    private Color dropIndicatorColor = Color.BLACK;

    public DraggableTreeCellRenderer()
    {
        super();
        normalInsets = super.getInsets();
    }

    @Override
    public Component getTreeCellRendererComponent( JTree tree,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean isExpanded,
                                                   boolean isLeaf,
                                                   int row,
                                                   boolean hasFocus )
    {

        if( tree instanceof DragAndDropTree )
        {
            dragAndDropTree = ( DragAndDropTree ) tree;

            isTargetNode = ( value == dragAndDropTree.getDropTargetNode() );
            return super.getTreeCellRendererComponent( tree, value,
                                                       isSelected, isExpanded,
                                                       isLeaf, row, hasFocus );
        }

        throw new IllegalArgumentException( "DraggableTreeCellRenderer can only render for DragAndDropTrees" );
    }

    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if( isTargetNode )
        {
            g.setColor( dropIndicatorColor );
            if( dragAndDropTree.nodeWillBeInsertedAbove() )
            {
                g.drawLine( 0, 0, getSize().width, 0 );
            }
            else if( dragAndDropTree.nodeWillBeInsertedBelow() )
            {
                g.drawLine( 0, getSize().height - 1, getSize().width, getSize().height
                                                                      - 1 );
            }
            else
            {
                g.drawRect( 0, 0, getSize().width - 1, getSize().height - 1 );
            }
        }
    }

    public Color getDropIndicatorColor()
    {
        return dropIndicatorColor;
    }

    public void setDropIndicatorColor( Color dropIndicatorColor )
    {
        this.dropIndicatorColor = dropIndicatorColor;
    }
}
