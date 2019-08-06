import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class ToMake {

    public static void main(String[] args) {

        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        final GLCanvas glCanvas = new GLCanvas( capabilities );
        //Triangle triangle = new Triangle();
        Polygon polygon = new Polygon();
        glCanvas.addGLEventListener( polygon );
        glCanvas.setSize( 400, 400 );

        final JFrame frame = new JFrame( "Triangle" );
        frame.getContentPane().add( glCanvas );
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
    }
}
