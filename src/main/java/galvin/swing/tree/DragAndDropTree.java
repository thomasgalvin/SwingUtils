package galvin.swing.tree;

import galvin.swing.ApplicationWindow;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DragAndDropTree
    extends JTree
    implements DragSourceListener,
               DropTargetListener,
               DragGestureListener
{

    public static final int PIXELS_ABOVE_AND_BELOW = 5;
    private static DataFlavor[] supportedFlavors;
    private DragSource dragSource;
    private DropTarget dropTarget;
    private DraggableTreeNode dropTargetNode;
    private TreePath dropTargetPath;
    private boolean nodeWillBeInsertedAbove;
    private boolean nodeWillBeInsertedBelow;
    private List draggedObjects = new ArrayList();
    private DraggableTreeNode rootNode;

    public DragAndDropTree()
    {
        this( new DraggableTreeNode( "Root" ) );
    }

    public DragAndDropTree( DraggableTreeNode rootNode )
    {
        this.rootNode = rootNode;
        setCellRenderer( new DraggableTreeCellRenderer() );
        setModel( new DefaultTreeModel( rootNode ) );

        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this,
                                                       DnDConstants.ACTION_COPY_OR_MOVE,
                                                       this );

        dropTarget = new DropTarget( this, this );
    }

    public List<DefaultMutableTreeNode> getSelectedNodes()
    {
        List<DefaultMutableTreeNode> result = new ArrayList();

        TreePath[] selectedPaths = getSelectionPaths();
        if( selectedPaths != null )
        {
            for(TreePath treePath : selectedPaths)
            {
                Object lastComponent = treePath.getLastPathComponent();
                if( lastComponent instanceof DefaultMutableTreeNode )
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastComponent;
                    result.add( node );
                }
            }
        }

        return result;
    }

    public void select( DefaultMutableTreeNode node )
    {
        if( node != null )
        {
            DefaultTreeModel model = (DefaultTreeModel) getModel();

            TreeNode parent = node.getParent();
            if( parent != null )
            {
                TreeNode[] parentNodes = model.getPathToRoot( parent );
                TreePath parentPath = new TreePath( parentNodes );
                expandPath( parentPath );
            }

            TreeNode[] treeNodes = model.getPathToRoot( node );
            TreePath treePath = new TreePath( treeNodes );
            setSelectionPath( treePath );
        }
    }

    public boolean isExpanded( DefaultMutableTreeNode node )
    {
        if( node != null )
        {
            DefaultTreeModel model = (DefaultTreeModel) getModel();
            TreeNode[] nodes = model.getPathToRoot( node );
            if( nodes != null )
            {
                TreePath path = new TreePath( nodes );
                return isExpanded( path );
            }
        }

        return false;
    }

    public void setExpanded( DefaultMutableTreeNode node, boolean expanded )
    {
        if( node != null )
        {
            DefaultTreeModel model = (DefaultTreeModel) getModel();
            TreeNode[] nodes = model.getPathToRoot( node );
            if( nodes != null )
            {
                TreePath path = new TreePath( nodes );

                if( expanded )
                {
                    expandPath( path );
                }
                else
                {
                    collapsePath( path );
                }
            }
        }
    }

    //////////////////////////////////////
    // Sibling / Child convinience methods
    //////////////////////////////////////
    public void addChild( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode )
    {
        DefaultTreeModel model = (DefaultTreeModel) getModel();

        int index = target.getChildCount();
        model.insertNodeInto( newNode, target, index );
    }

    public void addSibling( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode )
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) target.getParent();
        DefaultTreeModel model = (DefaultTreeModel) getModel();

        if( parent == null )
        {
            parent = target;
        }

        int index = parent.getChildCount();
        model.insertNodeInto( newNode, parent, index );
    }

    public void addSiblingAfter( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode )
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) target.getParent();


        DefaultTreeModel model = (DefaultTreeModel) getModel();
        int index = 0;

        if( parent != null )
        {
            index = parent.getIndex( target );
            index++;
        }
        else
        {
            parent = target;
        }

        model.insertNodeInto( newNode, parent, index );
    }

    /**
     * Can be overridden by children to control drag-and-drop behavior.
     */
    public boolean acceptDrop( DropTargetDropEvent dropTargetDropEvent )
    {
        Point dragPoint = dropTargetDropEvent.getLocation();
        dropTargetPath = getDropTargetPath( dragPoint );
        DraggableTreeNode node = (DraggableTreeNode) dropTargetPath.getLastPathComponent();
        if( node != null )
        {
            return node.getAllowsMoreChildren();
        }

        return true;
    }

    public void dragGestureRecognized( DragGestureEvent dragGestureEvent )
    {
        draggedObjects.clear();
        TreePath[] selectedPaths = getSelectionPaths();
        if( selectedPaths != null )
        {
            for(TreePath selectedPath : selectedPaths)
            {
                Object[] path = selectedPath.getPath();
                Object selectedObject = path[path.length - 1];
                draggedObjects.add( selectedObject );
            }

            Transferable transferable = new LocalTransferable( draggedObjects );
            dragSource.startDrag( dragGestureEvent, Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ), transferable, this );
        }
    }

    public void dragEnter( DragSourceDragEvent dsde )
    {
    }

    public void dragEnter( DropTargetDragEvent dropTargetDragEvent )
    {
        dropTargetDragEvent.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE );
    }

    public void dragOver( DragSourceDragEvent dsde )
    {
    }

    public void dragOver( DropTargetDragEvent dropTargetDragEvent )
    {
        nodeWillBeInsertedAbove = false;
        nodeWillBeInsertedBelow = false;

        Point dragPoint = dropTargetDragEvent.getLocation();
        dropTargetPath = getDropTargetPath( dragPoint );

        if( dropTargetPath != null )
        {
            dropTargetNode = (DraggableTreeNode) dropTargetPath.getLastPathComponent();

            if( dropTargetNode.getAllowsChildren()
                && dropTargetNode.getAllowsMoreChildren() )
            {
                Rectangle nodeBounds = getPathBounds( dropTargetPath );
                Point dropPoint = dropTargetDragEvent.getLocation();

                if( nodeBounds != null && dropPoint != null )
                {
                    if( dropPoint.y - nodeBounds.y <= PIXELS_ABOVE_AND_BELOW )
                    {
                        nodeWillBeInsertedAbove = true;
                    }
                    else if( ( nodeBounds.height + nodeBounds.y ) - dropPoint.y
                             <= PIXELS_ABOVE_AND_BELOW )
                    {
                        nodeWillBeInsertedBelow = true;
                    }
                }
            }
            else
            {
                dropTargetNode = null;
                dropTargetPath = null;
            }

            //setExpandedState( dropTargetPath, true );
            //getExpandsSelectedPaths();
        }

        repaint();
    }

    public void dragDropEnd( DragSourceDropEvent dsde )
    {
        dropTargetNode = null;
        draggedObjects.clear();
        repaint();
    }

    public DraggableTreeNode getDroppedNode( DropTargetDropEvent dropTargetDropEvent )
    {
        List<DraggableTreeNode> result = getDroppedNodes( dropTargetDropEvent );
        if( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        
        return null;
    }
    
    public List<DraggableTreeNode> getDroppedNodes( DropTargetDropEvent dropTargetDropEvent )
    {
        List<DraggableTreeNode> result = new ArrayList();
        
        Point dragPoint = dropTargetDropEvent.getLocation();
        dropTargetPath = getDropTargetPath( dragPoint );

        if( dropTargetPath != null )
        {
            DraggableTreeNode dropTargetNode = (DraggableTreeNode) dropTargetPath.getLastPathComponent();
            if( dropTargetNode != null )
            {
                try
                {
                    DefaultTreeModel model = (DefaultTreeModel) getModel();
                    Object droppedObject = dropTargetDropEvent.getTransferable().getTransferData( getSupportedFlavors()[0] );

                    if( droppedObject instanceof List )
                    {
                        List droppedList = (List) droppedObject;
                        for(Object object : droppedList)
                        {
                            DraggableTreeNode droppedNode = createDraggableTreeNode( object );
                            result.add( droppedNode );
                        }
                    }
                }
                catch( Throwable t )
                {
                    t.printStackTrace();
                }
            }
        }
        
        return result;
    }

    public void drop( DropTargetDropEvent dropTargetDropEvent )
    {
        System.out.println( "drop: " + this );

        if( !acceptDrop( dropTargetDropEvent ) )
        {
            dropTargetDropEvent.rejectDrop();
        }
        else
        {
            Point dragPoint = dropTargetDropEvent.getLocation();
            dropTargetPath = getDropTargetPath( dragPoint );

            if( dropTargetPath != null )
            {
                DraggableTreeNode dropTargetNode = (DraggableTreeNode) dropTargetPath.getLastPathComponent();
                if( dropTargetNode != null )
                {
                    try
                    {
                        DefaultTreeModel model = (DefaultTreeModel) getModel();
                        Object droppedObject = dropTargetDropEvent.getTransferable().getTransferData( getSupportedFlavors()[0] );

                        if( droppedObject instanceof List )
                        {
                            List droppedList = (List) droppedObject;
                            for(Object object : droppedList)
                            {
                                DraggableTreeNode droppedNode = createDraggableTreeNode( object );
                                if( !droppedNode.isNodeDescendant( dropTargetNode ) )
                                {
                                    if( nodeWillBeInsertedAbove
                                        || nodeWillBeInsertedBelow )
                                    {
                                        DraggableTreeNode parent = (DraggableTreeNode) dropTargetNode.getParent();
                                        if( parent != null
                                            && parent.getAllowsChildren()
                                            && parent.getAllowsMoreChildren() )
                                        {
                                            if( droppedNode.canBeReparented() || droppedNode.getParent() == parent )
                                            {

                                                if( nodeWillBeInsertedAbove )
                                                {
                                                    insertNodeAbove( droppedNode, dropTargetNode );
                                                }
                                                else if( nodeWillBeInsertedBelow )
                                                {
                                                    insertNodeBelow( droppedNode, dropTargetNode );
                                                }
                                            }
                                            else
                                            {
                                                dropTargetDropEvent.rejectDrop();
                                            }
                                        }
                                        else
                                        {
                                            dropTargetDropEvent.rejectDrop();
                                        }
                                    }
                                    else
                                    {
                                        if( droppedNode.canBeReparented() || droppedNode.getParent() == dropTargetNode )
                                        {
                                            insertNodeAsNewChild( droppedNode, dropTargetNode );
                                        }
                                        else
                                        {
                                            dropTargetDropEvent.rejectDrop();
                                        }
                                    }
                                }
                                else
                                {
                                    dropTargetDropEvent.rejectDrop();
                                }
                            }

                            dropTargetDropEvent.acceptDrop( DnDConstants.ACTION_MOVE );
                            dropTargetDropEvent.dropComplete( true );
                        }
                    }
                    catch( Throwable t )
                    {
                        t.printStackTrace();
                    }
                }
                else
                {
                    dropTargetDropEvent.rejectDrop();
                }
            }
            else
            {
                dropTargetDropEvent.rejectDrop();
            }
        }
    }

    public void insertNodeAbove( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode )
    {
        System.out.println( "base class: insertNodeAbove" );

        DraggableTreeNode parent = (DraggableTreeNode) dropTargetNode.getParent();
        DefaultTreeModel model = (DefaultTreeModel) getModel();

        TreePath path = new TreePath( model.getPathToRoot( parent ) );
        boolean isExpanded = isExpanded( path ) || parent.isLeaf();

        TreePath dropPath = new TreePath( model.getPathToRoot( dropTargetNode ) );
        System.out.println( "  dropPath: " + dropPath );
        if( dropPath != null )
        {
            model.removeNodeFromParent( droppedNode );
        }

        int index = parent.getIndex( dropTargetNode );
        model.insertNodeInto( droppedNode, parent, index );

        if( isExpanded )
        {
            expandPath( path );
        }

        System.out.println( "" );
    }

    public void insertNodeBelow( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode )
    {
        System.out.println( "base class: insertNodeBelow" );

        DraggableTreeNode parent = (DraggableTreeNode) dropTargetNode.getParent();
        DefaultTreeModel model = (DefaultTreeModel) getModel();

        TreePath path = new TreePath( model.getPathToRoot( parent ) );
        boolean isExpanded = isExpanded( path ) || parent.isLeaf();

        TreePath dropPath = new TreePath( model.getPathToRoot( dropTargetNode ) );
        System.out.println( "  dropPath: " + dropPath );
        if( dropPath != null )
        {
            model.removeNodeFromParent( droppedNode );
        }

        int index = parent.getIndex( dropTargetNode ) + 1;
        model.insertNodeInto( droppedNode, parent, index );

        if( isExpanded )
        {
            expandPath( path );
        }

        System.out.println( "" );
    }

    public void insertNodeAsNewChild( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode )
    {
        System.out.println( "base class: insertNodeAsNewChild" );

        DefaultTreeModel model = (DefaultTreeModel) getModel();

        TreePath path = new TreePath( model.getPathToRoot( dropTargetNode ) );
        boolean isExpanded = isExpanded( path ) || dropTargetNode.isLeaf();

        TreePath dropPath = new TreePath( model.getPathToRoot( dropTargetNode ) );
        System.out.println( "  dropPath: " + dropPath );
        if( dropPath != null )
        {
            model.removeNodeFromParent( droppedNode );
        }

        model.insertNodeInto( droppedNode, dropTargetNode, dropTargetNode.getChildCount() );

        if( isExpanded )
        {
            expandPath( path );
        }

        System.out.println( "" );
    }

    public void dragExit( DragSourceEvent dse )
    {
    }

    public void dragExit( DropTargetEvent dte )
    {
        repaint();
    }

    public void dropActionChanged( DragSourceDragEvent dsde )
    {
    }

    public void dropActionChanged( DropTargetDragEvent dtde )
    {
    }

    public TreePath getDropTargetPath( Point point )
    {
        TreePath result = getPathForLocation( point.x, point.y );
        if( result == null )
        {
            dropTargetNode = null;

            int y = point.y;
            while( y >= 0 && result == null )
            {
                y--;
                result = getPathForLocation( point.x, y );
            }
        }

        return result;
    }

    private DraggableTreeNode createDraggableTreeNode( Object object )
    {
        if( object instanceof DraggableTreeNode )
        {
            return (DraggableTreeNode) object;
        }
        else
        {
            return new DraggableTreeNode( object );
        }
    }

    public boolean nodeWillBeInsertedAbove()
    {
        return nodeWillBeInsertedAbove;
    }

    public boolean nodeWillBeInsertedBelow()
    {
        return nodeWillBeInsertedBelow;
    }

    public DraggableTreeNode getDropTargetNode()
    {
        return dropTargetNode;
    }

    public DraggableTreeNode getRootNode()
    {
        return rootNode;
    }

    public static DataFlavor[] getSupportedFlavors()
    {
        if( supportedFlavors == null )
        {
            try
            {
                DataFlavor localObjectFlavor = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType );
                supportedFlavors = new DataFlavor[]
                {
                    localObjectFlavor
                };
            }
            catch( ClassNotFoundException cnfe )
            {
            }
        }

        return supportedFlavors;
    }

    public static class LocalTransferable
        implements Transferable
    {

        private Object payload;

        public LocalTransferable( Object payload )
        {
            this.payload = payload;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return getSupportedFlavors();
        }

        public boolean isDataFlavorSupported( DataFlavor flavor )
        {
            for(DataFlavor supportedFlavor : getSupportedFlavors())
            {
                if( supportedFlavor.equals( flavor ) )
                {
                    return true;
                }
            }
            return false;
        }

        public Object getTransferData( DataFlavor flavor )
            throws UnsupportedFlavorException, IOException
        {
            if( isDataFlavorSupported( flavor ) )
            {
                return payload;
            }

            throw new UnsupportedFlavorException( flavor );
        }
    }

    public static void main( String[] args )
    {
        JTree tree = new DragAndDropTree();
        tree.setEditable( true );

        DraggableTreeNode root = new DraggableTreeNode( "Project", false, true, true );
        DraggableTreeNode set1 = new DraggableTreeNode( "Draft", false, true );
        DraggableTreeNode set2 = new DraggableTreeNode( "Research", false, true );
        DraggableTreeNode set3 = new DraggableTreeNode( "Trash", false, true );
        set1.add( new DraggableTreeNode( "Chapter 01" ) );
        set1.add( new DraggableTreeNode( "Chapter 02" ) );
        set1.add( new DraggableTreeNode( "Chapter 03" ) );
        set2.add( new DraggableTreeNode( "Characters" ) );
        set2.add( new DraggableTreeNode( "Localtions" ) );
        set2.add( new DraggableTreeNode( "Outline" ) );
        set3.add( new DraggableTreeNode( "Chapter One" ) );
        set3.add( new DraggableTreeNode( "Chapter Two" ) );
        root.add( set1 );
        root.add( set2 );
        root.add( set3 );
        DefaultTreeModel mod = new DefaultTreeModel( root );
        tree.setModel( mod );

        // expand all
        for(int i = 0; i < tree.getRowCount(); i++)
        {
            tree.expandRow( i );
        }
        // show tree
        JScrollPane scroller = new JScrollPane( tree,
                                                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

        ApplicationWindow window = new ApplicationWindow( "DnD JTree" );
        window.getContentPane().add( scroller );
        window.pack();
        window.setVisible( true );
    }
}
