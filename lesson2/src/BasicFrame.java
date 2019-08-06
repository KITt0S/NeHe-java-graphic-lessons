import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

/**
 * Created by KITS on 21.05.2017.
 */
public class BasicFrame implements GLEventListener {
    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    public static void main(String[] args) {

        final GLProfile glProfile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( glProfile );

        final GLCanvas glCanvas = new GLCanvas( capabilities );
        BasicFrame basicFrame = new BasicFrame();
        glCanvas.addGLEventListener( basicFrame );
        glCanvas.setSize( 400, 400 );

        final JFrame frame = new JFrame( "Basic frame" );
        frame.getContentPane().add( glCanvas );
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
    }
}
