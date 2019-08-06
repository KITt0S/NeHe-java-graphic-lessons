import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class ToMake {

    public static void main(String[] args) {

        final GLProfile glProfile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( glProfile );

        final GLCanvas canvas = new GLCanvas( capabilities );
        Triangle triangle = new Triangle();
        canvas.addGLEventListener( triangle );
        canvas.setSize( 400, 400 );

        final JFrame frame = new JFrame( "Moving triangle" );
        frame.getContentPane().add( canvas );
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
    }
}
