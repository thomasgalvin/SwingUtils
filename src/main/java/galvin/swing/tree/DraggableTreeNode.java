/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class DraggableTreeNode
    extends DefaultMutableTreeNode
{

    private boolean draggable = true;
    private boolean allowMoreChildren = true;
    private boolean canBeReparented = true;

    public DraggableTreeNode()
    {
        super();
    }

    public DraggableTreeNode( Object userObject )
    {
        super( userObject );
    }

    public DraggableTreeNode( Object userObject, boolean draggable )
    {
        super( userObject );
        this.draggable = draggable;
    }

    public DraggableTreeNode( Object userObject, 
                              boolean draggable,
                              boolean allowsChildren )
    {
        this( userObject, draggable, allowsChildren, true );
    }

    public DraggableTreeNode( Object userObject, 
                              boolean draggable,
                              boolean allowsChildren, 
                              boolean allowsMoreChildren )
    {
        this( userObject, draggable, allowsChildren, true, true );
    }
    
    public DraggableTreeNode( Object userObject, 
                              boolean draggable,
                              boolean allowsChildren, 
                              boolean allowsMoreChildren,
                              boolean canBeReparented)
    {
        setDraggable( draggable );
        setAllowsChildren( allowsChildren );
        setAllowMoreChildren( allowsMoreChildren );
        setCanBeReparented( canBeReparented );
    }

    public boolean isDraggable()
    {
        return draggable;
    }

    public void setDraggable( boolean draggable )
    {
        this.draggable = draggable;
    }

    public boolean getAllowsMoreChildren()
    {
        return allowMoreChildren;
    }

    public void setAllowMoreChildren( boolean allowMoreChildren )
    {
        this.allowMoreChildren = allowMoreChildren;
    }

    public boolean canBeReparented()
    {
        return canBeReparented;
    }

    public void setCanBeReparented( boolean canBeReparented )
    {
        this.canBeReparented = canBeReparented;
    }

    @Override
    public void add( MutableTreeNode newChild )
    {
        if( newChild instanceof DraggableTreeNode )
        {
            super.add( newChild );
        }
        else
        {
            throw new IllegalArgumentException( "Node " + newChild + " is not of type DraggableTreeNode." );
        }
    }

    @Override
    public void insert( MutableTreeNode newChild, int index )
    {
        if( newChild instanceof DraggableTreeNode )
        {
            super.insert( newChild, index );
        }
        else
        {
            throw new IllegalArgumentException( "Node " + newChild + " is not of type DraggableTreeNode." );
        }
    }
}
