import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class ToMake {

    public static void main(String[] args) {

        final GLProfile glProfile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities glCapabilities = new GLCapabilities( glProfile );

        final GLCanvas glCanvas = new GLCanvas( glCapabilities );
        Line line = new Line();
        glCanvas.addGLEventListener( line );
        glCanvas.setSize( 400, 400 );

        final JFrame frame = new JFrame( "Straight line" );
        frame.getContentPane().add( glCanvas );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize( screenSize );
        frame.setVisible( true );
    }
}
